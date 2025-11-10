/* ===== NAV BOOTSTRAP ===== */

async function bootstrapNav(){
    const ph = document.getElementById("nav-placeholder");
    if(!ph) return;
    const html = await fetch("/nav.html").then(r=>r.text());
    ph.innerHTML = html;
}


/* ===== ADD MATCH BOOTSTRAP ===== */

let addMatchReady = false;

async function bootstrapAddMatch(){
    const ph = document.getElementById("add-match-placeholder");
    if(!ph) return;

    const html = await fetch("/add-match.html").then(r=>r.text());
    ph.innerHTML = html;

    // start collapsed
    const box = document.getElementById("add-match-container");
    if (box) box.style.display = "none";

    // init logic after snippet exists
    if (typeof initAddMatchForm === "function") {
        await initAddMatchForm();
    }

    addMatchReady = true;
}

function toggleAddMatch(){
    if(!addMatchReady) return;
    const box = document.getElementById("add-match-container");
    if(!box) return;
    box.style.display = (box.style.display === "none") ? "block" : "none";
}


/* ===== API HELPERS ===== */

const GET = (url) => fetch(url).then(r=>r.json());

const api = {
    sports:      () => GET('/sports'),
    venues:      () => GET('/venues'),
    leagues:     () => GET('/leagues'),
    teams:       () => GET('/teams'),
    leagueTeams: (id) => GET(`/leagues/${id}/teams`),
    teamLeagues: (id) => GET(`/teams/${id}/leagues`)
};


/* ===== CORE: RENDER & DATA LOAD ===== */

function render(data) {
    const tbody = document.querySelector('#matches tbody');
    if (!tbody) return;

    tbody.innerHTML = data.map(m => {
        const d = new Date(m.startAt);
        const date = d.toLocaleDateString("de-DE", { timeZone: "Europe/Vienna" });
        const time = d.toLocaleTimeString("de-DE", { hour: "2-digit", minute: "2-digit", timeZone: "Europe/Vienna" });

        const score = (m.homeScore != null && m.awayScore != null) ? `${m.homeScore}:${m.awayScore}` : "–";
        const percent = (m.status !== "scheduled" || m.homeWinProbability == null)
            ? "–"
            : Math.round(m.homeWinProbability * 100) + "%";

        return `
      <tr>
        <td>${date}</td>
        <td>${time}</td>
        <td class="clickable" onclick="openSport(${m.league.sport.id})">${m.league.sport.name}</td>
        <td class="clickable" onclick="openLeague(${m.league.id})">${m.league.name}</td>
        <td class="clickable" onclick="openMatch(${m.id})">${m.title}</td>
        <td class="clickable" onclick="openVenue(${m.venue.id})">${m.venue.name}</td>
        <td>${score}</td>
        <td>${percent}</td>
      </tr>`;
    }).join('');
}

function load(url) {
    fetch(url)
        .then(r => r.json())
        .then(res => render(Array.isArray(res) ? res : res.items));
}


/* ===== FILTERS (SPA via URL) ===== */

function restoreFilterUIFromUrl() {
    const qs = new URLSearchParams(location.search);

    // clear selects if search is active
    if (qs.has("q")) {
        ["sport","status","venue","league","team"].forEach(k => {
            const el = document.getElementById(`${k}-select`);
            if (el) el.value = "";
        });
        return;
    }

    // mirror URL params into selects
    const map = {
        sportId:  "sport-select",
        status:   "status-select",
        venueId:  "venue-select",
        leagueId: "league-select",
        teamId:   "team-select"
    };
    Object.entries(map).forEach(([param, id]) => {
        const el = document.getElementById(id);
        if (!el) return;
        el.value = qs.get(param) || "";
    });
}

function applyFilters() {
    // build params from selects
    const params = new URLSearchParams();

    const sportId  = document.getElementById('sport-select').value;
    const status   = document.getElementById('status-select').value;
    const venueId  = document.getElementById('venue-select').value;
    const leagueId = document.getElementById('league-select').value;
    const teamId   = document.getElementById('team-select').value;

    if (sportId)  params.set("sportId", sportId);
    if (status)   params.set("status", status);
    if (venueId)  params.set("venueId", venueId);
    if (leagueId) params.set("leagueId", leagueId);
    if (teamId)   params.set("teamId", teamId);

    // filters replace search (remove ?q)
    // update URL without reload
    history.replaceState(null, "", params.toString() ? `?${params}` : "/");

    // load filtered or all
    load(params.toString() ? `/matches?${params}` : `/matches`);
}

function resetFilters() {
    // clear URL to root
    history.replaceState(null, "", "/");

    // clear UI
    ["sport","status","venue","league","team"].forEach(k=>{
        const el = document.getElementById(`${k}-select`);
        if(el) el.value="";
    });

    // load all
    load('/matches');
}


/* ===== SEARCH (separate from filters) ===== */

function searchGlobal() {
    const q = (document.getElementById('search-input')?.value || "").trim();

    // update URL to ?q or /
    history.replaceState(null, "", q ? `?q=${encodeURIComponent(q)}` : "/");

    // clear selects when searching
    ["sport","status","venue","league","team"].forEach(k => {
        const el = document.getElementById(`${k}-select`);
        if (el) el.value = "";
    });

    // load search or all
    load(q ? `/matches?q=${encodeURIComponent(q)}` : `/matches`);
}


/* ===== FILTER SELECT OPTIONS (once) ===== */

function fillSelect(id, list) {
    const sel = document.getElementById(id);
    if (!sel) return;
    list.forEach(e => {
        const opt = document.createElement('option');
        opt.value = e.id;
        opt.textContent = e.name;
        sel.appendChild(opt);
    });
}

// prefill filter dropdowns
api.sports().then(list => fillSelect('sport-select', list));
api.venues().then(list => fillSelect('venue-select', list));
api.leagues().then(list => fillSelect('league-select', list));
api.teams().then(list => fillSelect('team-select', list));


/* ===== NAVIGATION SHORTCUTS ===== */

const openSport  = id => location.href = `/sport.html?id=${id}`;
const openLeague = id => location.href = `/league.html?id=${id}`;
const openMatch  = id => location.href = `/match.html?id=${id}`;
const openVenue  = id => location.href = `/venue.html?id=${id}`;
const openTeam   = id => location.href = `/team.html?id=${id}`;


/* ===== DETAIL PAGES INIT ===== */

function initLeaguePage() {
    const id = new URLSearchParams(location.search).get("id");
    if (!id) return;

    fetch(`/leagues/${id}`).then(r => r.json())
        .then(l => document.getElementById('league-name').textContent = l.name);

    fetch(`/leagues/${id}/teams`).then(r => r.json())
        .then(teams => {
            const tb = document.querySelector("#teams tbody");
            if(!tb) return;
            tb.innerHTML = teams.map(t =>
                `<tr><td class="clickable" onclick="openTeam(${t.id})">${t.name}</td></tr>`
            ).join('');
        });
}

function initTeamPage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id) return;

    fetch(`/teams/${id}`).then(r => r.json())
        .then(t => document.getElementById("team-name").textContent = t.name);

    fetch(`/teams/${id}/leagues`).then(r => r.json())
        .then(leagues => {
            const tb = document.querySelector("#leagues tbody");
            if(!tb) return;
            tb.innerHTML = leagues.map(l =>
                `<tr><td class="clickable" onclick="openLeague(${l.id})">${l.name}</td></tr>`
            ).join('');
        });
}

function initSportPage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id) return;

    fetch(`/sports/${id}`).then(r => r.json())
        .then(s => document.getElementById("sport-name").textContent = s.name);

    fetch(`/sports/${id}/leagues`).then(r => r.json())
        .then(leagues => {
            const tb = document.querySelector("#sport-leagues tbody");
            if(!tb) return;
            tb.innerHTML = leagues.map(l =>
                `<tr><td class="clickable" onclick="openLeague(${l.id})">${l.name}</td></tr>`
            ).join('');
        });

    fetch(`/sports/${id}/teams`).then(r => r.json())
        .then(teams => {
            const tb = document.querySelector("#sport-teams tbody");
            if(!tb) return;
            tb.innerHTML = teams.map(t =>
                `<tr><td class="clickable" onclick="openTeam(${t.id})">${t.name}</td></tr>`
            ).join('');
        });
}

function initMatchPage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id){ document.body.innerHTML = "<h1>Missing ?id= in URL</h1>"; return; }

    fetch(`/matches/${id}`).then(r => r.json()).then(m => {
        document.getElementById("match-title").textContent = m.title;

        if (m.status === "finished") {
            const score = `${m.homeScore}:${m.awayScore}`;
            document.getElementById('probability').innerHTML = `<h2>Final score: ${score}</h2>`;
            return;
        }
        if (m.status === "live") {
            document.getElementById('probability').innerHTML = `<h2>Game in progress ...</h2>`;
            return;
        }
        if (m.status === "scheduled") {
            const p = m.homeWinProbability;
            if (p == null) {
                document.getElementById('probability').innerHTML =
                    "<h2>No prediction available</h2><p>Not enough historic data</p>";
            } else {
                const percent = Math.round(p * 100);
                document.getElementById('probability').innerHTML = `
          <h2>Home win probability: ${percent}%</h2>
          <div class="prob-bar">
            <div class="prob-bar-fill" style="width:${percent}%"></div>
          </div>`;
            }
            return;
        }
        document.getElementById('probability').innerHTML = `<h2>No info available for this match</h2>`;
    });
}

function initVenuePage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id){ document.body.innerHTML = "<h1>Missing ?id= in URL</h1>"; return; }

    fetch(`/venues/${id}`).then(r => r.json()).then(v => {
        document.getElementById("venue-name").textContent = v.name;
        document.getElementById("venue-meta").innerHTML =
            `City: ${v.city}<br>Capacity: ${v.capacity.toLocaleString("de-DE")}`;
    });
}


/* ===== ADD MATCH: LOGIC ===== */

async function getOpponentTeams(teamId){
    // compute union of teams across all leagues of selected team
    const leagues = await api.teamLeagues(teamId);
    const teamSets = await Promise.all(leagues.map(l => api.leagueTeams(l.id)));
    const all = teamSets.flat();
    const unique = Array.from(new Map(all.map(t => [t.id, t])).values());
    return unique.filter(t => t.id != teamId);
}

function renderSelect(sel, list, preserve){
    sel.innerHTML = '<option value="">-</option>';
    list.forEach(o => {
        sel.insertAdjacentHTML("beforeend", `<option value="${o.id}">${o.name}</option>`);
    });
    if (preserve && list.some(t => t.id == preserve)) sel.value = preserve;
}

async function initAddMatchForm(){
    const title = document.getElementById("title-input");
    if(!title) return;

    // cache base lists
    window.ALL_LEAGUES = await api.leagues();
    window.ALL_TEAMS   = await api.teams();
    const venues       = await api.venues();

    // initial options
    renderSelect(document.getElementById('league-select-add'), window.ALL_LEAGUES);
    renderSelect(document.getElementById('home-select-add'),   window.ALL_TEAMS);
    renderSelect(document.getElementById('away-select-add'),   window.ALL_TEAMS);
    renderSelect(document.getElementById('venue-select-add'),  venues);

    // dependent dropdowns
    ["league-select-add","home-select-add","away-select-add"]
        .forEach(id => document.getElementById(id).addEventListener("change", recomputeAddMatchForm));

    // save handler
    document.getElementById("save-btn").addEventListener("click", saveMatch);
}

async function recomputeAddMatchForm(){
    // read current selections
    const leagueSel = document.getElementById("league-select-add");
    const homeSel   = document.getElementById("home-select-add");
    const awaySel   = document.getElementById("away-select-add");

    const leagueId = leagueSel.value || null;
    const homeId   = homeSel.value   || null;
    const awayId   = awaySel.value   || null;

    // base candidates
    let homeOptions = window.ALL_TEAMS;
    let awayOptions = window.ALL_TEAMS;
    let leagueOptions = window.ALL_LEAGUES;

    // narrow by league
    if (leagueId){
        const inLeague = await api.leagueTeams(leagueId);
        homeOptions = inLeague;
        awayOptions = inLeague;
        leagueOptions = [window.ALL_LEAGUES.find(l => l.id == leagueId)];
    }

    // narrow by home
    if (homeId){
        const opp = await getOpponentTeams(homeId);
        awayOptions = awayOptions.filter(t => opp.some(o => o.id == t.id));
        leagueOptions = await api.teamLeagues(homeId);
    }

    // narrow by away
    if (awayId){
        const opp = await getOpponentTeams(awayId);
        homeOptions = homeOptions.filter(t => opp.some(o => o.id == t.id));
        leagueOptions = await api.teamLeagues(awayId);
    }

    // render with preserved selections if still valid
    renderSelect(homeSel, homeOptions, homeId);
    renderSelect(awaySel, awayOptions, awayId);
    renderSelect(leagueSel, leagueOptions, leagueId);
}

async function saveMatch(){
    // build payload
    const body = {
        title: document.getElementById("title-input").value,
        leagueId: +document.getElementById("league-select-add").value,
        venueId: +document.getElementById("venue-select-add").value,
        homeTeamId: +document.getElementById("home-select-add").value,
        awayTeamId: +document.getElementById("away-select-add").value,
        startAt: new Date(document.getElementById("startAt-input").value).toISOString()
    };

    // POST and open created resource
    const r = await fetch("/matches", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(body)
    });

    if (!r.ok){
        alert("Error saving match");
        return;
    }

    const newId = await r.json();
    openMatch(newId);
}


/* ===== HOME ENTRY POINT ===== */

window.addEventListener("DOMContentLoaded", async () => {
    // load nav and add-match snippet
    await bootstrapNav();
    await bootstrapAddMatch();

    // reflect URL → selects
    restoreFilterUIFromUrl();

    // initial load rule A: load ALL unless URL has params
    const hasParams = !!location.search;
    const url = hasParams ? `/matches${location.search}` : `/matches`;
    load(url);
});

/********************************************
 * MATCH TABLE RENDERING
 ********************************************/
function render(data) {
    const tbody = document.querySelector('#matches tbody');
    tbody.innerHTML = data.map(m => {
        const d = new Date(m.startAt);
        const date = d.toLocaleDateString("de-DE", { timeZone: "Europe/Vienna" });
        const time = d.toLocaleTimeString("de-DE", { hour: "2-digit", minute: "2-digit", timeZone: "Europe/Vienna" });

        const score = (m.homeScore != null && m.awayScore != null)
            ? `${m.homeScore}:${m.awayScore}`
            : "–";

        const percent = (() => {
            if (m.status !== "scheduled") return "–";

            // scheduled:
            if (m.homeWinProbability == null) return "–";

            return Math.round(m.homeWinProbability * 100) + "%";
        })();


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
        </tr>
        `;
    }).join('');
}


function load(url) {
    fetch(url)
        .then(r => r.json())
        .then(res => {
            if (Array.isArray(res)) render(res)
            else render(res.items)
        })

}

function loadAll() {
    load('/matches');

    ["sport-select","status-select","venue-select","league-select","team-select"]
        .forEach(id => document.getElementById(id).value = "");
}


/********************************************
 * FILTER HANDLING
 ********************************************/
function applyFilters() {
    const params = new URLSearchParams();

    const sportId  = document.getElementById('sport-select').value;
    const status   = document.getElementById('status-select').value;
    const venueId  = document.getElementById('venue-select').value;
    const leagueId = document.getElementById('league-select').value;
    const teamId   = document.getElementById('team-select').value;

    if (sportId)  params.append("sportId", sportId);
    if (status)   params.append("status", status);
    if (venueId)  params.append("venueId", venueId);
    if (leagueId) params.append("leagueId", leagueId);
    if (teamId)   params.append("teamId", teamId);

    const url = params.toString() ? `/matches?${params.toString()}` : `/matches`;
    load(url);
}

function searchGlobal() {
    const q = document.getElementById('search-input').value.trim();
    if(q === "") { loadAll(); return; }
    load(`/matches?q=${encodeURIComponent(q)}`);
}


/********************************************
 * DROP-DOWN POPULATION
 ********************************************/
function fillSelect(id, list) {
    const sel = document.getElementById(id);
    list.forEach(e => {
        const opt = document.createElement('option');
        opt.value = e.id;
        opt.textContent = e.name;
        sel.appendChild(opt);
    });
}

fetch('/sports').then(r => r.json()).then(list => fillSelect('sport-select',  list));
fetch('/venues').then(r => r.json()).then(list => fillSelect('venue-select',  list));
fetch('/leagues').then(r => r.json()).then(list => fillSelect('league-select', list));
fetch('/teams').then(r => r.json()).then(list => fillSelect('team-select',   list));

loadAll();


/********************************************
 * NAVIGATION TO DETAIL PAGES
 ********************************************/
function openSport(id){ location.href = `/sport.html?id=${id}`; }
function openLeague(id){ location.href = `/league.html?id=${id}`; }
function openMatch(id){ location.href = `/match.html?id=${id}`; }
function openVenue(id){ location.href = `/venues.html?id=${id}`; }
function openTeam(id){ location.href = `/team.html?id=${id}`; }


/********************************************
 * DETAIL PAGE INITIALIZERS
 ********************************************/
function initLeaguePage() {
    const id = new URLSearchParams(location.search).get("id");
    if (!id) return;

    fetch(`/leagues/${id}`)
        .then(r => r.json())
        .then(l => document.getElementById('league-name').textContent = l.name);

    fetch(`/leagues/${id}/teams`)
        .then(r => r.json())
        .then(teams => {
            const tbody = document.querySelector("#teams tbody");
            tbody.innerHTML = teams.map(t =>
                `<tr><td class="clickable" onclick="openTeam(${t.id})">${t.name}</td></tr>`
            ).join('');
        });
}

function initTeamPage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id) return;

    fetch(`/teams/${id}`)
        .then(r => r.json())
        .then(t => document.getElementById("team-name").textContent = t.name);

    fetch(`/teams/${id}/leagues`)
        .then(r => r.json())
        .then(leagues => {
            const tbody = document.querySelector("#leagues tbody");
            tbody.innerHTML = leagues.map(l =>
                `<tr><td class="clickable" onclick="openLeague(${l.id})">${l.name}</td></tr>`
            ).join('');
        });
}

function initSportPage(){
    const id = new URLSearchParams(location.search).get("id");
    if(!id) return;

    // load sport info
    fetch(`/sports/${id}`)
        .then(r => r.json())
        .then(s => document.getElementById("sport-name").textContent = s.name);

    // load leagues
    fetch(`/sports/${id}/leagues`)
        .then(r => r.json())
        .then(leagues => {
            const tbody = document.querySelector("#sport-leagues tbody");
            tbody.innerHTML = leagues.map(l =>
                `<tr><td class="clickable" onclick="openLeague(${l.id})">${l.name}</td></tr>`
            ).join('');
        });

    // load teams
    fetch(`/sports/${id}/teams`)
        .then(r => r.json())
        .then(teams => {
            const tbody = document.querySelector("#sport-teams tbody");
            tbody.innerHTML = teams.map(t =>
                `<tr><td class="clickable" onclick="openTeam(${t.id})">${t.name}</td></tr>`
            ).join('');
        });
}

function initMatchPage(){
    const params = new URLSearchParams(location.search);
    const id = params.get("id");

    if(!id){
        document.body.innerHTML = "<h1>Missing ?id= in URL</h1>";
        return;
    }

    fetch(`/matches/${id}`)
        .then(r => r.json())
        .then(m => {
            document.getElementById("match-title").textContent = m.title;

            // FINISHED
            if (m.status === "finished") {
                const score = `${m.homeScore}:${m.awayScore}`;
                document.getElementById('probability').innerHTML =
                    `<h2>Final score: ${score}</h2>`;
                return;
            }

            // LIVE
            if (m.status === "live") {
                document.getElementById('probability').innerHTML =
                    `<h2>Game in progress ...</h2>`;
                return;
            }

            // SCHEDULED
            if (m.status === "scheduled") {
                const p = m.homeWinProbability;
                if (p == null || p == undefined){
                    document.getElementById('probability').innerHTML =
                        "<h2>No prediction available</h2><p>Not enough historic data</p>";
                } else {
                    const percent = Math.round(p * 100);
                    document.getElementById('probability').innerHTML = `
                          <h2>Home win probability: ${percent}%</h2>
                          <div class="prob-bar">
                            <div class="prob-bar-fill" style="width:${percent}%"></div>
                          </div>
                        `;
                }
                return;

            }
            // fallback (just in case)
            document.getElementById('probability').innerHTML =
                `<h2>No info available for this match</h2>`;
        });


}



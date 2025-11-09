function render(data) {
    const tbody = document.querySelector('#matches tbody');
    tbody.innerHTML = data.map(m => {
        const d = new Date(m.startAt);
        const date = d.toLocaleDateString("de-DE", { timeZone: "Europe/Vienna" });
        const time = d.toLocaleTimeString("de-DE", { hour: "2-digit", minute: "2-digit", timeZone: "Europe/Vienna" });

        return `
        <tr>
          <td>${date}</td>
          <td>${time}</td>
          <td>${m.league.sport.name}</td>
          <td>${m.league.name}</td>
          <td>${m.title}</td>
          <td>${m.venue.name}</td>
        </tr>
      `;
    }).join('');
}

function load(url) {
    fetch(url)
        .then(r => r.json())
        .then(data => render(data));
}

function loadAll() {
    load('/matches');

    ["sport-select","status-select","venue-select","league-select","team-select"]
        .forEach(id => document.getElementById(id).value = "");
}



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
    if(q === "") {
        loadAll();
        return;
    }
    load(`/matches?q=${encodeURIComponent(q)}`);
}




// populate dropdowns
fetch('/sports').then(r => r.json()).then(list => fillSelect('sport-select', list));
fetch('/venues').then(r => r.json()).then(list => fillSelect('venue-select', list));
fetch('/leagues').then(r => r.json()).then(list => fillSelect('league-select', list));
fetch('/teams').then(r => r.json()).then(list => fillSelect('team-select', list));

// init
loadAll();

function fillSelect(id, list) {
    const sel = document.getElementById(id);
    list.forEach(e => {
        const opt = document.createElement('option');
        opt.value = e.id;
        opt.textContent = e.name;
        sel.appendChild(opt);
    });
}

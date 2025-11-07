-- ===== RESET =====
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE matches;
TRUNCATE TABLE team_leagues;
TRUNCATE TABLE teams;
TRUNCATE TABLE leagues;
TRUNCATE TABLE venues;
TRUNCATE TABLE sports;
SET FOREIGN_KEY_CHECKS = 1;

-- ===== SPORTS =====
INSERT INTO sports (name) VALUES
  ('Football'),      
  ('Ice Hockey'),
  ('Basketball');

-- ===== LEAGUES (real world) =====
INSERT INTO leagues (name, country, short_code, sport_id) VALUES
  ('Premier League',            'England',        'EPL',    (SELECT id FROM sports WHERE name='Football')),
  ('Bundesliga',                'Germany',        'BL1',    (SELECT id FROM sports WHERE name='Football')),
  ('NHL',                       'USA/Canada',     'NHL',    (SELECT id FROM sports WHERE name='Ice Hockey')),
  ('ICE Hockey League',         'Austria',        'ICEHL',  (SELECT id FROM sports WHERE name='Ice Hockey')),
  ('NBA',                       'USA',            'NBA',    (SELECT id FROM sports WHERE name='Basketball')),
  ('EuroLeague',                'Europe',         'EL',     (SELECT id FROM sports WHERE name='Basketball'));

-- ===== TEAMS =====
-- Premier League (EPL)
INSERT INTO teams (name, sport_id) VALUES
  ('Liverpool FC', 1),
  ('Arsenal FC', 1),
  ('Manchester City', 1),
  ('Chelsea FC', 1),
  ('Tottenham Hotspur', 1),
  ('Manchester United', 1),
  ('Newcastle United', 1),
  ('Brighton & Hove Albion', 1);

-- Bundesliga (BL1)
INSERT INTO teams (name, sport_id) VALUES
  ('FC Bayern München', 1),
  ('Borussia Dortmund', 1),
  ('RB Leipzig', 1),
  ('Bayer 04 Leverkusen', 1),
  ('VfB Stuttgart', 1),
  ('Eintracht Frankfurt', 1),
  ('Borussia Mönchengladbach', 1),
  ('SC Freiburg', 1);

-- NHL
INSERT INTO teams (name, sport_id) VALUES
  ('Boston Bruins', 2),
  ('New York Rangers', 2),
  ('Toronto Maple Leafs', 2),
  ('Montréal Canadiens', 2),
  ('Detroit Red Wings', 2),
  ('Chicago Blackhawks', 2),
  ('Tampa Bay Lightning', 2),
  ('Pittsburgh Penguins', 2);

-- ICEHL
INSERT INTO teams (name, sport_id) VALUES
  ('EC KAC', 2),
  ('EC Red Bull Salzburg', 2),
  ('HC Bolzano', 2),
  ('Vienna Capitals', 2),
  ('Graz99ers', 2),
  ('Black Wings Linz', 2),
  ('HC Innsbruck', 2),
  ('Fehérvár AV19', 2);

-- NBA
INSERT INTO teams (name, sport_id) VALUES
  ('Boston Celtics', 3),
  ('Los Angeles Lakers', 3),
  ('Golden State Warriors', 3),
  ('Miami Heat', 3),
  ('Milwaukee Bucks', 3),
  ('Dallas Mavericks', 3),
  ('New York Knicks', 3),
  ('Denver Nuggets', 3);

-- EuroLeague (EL)
INSERT INTO teams (name, sport_id) VALUES
  ('Real Madrid Baloncesto', 3),
  ('FC Barcelona', 3),
  ('Anadolu Efes', 3),
  ('Olympiacos Piraeus', 3),
  ('Panathinaikos', 3),
  ('Fenerbahçe', 3),
  ('AS Monaco', 3),
  ('Maccabi Tel Aviv', 3);


-- ===== TEAM ↔ LEAGUE =====
-- EPL
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='EPL' AND t.name IN (
  'Liverpool FC','Arsenal FC','Manchester City','Chelsea FC',
  'Tottenham Hotspur','Manchester United','Newcastle United','Brighton & Hove Albion'
);

-- BL1
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='BL1' AND t.name IN (
  'FC Bayern München','Borussia Dortmund','RB Leipzig','Bayer 04 Leverkusen',
  'VfB Stuttgart','Eintracht Frankfurt','Borussia Mönchengladbach','SC Freiburg'
);

-- NHL
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='NHL' AND t.name IN (
  'Boston Bruins','New York Rangers','Toronto Maple Leafs','Montréal Canadiens',
  'Detroit Red Wings','Chicago Blackhawks','Tampa Bay Lightning','Pittsburgh Penguins'
);

-- ICEHL
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='ICEHL' AND t.name IN (
  'EC KAC','EC Red Bull Salzburg','HC Bolzano','Vienna Capitals',
  'Graz99ers','Black Wings Linz','HC Innsbruck','Fehérvár AV19'
);

-- NBA
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='NBA' AND t.name IN (
  'Boston Celtics','Los Angeles Lakers','Golden State Warriors','Miami Heat',
  'Milwaukee Bucks','Dallas Mavericks','New York Knicks','Denver Nuggets'
);

-- EL
INSERT INTO team_leagues (team_id, league_id)
SELECT t.id, l.id FROM teams t, leagues l
WHERE l.short_code='EL' AND t.name IN (
  'Real Madrid Baloncesto','FC Barcelona','Anadolu Efes','Olympiacos Piraeus',
  'Panathinaikos','Fenerbahçe','AS Monaco','Maccabi Tel Aviv'
);

-- ===== VENUES =====
INSERT INTO venues (name, city, capacity) VALUES
  -- EPL
  ('Anfield', 'Liverpool', 54074),
  ('Emirates Stadium', 'London', 60704),
  ('Etihad Stadium', 'Manchester', 53400),
  ('Stamford Bridge', 'London', 40343),
  ('Tottenham Hotspur Stadium', 'London', 62850),
  ('Old Trafford', 'Manchester', 74310),
  ('St James'' Park', 'Newcastle upon Tyne', 52305),
  ('Amex Stadium', 'Brighton', 31800),

  -- BL1
  ('Allianz Arena', 'Munich', 75000),
  ('Signal Iduna Park', 'Dortmund', 81365),
  ('Red Bull Arena Leipzig', 'Leipzig', 47130),
  ('BayArena', 'Leverkusen', 30210),
  ('Mercedes-Benz Arena (Stuttgart)', 'Stuttgart', 60441),
  ('Deutsche Bank Park', 'Frankfurt', 51500),
  ('Borussia-Park', 'Mönchengladbach', 54057),
  ('Europa-Park Stadion', 'Freiburg', 34700),

  -- NHL (Achtung: einige Arenen doppelt genutzt mit NBA, ist realistisch)
  ('TD Garden', 'Boston', 19580),
  ('Madison Square Garden', 'New York', 19812),
  ('Scotiabank Arena', 'Toronto', 19800),
  ('Bell Centre', 'Montreal', 21273),
  ('Little Caesars Arena', 'Detroit', 19515),
  ('United Center', 'Chicago', 23500),
  ('Amalie Arena', 'Tampa', 19500),
  ('PPG Paints Arena', 'Pittsburgh', 18787),

  -- ICEHL
  ('Stadthalle Klagenfurt', 'Klagenfurt', 5000),
  ('Eisarena Salzburg', 'Salzburg', 3400),
  ('PalaOnda', 'Bolzano', 7200),
  ('Steffl Arena', 'Vienna', 7200),
  ('Merkur Eisstadion', 'Graz', 4000),
  ('Linz AG Eisarena', 'Linz', 4500),
  ('Tiroler Wasserkraft Arena', 'Innsbruck', 3500),
  ('Ifjabb Ocskay Gábor Jégcsarnok', 'Székesfehérvár', 3500),

  -- NBA
  ('Crypto.com Arena', 'Los Angeles', 19068),
  ('Chase Center', 'San Francisco', 18064),
  ('Kaseya Center', 'Miami', 19600),
  ('Fiserv Forum', 'Milwaukee', 17500),
  ('American Airlines Center', 'Dallas', 19200),
  ('Ball Arena', 'Denver', 19520),

  -- EuroLeague
  ('WiZink Center', 'Madrid', 17300),
  ('Palau Blaugrana', 'Barcelona', 7500),
  ('Sinan Erdem Dome', 'Istanbul', 16000),
  ('Peace and Friendship Stadium', 'Piraeus', 12000),
  ('OAKA Arena', 'Athens', 19000),
  ('Ülker Sports Arena', 'Istanbul', 13000),
  ('Salle Gaston Médecin', 'Monaco', 4500),
  ('Menora Mivtachim Arena', 'Tel Aviv', 10500);

-- ===== MATCHES =====
-- EPL
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('Liverpool vs Arsenal – Premier League', 
    (SELECT id FROM teams WHERE name='Liverpool FC'),
    (SELECT id FROM teams WHERE name='Arsenal FC'),
    (SELECT id FROM venues WHERE name='Anfield'),
    (SELECT id FROM leagues WHERE short_code='EPL'),
    '2025-08-16 17:30:00', 2, 1, 'finished'),
  ('Manchester City vs Chelsea – Premier League', 
    (SELECT id FROM teams WHERE name='Manchester City'),
    (SELECT id FROM teams WHERE name='Chelsea FC'),
    (SELECT id FROM venues WHERE name='Etihad Stadium'),
    (SELECT id FROM leagues WHERE short_code='EPL'),
    '2025-11-09 16:00:00', NULL, NULL, 'scheduled'),
  ('Tottenham vs Manchester United – Premier League',
    (SELECT id FROM teams WHERE name='Tottenham Hotspur'),
    (SELECT id FROM teams WHERE name='Manchester United'),
    (SELECT id FROM venues WHERE name='Tottenham Hotspur Stadium'),
    (SELECT id FROM leagues WHERE short_code='EPL'),
    '2025-11-07 19:45:00', NULL, NULL, 'live'),
  ('Newcastle vs Brighton – Premier League',
    (SELECT id FROM teams WHERE name='Newcastle United'),
    (SELECT id FROM teams WHERE name='Brighton & Hove Albion'),
    (SELECT id FROM venues WHERE name='St James'' Park'),
    (SELECT id FROM leagues WHERE short_code='EPL'),
    '2025-10-26 15:00:00', 1, 1, 'finished');

-- BL1
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('Bayern vs Dortmund – Bundesliga',
    (SELECT id FROM teams WHERE name='FC Bayern München'),
    (SELECT id FROM teams WHERE name='Borussia Dortmund'),
    (SELECT id FROM venues WHERE name='Allianz Arena'),
    (SELECT id FROM leagues WHERE short_code='BL1'),
    '2025-11-02 18:30:00', 3, 1, 'finished'),
  ('Leipzig vs Leverkusen – Bundesliga',
    (SELECT id FROM teams WHERE name='RB Leipzig'),
    (SELECT id FROM teams WHERE name='Bayer 04 Leverkusen'),
    (SELECT id FROM venues WHERE name='Red Bull Arena Leipzig'),
    (SELECT id FROM leagues WHERE short_code='BL1'),
    '2025-11-09 17:30:00', NULL, NULL, 'scheduled'),
  ('Stuttgart vs Frankfurt – Bundesliga',
    (SELECT id FROM teams WHERE name='VfB Stuttgart'),
    (SELECT id FROM teams WHERE name='Eintracht Frankfurt'),
    (SELECT id FROM venues WHERE name='Mercedes-Benz Arena (Stuttgart)'),
    (SELECT id FROM leagues WHERE short_code='BL1'),
    '2025-11-07 20:30:00', NULL, NULL, 'live'),
  ('Mönchengladbach vs Freiburg – Bundesliga',
    (SELECT id FROM teams WHERE name='Borussia Mönchengladbach'),
    (SELECT id FROM teams WHERE name='SC Freiburg'),
    (SELECT id FROM venues WHERE name='Borussia-Park'),
    (SELECT id FROM leagues WHERE short_code='BL1'),
    '2025-10-19 15:30:00', 0, 2, 'finished');

-- NHL
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('Bruins vs Rangers – NHL Regular Season',
    (SELECT id FROM teams WHERE name='Boston Bruins'),
    (SELECT id FROM teams WHERE name='New York Rangers'),
    (SELECT id FROM venues WHERE name='TD Garden'),
    (SELECT id FROM leagues WHERE short_code='NHL'),
    '2025-10-12 19:00:00', 3, 2, 'finished'),
  ('Maple Leafs vs Canadiens – NHL Regular Season',
    (SELECT id FROM teams WHERE name='Toronto Maple Leafs'),
    (SELECT id FROM teams WHERE name='Montréal Canadiens'),
    (SELECT id FROM venues WHERE name='Scotiabank Arena'),
    (SELECT id FROM leagues WHERE short_code='NHL'),
    '2025-11-08 01:00:00', NULL, NULL, 'scheduled'),
  ('Red Wings vs Blackhawks – NHL Regular Season',
    (SELECT id FROM teams WHERE name='Detroit Red Wings'),
    (SELECT id FROM teams WHERE name='Chicago Blackhawks'),
    (SELECT id FROM venues WHERE name='Little Caesars Arena'),
    (SELECT id FROM leagues WHERE short_code='NHL'),
    '2025-11-07 01:30:00', NULL, NULL, 'live'),
  ('Lightning vs Penguins – NHL Regular Season',
    (SELECT id FROM teams WHERE name='Tampa Bay Lightning'),
    (SELECT id FROM teams WHERE name='Pittsburgh Penguins'),
    (SELECT id FROM venues WHERE name='Amalie Arena'),
    (SELECT id FROM leagues WHERE short_code='NHL'),
    '2025-10-25 23:00:00', 2, 4, 'finished');

-- ICEHL
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('EC KAC vs EC Red Bull Salzburg – ICEHL',
    (SELECT id FROM teams WHERE name='EC KAC'),
    (SELECT id FROM teams WHERE name='EC Red Bull Salzburg'),
    (SELECT id FROM venues WHERE name='Stadthalle Klagenfurt'),
    (SELECT id FROM leagues WHERE short_code='ICEHL'),
    '2025-09-28 19:15:00', 2, 2, 'finished'),
  ('HC Bolzano vs Vienna Capitals – ICEHL',
    (SELECT id FROM teams WHERE name='HC Bolzano'),
    (SELECT id FROM teams WHERE name='Vienna Capitals'),
    (SELECT id FROM venues WHERE name='PalaOnda'),
    (SELECT id FROM leagues WHERE short_code='ICEHL'),
    '2025-11-10 19:15:00', NULL, NULL, 'scheduled'),
  ('Graz99ers vs Black Wings Linz – ICEHL',
    (SELECT id FROM teams WHERE name='Graz99ers'),
    (SELECT id FROM teams WHERE name='Black Wings Linz'),
    (SELECT id FROM venues WHERE name='Merkur Eisstadion'),
    (SELECT id FROM leagues WHERE short_code='ICEHL'),
    '2025-11-07 19:15:00', NULL, NULL, 'live'),
  ('HC Innsbruck vs Fehérvár AV19 – ICEHL',
    (SELECT id FROM teams WHERE name='HC Innsbruck'),
    (SELECT id FROM teams WHERE name='Fehérvár AV19'),
    (SELECT id FROM venues WHERE name='Tiroler Wasserkraft Arena'),
    (SELECT id FROM leagues WHERE short_code='ICEHL'),
    '2025-10-14 19:15:00', 1, 3, 'finished');

-- NBA
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('Celtics vs Lakers – NBA Regular Season',
    (SELECT id FROM teams WHERE name='Boston Celtics'),
    (SELECT id FROM teams WHERE name='Los Angeles Lakers'),
    (SELECT id FROM venues WHERE name='TD Garden'),
    (SELECT id FROM leagues WHERE short_code='NBA'),
    '2025-10-05 19:30:00', 112, 105, 'finished'),
  ('Warriors vs Heat – NBA Regular Season',
    (SELECT id FROM teams WHERE name='Golden State Warriors'),
    (SELECT id FROM teams WHERE name='Miami Heat'),
    (SELECT id FROM venues WHERE name='Chase Center'),
    (SELECT id FROM leagues WHERE short_code='NBA'),
    '2025-11-09 03:30:00', NULL, NULL, 'scheduled'),
  ('Bucks vs Mavericks – NBA Regular Season',
    (SELECT id FROM teams WHERE name='Milwaukee Bucks'),
    (SELECT id FROM teams WHERE name='Dallas Mavericks'),
    (SELECT id FROM venues WHERE name='Fiserv Forum'),
    (SELECT id FROM leagues WHERE short_code='NBA'),
    '2025-11-07 02:00:00', NULL, NULL, 'live'),
  ('Knicks vs Nuggets – NBA Regular Season',
    (SELECT id FROM teams WHERE name='New York Knicks'),
    (SELECT id FROM teams WHERE name='Denver Nuggets'),
    (SELECT id FROM venues WHERE name='Madison Square Garden'),
    (SELECT id FROM leagues WHERE short_code='NBA'),
    '2025-10-22 23:00:00', 104, 108, 'finished');

-- EuroLeague
INSERT INTO matches (title, home_team_id, away_team_id, venue_id, league_id, start_at, home_score, away_score, status) VALUES
  ('Real Madrid vs FC Barcelona – EuroLeague',
    (SELECT id FROM teams WHERE name='Real Madrid Baloncesto'),
    (SELECT id FROM teams WHERE name='FC Barcelona'),
    (SELECT id FROM venues WHERE name='WiZink Center'),
    (SELECT id FROM leagues WHERE short_code='EL'),
    '2025-11-06 20:30:00', 87, 82, 'finished'),
  ('Anadolu Efes vs Olympiacos – EuroLeague',
    (SELECT id FROM teams WHERE name='Anadolu Efes'),
    (SELECT id FROM teams WHERE name='Olympiacos Piraeus'),
    (SELECT id FROM venues WHERE name='Sinan Erdem Dome'),
    (SELECT id FROM leagues WHERE short_code='EL'),
    '2025-11-13 19:30:00', NULL, NULL, 'scheduled'),
  ('Panathinaikos vs Fenerbahçe – EuroLeague',
    (SELECT id FROM teams WHERE name='Panathinaikos'),
    (SELECT id FROM teams WHERE name='Fenerbahçe'),
    (SELECT id FROM venues WHERE name='OAKA Arena'),
    (SELECT id FROM leagues WHERE short_code='EL'),
    '2025-11-07 20:00:00', NULL, NULL, 'live'),
  ('AS Monaco vs Maccabi Tel Aviv – EuroLeague',
    (SELECT id FROM teams WHERE name='AS Monaco'),
    (SELECT id FROM teams WHERE name='Maccabi Tel Aviv'),
    (SELECT id FROM venues WHERE name='Salle Gaston Médecin'),
    (SELECT id FROM leagues WHERE short_code='EL'),
    '2025-10-17 20:00:00', 78, 81, 'finished');

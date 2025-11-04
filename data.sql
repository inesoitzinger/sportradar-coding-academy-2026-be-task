-- ===== RESET =====
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE matches;
TRUNCATE TABLE teams;
TRUNCATE TABLE venues;
TRUNCATE TABLE sports;
SET FOREIGN_KEY_CHECKS = 1;

-- ===== SPORTS =====
INSERT INTO sports (name) VALUES
  ('Football'),      -- id=1
  ('Ice Hockey'),    -- id=2
  ('Basketball');    -- id=3

-- ===== VENUES =====
INSERT INTO venues (name, city, capacity) VALUES
  ('Anfield', 'Liverpool', 54074),
  ('Allianz Arena', 'Munich', 75000),
  ('Red Bull Arena Salzburg', 'Salzburg', 30188),
  ('Emirates Stadium', 'London', 60704),
  ('TD Garden', 'Boston', 19580),
  ('Klagenfurt Arena', 'Klagenfurt', 11500),
  ('Wiener Stadthalle', 'Vienna', 16000);

-- ===== TEAMS =====
-- Football (id=1)
INSERT INTO teams (name, sport_id) VALUES
  ('Liverpool FC', 1),
  ('Arsenal FC', 1),
  ('FC Bayern München', 1),
  ('FC Red Bull Salzburg', 1);

-- Ice Hockey (id=2)
INSERT INTO teams (name, sport_id) VALUES
  ('Boston Bruins', 2),
  ('New York Rangers', 2),
  ('EC KAC Klagenfurt', 2),
  ('EC Red Bull Salzburg', 2);

-- Basketball (id=3)
INSERT INTO teams (name, sport_id) VALUES
  ('Boston Celtics', 3),
  ('Los Angeles Lakers', 3),
  ('ALBA Berlin', 3),
  ('Vienna D.C. Timberwolves', 3);

-- ===== MATCHES =====
INSERT INTO matches (home_team_id, away_team_id, venue_id, start_at, home_score, away_score, status) VALUES
  (1, 2, 1, '2025-08-12 20:00:00', 2, 1, 'finished'),      
  (3, 1, 2, '2025-07-30 20:30:00', NULL, NULL, 'scheduled'),
  (4, 3, 3, '2025-07-18 18:30:00', NULL, NULL, 'scheduled'),
  (5, 6, 5, '2025-10-12 19:00:00', 3, 2, 'finished'),
  (7, 8, 6, '2025-10-23 19:45:00', NULL, NULL, 'scheduled'),
  (9, 10, 5, '2025-10-05 19:30:00', 112, 105, 'finished'),
  (11, 12, 7, '2025-11-20 18:30:00', NULL, NULL, 'scheduled');

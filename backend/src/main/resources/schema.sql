SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS team_leagues;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS venues;
DROP TABLE IF EXISTS leagues;
DROP TABLE IF EXISTS sports;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE sports (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE venues (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(255),
  capacity INTEGER
);

CREATE TABLE leagues (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  country VARCHAR(255),
  short_code VARCHAR(50) NOT NULL,
  sport_id INTEGER NOT NULL,
  FOREIGN KEY (sport_id) REFERENCES sports (id)
);

CREATE TABLE teams (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  sport_id INTEGER NOT NULL,
  FOREIGN KEY (sport_id) REFERENCES sports (id)
);

CREATE TABLE team_leagues (
  team_id INTEGER NOT NULL,
  league_id INTEGER NOT NULL,
  PRIMARY KEY (team_id, league_id),
  FOREIGN KEY (team_id) REFERENCES teams (id),
  FOREIGN KEY (league_id) REFERENCES leagues (id)
);

CREATE TABLE matches (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  home_team_id INTEGER NOT NULL,
  away_team_id INTEGER NOT NULL,
  venue_id INTEGER NOT NULL,
  league_id INTEGER NOT NULL,
  start_at TIMESTAMP NOT NULL,
  home_score INTEGER,
  away_score INTEGER,
  status ENUM('scheduled','live','finished') NOT NULL,
  FOREIGN KEY (home_team_id) REFERENCES teams (id),
  FOREIGN KEY (away_team_id) REFERENCES teams (id),
  FOREIGN KEY (venue_id) REFERENCES venues (id),
  FOREIGN KEY (league_id) REFERENCES leagues (id)
);

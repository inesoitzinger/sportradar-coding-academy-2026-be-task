CREATE TABLE `sports` (
  `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `venues` (
  `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `city` VARCHAR(255),
  `capacity` INTEGER
);

CREATE TABLE `teams` (
  `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `sport_id` INTEGER NOT NULL,
  FOREIGN KEY (`sport_id`) REFERENCES `sports` (`id`)
);

CREATE TABLE `matches` (
  `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `home_team_id` INTEGER NOT NULL,
  `away_team_id` INTEGER NOT NULL,
  `venue_id` INTEGER NOT NULL,
  `start_at` DATETIME NOT NULL,
  `home_score` INTEGER,
  `away_score` INTEGER,
  `status` VARCHAR(255),
  FOREIGN KEY (`home_team_id`) REFERENCES `teams` (`id`),
  FOREIGN KEY (`away_team_id`) REFERENCES `teams` (`id`),
  FOREIGN KEY (`venue_id`) REFERENCES `venues` (`id`)
);

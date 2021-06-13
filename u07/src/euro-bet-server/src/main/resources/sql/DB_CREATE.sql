SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema betdb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS betdb;

-- -----------------------------------------------------
-- Schema betdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS betdb DEFAULT CHARACTER SET utf8;
USE betdb;

-- -----------------------------------------------------
-- Table `betdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS betdb.`users`
(
    `id`        INT           NOT NULL AUTO_INCREMENT,
    `firstname` VARCHAR(1000) NOT NULL,
    `lastname`  VARCHAR(1000) NOT NULL,
    `username`  VARCHAR(1000) NOT NULL,
    `password`  VARCHAR(255)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `betdb`.`teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS betdb.`teams`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `betdb`.`games`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS betdb.`games`
(
    `id`          INT           NOT NULL AUTO_INCREMENT,
    `team1`       INT           NOT NULL,
    `team2`       INT           NOT NULL,
    `score_team1` INT           NOT NULL DEFAULT 0,
    `score_team2` INT           NOT NULL DEFAULT 0,
    `start_time`  DATETIME      NOT NULL,
    `venue`       VARCHAR(1000) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_games_teams_idx` (`team1` ASC) VISIBLE,
    INDEX `fk_games_teams1_idx` (`team2` ASC) VISIBLE,
    CONSTRAINT `fk_games_teams`
        FOREIGN KEY (`team1`)
            REFERENCES betdb.`teams` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_games_teams1`
        FOREIGN KEY (`team2`)
            REFERENCES betdb.`teams` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `betdb`.`bets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS betdb.`bets`
(
    `id`          INT         NOT NULL AUTO_INCREMENT,
    `user`        INT         NOT NULL,
    `game`        INT         NOT NULL,
    `winner_team` INT         NOT NULL,
    `placed`      VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_bets_users1_idx` (`user` ASC) VISIBLE,
    INDEX `fk_bets_games1_idx` (`game` ASC) VISIBLE,
    INDEX `fk_bets_teams1_idx` (`winner_team` ASC) VISIBLE,
    CONSTRAINT `fk_bets_users1`
        FOREIGN KEY (`user`)
            REFERENCES betdb.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_bets_games1`
        FOREIGN KEY (`game`)
            REFERENCES betdb.`games` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_bets_teams1`
        FOREIGN KEY (`winner_team`)
            REFERENCES betdb.`teams` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

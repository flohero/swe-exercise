-- User
INSERT INTO users (firstname, lastname, username, password)
    VALUE ('Florian', 'Weingartshofer', 'flo',
           '65536:eece4a429e7e9ef0009015291b3642b1:aace0da6b079c3e300ebe0a606e9ce7c');
INSERT INTO users (firstname, lastname, username, password)
    VALUE ('Max', 'Mustermann', 'max', '65536:c4a86ae50a296e82e00881a666c52cd7:85ec6e3eace9687cc1ea2e8aa3f31f6c');
INSERT INTO users (firstname, lastname, username, password)
    VALUE ('Dr', 'Acula', 'dracula', '65536:a2a40c48e3c3f5f6134797edf80e34ed:8da76da40272b2eee0d01808d4751bae');

-- Team
INSERT INTO teams (name) VALUE ('Rapid');
INSERT INTO teams (name) VALUE ('Austria Wien');
INSERT INTO teams (name) VALUE ('Admira Wacker');
INSERT INTO teams (name) VALUE ('Redbull Salzburg');
INSERT INTO teams (name) VALUE ('Bayern München');
INSERT INTO teams (name) VALUE ('Real Madrid');

-- Game
INSERT INTO games (team1, team2, score_team1, score_team2, start_time, venue) VALUE (1, 2, 3, 1, '2021-06-12 15:04:50.77819', 'Linz');
INSERT INTO games (team1, team2, score_team1, score_team2, start_time, venue) VALUE (3, 4, 0, 0, '2021-06-15 15:04:51.159174', 'Wien');
INSERT INTO games (team1, team2, score_team1, score_team2, start_time, venue) VALUE (5, 6, 0, 0, '2021-06-18 15:04:51.508258', 'München');
INSERT INTO games (team1, team2, score_team1, score_team2, start_time, venue) VALUE (1, 6, 0, 10, '2021-06-07 15:04:52.859133', 'Paris');

-- Bet
INSERT INTO bets (user, game, winner_team, placed) VALUE (1, 1, 1, 'BEFORE');
INSERT INTO bets (user, game, winner_team, placed) VALUE (2, 1, 1, 'DURING');
INSERT INTO bets (user, game, winner_team, placed) VALUE (3, 1, 2, 'DURING');
INSERT INTO bets (user, game, winner_team, placed) VALUE (1, 4, 6, 'BEFORE');
INSERT INTO bets (user, game, winner_team, placed) VALUE (2, 4, 6, 'DURING');
INSERT INTO bets (user, game, winner_team, placed) VALUE (3, 4, 6, 'BEFORE');

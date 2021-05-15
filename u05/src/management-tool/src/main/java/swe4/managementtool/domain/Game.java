package swe4.managementtool.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Game {

    private static final int GAME_LENGTH = 90;
    private final UUID id;
    private Team team1;
    private Team team2;
    private int scoreTeam1;
    private int scoreTeam2;
    private LocalDateTime startTime;

    public Game(Team team1, Team team2, int scoreTeam1, int scoreTeam2, LocalDateTime startTime) {
        id = UUID.randomUUID();
        this.team1 = team1;
        this.team2 = team2;
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.startTime = startTime;
    }

    public UUID getId() {
        return id;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEstimatedEndTime() {
        return startTime.plusMinutes(GAME_LENGTH);
    }

    @Override
    public String toString() {
        return team1 + " VS. " + team2;
    }
}

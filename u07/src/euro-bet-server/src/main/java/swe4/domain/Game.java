package swe4.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Game implements Serializable {

    private static final int GAME_LENGTH = 90 + 15;
    private int id;
    private Team team1;
    private Team team2;
    private int scoreTeam1;
    private int scoreTeam2;
    private LocalDateTime startTime;
    private String venue;

    public Game(int id,
                Team team1,
                Team team2,
                int scoreTeam1,
                int scoreTeam2,
                LocalDateTime startTime,
                String venue
    ) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.startTime = startTime;
        this.venue = venue;
    }

    public Game(Team team1, Team team2, int scoreTeam1, int scoreTeam2, LocalDateTime startTime, String venue) {
        this.team1 = team1;
        this.team2 = team2;
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.startTime = startTime;
        this.venue = venue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEstimatedEndTime() {
        return calculateEstimatedEndTime(this.startTime);
    }

    public static LocalDateTime calculateEstimatedEndTime(final LocalDateTime startTime) {
        return startTime.plusMinutes(GAME_LENGTH);
    }

    public String getVenue() {
        return venue;
    }

    public String getStatus(LocalDateTime time) {
        if (this.startTime.isAfter(time)) {
            return "Upcoming";
        }
        if (this.getEstimatedEndTime().isBefore(time)) {
            return "Finished";
        }
        return "LIVE";
    }

    public String getGameName() {
        return toString();
    }

    public String getScore() {
        return scoreTeam1 + " : " + scoreTeam2;
    }

    @Override
    public String toString() {
        return team1 + " VS. " + team2;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && this.id == (((Game) obj).id);
    }
}

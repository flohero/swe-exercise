package swe4.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Bet implements Serializable {
    private static final float POINTS = 10;
    private static final float POINT_MULTIPLICATION = 0.5f;
    private int id;
    private final User user;
    private final Game game;
    private Team winner;
    private PlacementTime placed;

    public Bet(int id, User user, Game game, Team winner, PlacementTime placed) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.winner = winner;
        this.placed = placed;
    }

    public Bet(User user, Game game, Team winner, PlacementTime placed) {
        this.user = user;
        this.game = game;
        this.winner = winner;
        this.placed = placed;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void setPlaced(PlacementTime placed) {
        this.placed = placed;
    }

    public PlacementTime getPlaced() {
        return placed;
    }

    public float getPoints() {
        return POINTS * (placed == PlacementTime.BEFORE ? 1 : POINT_MULTIPLICATION);
    }

    public boolean wasSuccessful() {
        if(game.getEstimatedEndTime().isAfter(LocalDateTime.now())) {
            return false;
        }
        if (game.getTeam1().equals(winner)) {
            return game.getScoreTeam1() > game.getScoreTeam2();
        } else {
            return game.getScoreTeam2() > game.getScoreTeam1();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bet
                && ((Bet) obj).getUser().equals(this.user)
                && ((Bet) obj).getGame().equals(this.game);
    }

    @Override
    public String toString() {
        return game.toString() + ": " + winner + " " + placed.toString();
    }
}

package swe4.domain;

import java.util.UUID;

public class Bet {
    private final UUID id;
    private final User user;
    private final Game game;
    private Team winner;
    private PlacementTime placed;

    public Bet(User user, Game game, Team winner, PlacementTime placed) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.game = game;
        this.winner = winner;
        this.placed = placed;
    }

    public UUID getId() {
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

    public PlacementTime getPlaced() {
        return placed;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bet
                && ((Bet) obj).getUser().equals(this.user)
                && ((Bet) obj).getGame().equals(this.game);
    }
}

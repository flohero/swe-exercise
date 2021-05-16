package swe4.managementtool.repositories;

import swe4.managementtool.domain.Game;
import swe4.managementtool.domain.Team;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FakeGameRepository implements GameRepository {

    private final List<Game> games = new ArrayList<>();

    FakeGameRepository() {
    }


    @Override
    public void insertGame(Game game) {
        this.games.add(game);
    }

    @Override
    public Collection<Game> findAllGames() {
        return games;
    }

    @Override
    public Collection<Game> findByTeam(final Team team) {
        return games
                .stream()
                .filter(game -> game.getTeam1().equals(team) || game.getTeam2().equals(team))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Game> findByTeamAndGameIsDuringTime(Team team, LocalDateTime dateTime) {
        return games
                .stream()
                .filter(game -> game.getTeam1().equals(team) || game.getTeam2().equals(team))
                .filter(game -> game.getStartTime().isBefore(dateTime) && game.getEstimatedEndTime().isAfter(dateTime))
                .collect(Collectors.toList());
    }


}

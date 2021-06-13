package swe4.server.services;

import swe4.domain.Game;
import swe4.domain.Team;
import swe4.server.repositories.GameRepository;
import swe4.server.repositories.RepositoryFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

public class GameServiceImpl implements GameService{

    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();

    @Override
    public void insertGame(Game game) {
        Objects.requireNonNull(game);
        gameRepository.insertGame(game);
    }

    @Override
    public void updateGame(Game game) {
        Objects.requireNonNull(game);
        gameRepository.updateGame(game);
    }

    @Override
    public void deleteGame(Game game) {
        Objects.requireNonNull(game);
        System.out.println("Deleting Game");
        gameRepository.deleteGame(game);
    }

    @Override
    public Collection<Game> findAllGames() {
        return gameRepository.findAllGames();
    }

    @Override
    public Collection<Game> findByTeam(Team team) {
        Objects.requireNonNull(team);
        return gameRepository.findByTeam(team);
    }

    @Override
    public Collection<Game> findByTeamAndGameIsDuringTime(Team team, LocalDateTime dateTime) {
        Objects.requireNonNull(team);
        Objects.requireNonNull(dateTime);
        return gameRepository.findByTeamAndGameIsDuringTime(team, dateTime);
    }

    @Override
    public Collection<Game> findByTeamAndGameOverlapsTimeFrame(Team team, LocalDateTime startTime, LocalDateTime endTime) {
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);
        return gameRepository.findByTeamAndGameOverlapsTimeFrame(team, startTime, endTime);
    }
}

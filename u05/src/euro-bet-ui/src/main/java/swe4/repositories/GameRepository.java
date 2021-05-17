package swe4.repositories;

import swe4.domain.Game;
import swe4.domain.Team;

import java.time.LocalDateTime;
import java.util.Collection;

public interface GameRepository {

    void insertGame(Game game);

    void updateGame(Game game);

    void deleteGame(Game game);

    Collection<Game> findAllGames();

    Collection<Game> findByTeam(final Team team);

    Collection<Game> findByTeamAndGameIsDuringTime(final Team team, final LocalDateTime dateTime);
}

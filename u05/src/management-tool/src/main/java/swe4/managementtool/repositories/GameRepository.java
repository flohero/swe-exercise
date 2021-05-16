package swe4.managementtool.repositories;

import swe4.managementtool.domain.Game;
import swe4.managementtool.domain.Team;

import java.time.LocalDateTime;
import java.util.Collection;

public interface GameRepository {

    void insertGame(Game game);

    Collection<Game> findAllGames();

    Collection<Game> findByTeam(final Team team);

    Collection<Game> findByTeamAndGameIsDuringTime(final Team team, final LocalDateTime dateTime);
}

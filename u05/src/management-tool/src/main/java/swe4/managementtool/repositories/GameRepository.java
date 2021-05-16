package swe4.managementtool.repositories;

import swe4.managementtool.domain.Game;

import java.util.Collection;

public interface GameRepository {

    void insertGame(Game game);

    Collection<Game> findAllGames();
}

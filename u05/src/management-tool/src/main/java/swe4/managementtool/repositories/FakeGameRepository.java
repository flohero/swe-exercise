package swe4.managementtool.repositories;

import swe4.managementtool.domain.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeGameRepository implements GameRepository {

    private final List<Game> games = new ArrayList<>();

    FakeGameRepository() {}


    @Override
    public Collection<Game> findAllGames() {
        return games;
    }
}

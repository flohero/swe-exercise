package swe4.server.repositories;

import swe4.server.config.DataAccessLayerConfig;

public class RepositoryFactory {

    private static UserRepository userRepository = null;
    private static TeamRepository teamRepository = null;
    private static GameRepository gameRepository = null;
    private static BetRepository betRepository = null;

    /**
     * It should not be possible to create an instance of RepositoryFactory
     */
    private RepositoryFactory() {
        throw new AssertionError("No RepositoryFactory instances for you!");
    }

    public static UserRepository userRepositoryInstance() {
        if (userRepository == null) {
            if (DataAccessLayerConfig.isInMemory()) {
                userRepository = new FakeUserRepository();
            } else {
                userRepository = new SqlUserRepository();
            }
        }
        return userRepository;
    }

    public static TeamRepository teamRepositoryInstance() {
        if (teamRepository == null) {
            if (DataAccessLayerConfig.isInMemory()) {
                teamRepository = new FakeTeamRepository();
            } else {
                teamRepository = new SqlTeamRepository();
            }
        }
        return teamRepository;
    }

    public static GameRepository gameRepositoryInstance() {
        if (gameRepository == null) {
            if (DataAccessLayerConfig.isInMemory()) {
                gameRepository = new FakeGameRepository();
            } else {
                gameRepository = new SqlGameRepository();
            }
        }
        return gameRepository;
    }

    public static BetRepository betRepositoryInstance() {
        if (betRepository == null) {
            betRepository = new FakeBetRepository();
        }
        return betRepository;
    }
}

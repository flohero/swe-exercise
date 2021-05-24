package swe4.server.repositories;

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
        if(userRepository == null) {
            userRepository = new FakeUserRepository();
        }
        return userRepository;
    }

    public static TeamRepository teamRepositoryInstance() {
        if(teamRepository == null) {
            teamRepository = new FakeTeamRepository();
        }
        return teamRepository;
    }

    public static GameRepository gameRepositoryInstance() {
        if(gameRepository == null) {
            gameRepository = new FakeGameRepository();
        }
        return gameRepository;
    }

    public static BetRepository betRepositoryInstance() {
        if(betRepository == null) {
        }
        return betRepository;
    }
}

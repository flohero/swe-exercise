package swe4.managementtool.repositories;

public class RepositoryFactory {

    private static UserRepository userRepository = null;
    private static TeamRepository teamRepository = null;

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

    public static TeamRepository teamsRepositoryInstance() {
        if(teamRepository == null) {
            teamRepository = new FakeTeamRepository();
        }
        return teamRepository;
    }
}

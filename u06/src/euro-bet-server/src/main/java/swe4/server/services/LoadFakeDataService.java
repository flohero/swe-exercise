package swe4.server.services;

import swe4.domain.entities.Bet;
import swe4.domain.entities.Game;
import swe4.domain.entities.PlacementTime;
import swe4.domain.entities.Team;
import swe4.server.repositories.BetRepository;
import swe4.server.repositories.GameRepository;
import swe4.server.repositories.RepositoryFactory;
import swe4.server.repositories.TeamRepository;

import java.time.LocalDateTime;

public class LoadFakeDataService {
    private final UserServiceImpl userService = new UserServiceImpl();
    private final TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();
    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final BetRepository betRepository = RepositoryFactory.betRepositoryInstance();

    public void load() {
        // Users
        userService.insertUser("Florian", "Weingartshofer", "flo", "Password");
        userService.insertUser("Max", "Mustermann", "max", "pass");
        userService.insertUser("Dr", "Acula", "dracula", "word");

        // Teams
        final Team rapid = new Team("Rapid");
        teamRepository.insertTeam(rapid);
        final Team austria_wien = new Team("Austria Wien");
        teamRepository.insertTeam(austria_wien);
        final Team admira_wacker = new Team("Admira Wacker");
        teamRepository.insertTeam(admira_wacker);
        final Team redbull_salzburg = new Team("Redbull Salzburg");
        teamRepository.insertTeam(redbull_salzburg);
        final Team bayern_muenchen = new Team("Bayern München");
        teamRepository.insertTeam(bayern_muenchen);
        final Team real_madrid = new Team("Real Madrid");
        teamRepository.insertTeam(real_madrid);

        // Games
        // Finished
        Game game1 = new Game(rapid, austria_wien, 3, 1, LocalDateTime.now().minusDays(3), "Linz");
        // Live
        Game game2 = new Game(admira_wacker, redbull_salzburg, 0, 0, LocalDateTime.now(), "Wien");
        // Upcoming
        Game game3 = new Game(bayern_muenchen, real_madrid, 0, 0, LocalDateTime.now().plusDays(3), "München");
        // Finished
        Game game4 = new Game(rapid, real_madrid, 0, 10, LocalDateTime.now().minusDays(8), "Paris");

        gameRepository.insertGame(game1);
        gameRepository.insertGame(game2);
        gameRepository.insertGame(game3);
        gameRepository.insertGame(game4);

        // Bets
        Bet bet1 = new Bet(userService.findUserByUsername("flo"), game1, rapid, PlacementTime.BEFORE);
        Bet bet2 = new Bet(userService.findUserByUsername("max"), game1, rapid, PlacementTime.DURING);
        Bet bet3 = new Bet(userService.findUserByUsername("dracula"), game1, austria_wien, PlacementTime.DURING);

        Bet bet4 = new Bet(userService.findUserByUsername("flo"), game4, real_madrid, PlacementTime.BEFORE);
        Bet bet5 = new Bet(userService.findUserByUsername("max"), game4, real_madrid, PlacementTime.DURING);
        Bet bet6 = new Bet(userService.findUserByUsername("dracula"), game4, real_madrid, PlacementTime.BEFORE);

        betRepository.insertBet(bet1);
        betRepository.insertBet(bet2);
        betRepository.insertBet(bet3);
        betRepository.insertBet(bet4);
        betRepository.insertBet(bet5);
        betRepository.insertBet(bet6);
    }
}

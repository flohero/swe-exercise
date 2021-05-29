package swe4.client.services.clients;

import javafx.concurrent.Task;
import swe4.client.services.ServiceFactory;
import swe4.client.utils.DialogUtils;
import swe4.domain.entities.Game;
import swe4.domain.entities.Team;
import swe4.server.services.BetService;
import swe4.server.services.GameService;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class GameClientService extends ClientService {

    private final GameService gameService = ServiceFactory.gameServiceInstance();
    private final BetService betService = ServiceFactory.betServiceInstance();

    public void updateGame(Game game) {
        new Thread(() -> {
            try {
                gameService.updateGame(game);
            } catch (RemoteException e) {
                DialogUtils.showErrorDialog(e);
            }
            dataService.refreshGames();
        }).start();
    }

    public void deleteGame(Game game) {
        new Thread(() -> {
            try {
                betService.deleteBetByGame(game);
                gameService.deleteGame(game);
            } catch (RemoteException e) {
                DialogUtils.showErrorDialog(e);
            }
            dataService.refreshGames();
        }).start();
    }

    public Task<Long> findOverlappingGames(Team team1, Team team2, LocalDateTime startTime, LocalDateTime endTime) {
        Task<Long> countOverlapTask = new Task<>() {
            @Override
            protected Long call() {
                try {
                    return Stream.concat(
                            gameService
                                    .findByTeamAndGameOverlapsTimeFrame(team1, startTime, endTime)
                                    .stream(),
                            gameService
                                    .findByTeamAndGameOverlapsTimeFrame(team2, startTime, endTime)
                                    .stream()
                    ).count();
                } catch (RemoteException e) {
                    DialogUtils.showErrorDialog(e);
                }
                return -1L;
            }
        };
        countOverlapTask.run();
        return countOverlapTask;
    }

    public void insertGame(Game game) {
        new Thread(() -> {
            try {
                gameService.insertGame(game);
            } catch (RemoteException e) {
                DialogUtils.showErrorDialog(e);
            }
        }).start();
    }

}

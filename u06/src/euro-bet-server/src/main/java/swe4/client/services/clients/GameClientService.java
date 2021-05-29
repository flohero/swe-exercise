package swe4.client.services.clients;

import swe4.client.services.ServiceFactory;
import swe4.domain.entities.Game;
import swe4.server.services.GameService;

import java.rmi.RemoteException;

public class GameClientService extends ClientService {

    private final GameService gameService = ServiceFactory.gameServiceInstance();

    public void updateGame(Game game) {
        new Thread(() -> {
            try {
                gameService.updateGame(game);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshGames();
        }).start();
    }

    public void deleteGame(Game game) {
        new Thread(() -> {
            try {
                gameService.deleteGame(game);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshGames();
        });
    }
}

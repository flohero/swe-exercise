package swe4.client.services.clients;

import javafx.concurrent.Task;
import swe4.client.services.ServiceFactory;
import swe4.domain.entities.Bet;
import swe4.domain.entities.Game;
import swe4.domain.entities.User;
import swe4.server.services.BetService;

import java.rmi.RemoteException;

public class BetClientService extends ClientService {

    private final BetService betService = ServiceFactory.betServiceInstance();

    public Task<Bet> findBetByUserAndGame(User user, Game selectedGame) {
        Task<Bet> betTask = new Task<>() {
            @Override
            protected Bet call() {
                try {
                    return betService.findBetByUserAndGame(user, selectedGame);
                } catch (RemoteException e) {
                    return null;
                }
            }
        };

        betTask.run();
        return betTask;
    }

    public void insertBet(Bet bet) {
        new Thread(() -> {
            try {
                betService.insertBet(bet);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshBets();
        }).start();
    }

    public void updateBet(Bet bet) {
        new Thread(() -> {
            try {
                betService.updateBet(bet);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshBets();
        }).start();
    }
}

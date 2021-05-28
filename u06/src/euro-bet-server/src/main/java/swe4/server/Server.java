package swe4.server;

import swe4.server.services.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static UserService userService;
    private static TeamService teamService;
    private static GameService gameService;
    private static BetService betService;

    public static void main(String... args) {
        new LoadFakeDataService().load();
        userService = new UserServiceImpl();
        teamService = new TeamServiceImpl();
        gameService = new GameServiceImpl();
        betService = new BetServiceImpl();

        try {
            UserService userServiceStub = (UserService) UnicastRemoteObject
                    .exportObject(userService, 0);

            TeamService teamServiceStub = (TeamService) UnicastRemoteObject
                    .exportObject(teamService, 0);

            GameService gameServiceStub = (GameService) UnicastRemoteObject
                    .exportObject(gameService, 0);

            BetService betServiceStub = (BetService) UnicastRemoteObject
                    .exportObject(betService, 0);

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("UserService", userServiceStub);
            registry.rebind("TeamService", teamServiceStub);
            registry.rebind("GameService", gameServiceStub);
            registry.rebind("BetService", betServiceStub);

            System.out.println("Server Running...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

package swe4.server;

import swe4.server.config.ServiceConfig;
import swe4.server.services.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final UserService userService = new UserServiceImpl();
    private static final TeamService teamService = new TeamServiceImpl();
    private static final GameService gameService = new GameServiceImpl();
    private static final BetService betService = new BetServiceImpl();

    public static void main(String... args) {
        try {
            UserService userServiceStub = (UserService) UnicastRemoteObject
                    .exportObject(userService, 0);

            TeamService teamServiceStub = (TeamService) UnicastRemoteObject
                    .exportObject(teamService, 0);

            GameService gameServiceStub = (GameService) UnicastRemoteObject
                    .exportObject(gameService, 0);

            BetService betServiceStub = (BetService) UnicastRemoteObject
                    .exportObject(betService, 0);

            Registry registry = LocateRegistry.createRegistry(ServiceConfig.port());

            registry.rebind(ServiceConfig.userServiceName(), userServiceStub);
            registry.rebind(ServiceConfig.teamServiceName(), teamServiceStub);
            registry.rebind(ServiceConfig.gameServiceName(), gameServiceStub);
            registry.rebind(ServiceConfig.betServiceName(), betServiceStub);

            System.out.println("Server Running...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

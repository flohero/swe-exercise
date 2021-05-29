package swe4.client.services;

import swe4.server.config.ServiceConfig;
import swe4.server.services.BetService;
import swe4.server.services.GameService;
import swe4.server.services.TeamService;
import swe4.server.services.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceFactory {

    private static UserService userService = null;
    private static TeamService teamService = null;
    private static GameService gameService = null;
    private static DataService dataService = null;
    private static BetService betService = null;
    private static Registry registry = null;
    private static RefreshService refreshService = null;

    private ServiceFactory() {
        throw new AssertionError("No ServiceFactory Instances for you!");
    }

    public static UserService userServiceInstance() {
        if (userService == null) {
            Registry registry = initializeOrGetRegistry();
            try {
                userService = (UserService) registry.lookup(ServiceConfig.userServiceName());
            } catch (RemoteException | NotBoundException e) {
                handleRemoteException(e);
            }
        }
        return userService;
    }

    public static TeamService teamServiceInstance() {
        if (teamService == null) {
            Registry registry = initializeOrGetRegistry();
            try {
                teamService = (TeamService) registry.lookup(ServiceConfig.teamServiceName());
            } catch (RemoteException | NotBoundException e) {
                handleRemoteException(e);
            }
        }
        return teamService;
    }

    public static GameService gameServiceInstance() {
        if (gameService == null) {
            Registry registry = initializeOrGetRegistry();
            try {
                gameService = (GameService) registry.lookup(ServiceConfig.gameServiceName());
            } catch (RemoteException | NotBoundException e) {
                handleRemoteException(e);
            }
        }
        return gameService;
    }

    public static BetService betServiceInstance() {
        if (betService == null) {
            Registry registry = initializeOrGetRegistry();
            try {
                betService = (BetService) registry.lookup(ServiceConfig.betServiceName());
            } catch (RemoteException | NotBoundException e) {
                handleRemoteException(e);
            }
        }
        return betService;
    }

    public static DataService dataServiceInstance() {
        if(dataService == null) {
            dataService = new DataService();
        }
        return dataService;
    }

    public static void startRefreshService() {
        if(refreshService == null) {
            refreshService = new RefreshService();
        }
        if(!refreshService.isAlive()) {
            refreshService.start();
        }
    }

    private static Registry initializeOrGetRegistry() {
        if (registry == null) {
            try {
                registry = LocateRegistry.getRegistry(ServiceConfig.port());
            } catch (RemoteException e) {
                handleRemoteException(e);
            }
        }
        return registry;
    }

    private static void handleRemoteException(Exception e) {
        throw new RuntimeException(e);
    }
}

package swe4.client.services;

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

    private ServiceFactory() {
        throw new AssertionError("No ServiceFactory Instances for you!");
    }

    public static UserService userServiceInstance() {
        if (userService == null) {
            Registry registry = initializeOrGetRegistry();
            try {
                userService = (UserService) registry.lookup("UserService");
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
                teamService = (TeamService) registry.lookup("TeamService");
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
                gameService = (GameService) registry.lookup("GameService");
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
                betService = (BetService) registry.lookup("BetService");
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

    private static Registry initializeOrGetRegistry() {
        if (registry == null) {
            try {
                registry = LocateRegistry.getRegistry();
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

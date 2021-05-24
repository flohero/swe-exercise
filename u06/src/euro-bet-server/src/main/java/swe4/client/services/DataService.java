package swe4.client.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe4.domain.Game;
import swe4.domain.Team;
import swe4.domain.User;
import swe4.server.services.GameService;
import swe4.server.services.TeamService;
import swe4.server.services.UserService;

import java.rmi.RemoteException;

public class DataService {

    private final TeamService teamService = ServiceFactory.teamServiceInstance();
    private final UserService userService = ServiceFactory.userServiceInstance();
    private final GameService gameService = ServiceFactory.gameServiceInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Game> games = FXCollections.observableArrayList();

    DataService() {
        refresh();
    }

    private void refresh() {
        refreshTeams();
        refreshUsers();
        refreshGames();
    }

    public ObservableList<Team> teams() {
        return teams;
    }

    public ObservableList<User> users() {
        return users;
    }

    public ObservableList<Game> games() {
        return games;
    }

    public void refreshTeams() {
        try {
            teams.setAll(teamService.findAllTeams());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void refreshUsers() {
        try {
            users.setAll(userService.findAllUsers());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void refreshGames() {
        try {
            games.setAll(gameService.findAllGames());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

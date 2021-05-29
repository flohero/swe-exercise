package swe4.client.services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe4.domain.dto.UserScoreDto;
import swe4.domain.entities.Game;
import swe4.domain.entities.Team;
import swe4.domain.entities.User;
import swe4.server.services.BetService;
import swe4.server.services.GameService;
import swe4.server.services.TeamService;
import swe4.server.services.UserService;

import java.rmi.RemoteException;
import java.util.Collection;

public class DataService {

    private final TeamService teamService = ServiceFactory.teamServiceInstance();
    private final UserService userService = ServiceFactory.userServiceInstance();
    private final GameService gameService = ServiceFactory.gameServiceInstance();
    private final BetService betService = ServiceFactory.betServiceInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Game> games = FXCollections.observableArrayList();
    private final ObservableList<UserScoreDto> userScoreDtos = FXCollections.observableArrayList();

    DataService() {
        refresh();
    }

    public synchronized void refresh() {
        refreshTeams();
        refreshUsers();
        refreshGames();
        refreshUserScoreDtos();
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

    public ObservableList<UserScoreDto> userScoreDtos() {
        return userScoreDtos;
    }

    public synchronized void refreshTeams() {
        try {
            final Collection<Team> newTeams = teamService.findAllTeams();
            Platform.runLater(() -> teams.setAll(newTeams));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshUsers() {
        try {
            final Collection<User> newUsers = userService.findAllUsers();
            Platform.runLater(() -> users.setAll(newUsers));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshGames() {
        try {
            final Collection<Game> newGames = gameService.findAllGames();
            Platform.runLater(() -> games.setAll(newGames));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void refreshUserScoreDtos() {
        try {
            final Collection<UserScoreDto> newUserScoreDtos = betService.findAllUsersWithScore();
            Platform.runLater(() -> userScoreDtos.setAll(newUserScoreDtos));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

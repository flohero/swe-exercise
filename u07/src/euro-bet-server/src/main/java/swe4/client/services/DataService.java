package swe4.client.services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe4.client.utils.DialogUtils;
import swe4.domain.Bet;
import swe4.domain.Game;
import swe4.domain.Team;
import swe4.domain.User;
import swe4.dto.UserScore;
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
    private final StateService stateService = StateService.getInstance();

    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Game> games = FXCollections.observableArrayList();
    private final ObservableList<Bet> bets = FXCollections.observableArrayList();
    private final ObservableList<UserScore> userScores = FXCollections.observableArrayList();

    DataService() {
        refresh();
    }

    public synchronized void refresh() {
        refreshTeams();
        refreshUsers();
        refreshGames();
        refreshBets();
        refreshUserScores();
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

    public ObservableList<Bet> bets() {
        return bets;
    }

    public ObservableList<UserScore> userScores() {
        return userScores;
    }

    public synchronized void refreshTeams() {
        try {
            final Collection<Team> newTeams = teamService.findAllTeams();
            Platform.runLater(() -> teams.setAll(newTeams));
        } catch (RemoteException e) {
            DialogUtils.showErrorDialog(e);
        }
    }

    public synchronized void refreshUsers() {
        try {
            final Collection<User> newUsers = userService.findAllUsers();
            Platform.runLater(() -> users.setAll(newUsers));
        } catch (RemoteException e) {
            DialogUtils.showErrorDialog(e);
        }
    }

    public synchronized void refreshGames() {
        try {
            final Collection<Game> newGames = gameService.findAllGames();
            Platform.runLater(() -> games.setAll(newGames));
        } catch (RemoteException e) {
            DialogUtils.showErrorDialog(e);
        }
    }

    public synchronized void refreshBets() {
        try {
            if (stateService.getCurrentUser() != null) {
                final Collection<Bet> newBets = betService.findAllExistingAndPossibleBets(stateService.getCurrentUser());
                Platform.runLater(() -> bets.setAll(newBets));
            }
        } catch (RemoteException e) {
            DialogUtils.showErrorDialog(e);
        }
    }

    public synchronized void refreshUserScores() {
        try {
            final Collection<UserScore> newUserScores = betService.findAllUsersWithScore();
            Platform.runLater(() -> userScores.setAll(newUserScores));
        } catch (RemoteException e) {
            DialogUtils.showErrorDialog(e);
        }
    }
}

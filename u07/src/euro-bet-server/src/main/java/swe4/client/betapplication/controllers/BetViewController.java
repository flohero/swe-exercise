package swe4.client.betapplication.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.client.services.StateService;
import swe4.client.services.clients.BetClientService;
import swe4.client.utils.TableDateCell;
import swe4.domain.*;
import swe4.server.services.BetService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BetViewController implements Initializable {

    @FXML
    private TableView<Bet> gameTable;
    @FXML
    private TableColumn<Bet, String> statusCol;
    @FXML
    private TableColumn<Bet, Game> gameCol;
    @FXML
    private TableColumn<Bet, String> scoreCol;
    @FXML
    private TableColumn<Bet, LocalDateTime> startCol;
    @FXML
    private TableColumn<Bet, LocalDateTime> endCol;
    @FXML
    private TableColumn<Bet, String> venueCol;
    @FXML
    private TableColumn<Bet, Team> tippedWinnerCol;
    @FXML
    private TableColumn<Bet, PlacementTime> placedCol;

    @FXML
    private ComboBox<Team> winnerTeamField;
    @FXML
    private Button placeBetBtn;

    private final BetService betService = ServiceFactory.betServiceInstance();
    private final BetClientService betClientService = ServiceFactory.betClientServiceInstance();
    private final StateService stateService = StateService.getInstance();
    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private final ObservableList<Team> opposingTeams = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(dataService::refreshBets).start();
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGame().getStatus(LocalDateTime.now())));
        gameCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getGame()));
        scoreCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getGame().getScore()));
        startCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getGame().getStartTime()));
        startCol.setCellFactory(cell -> new TableDateCell<>());
        endCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getGame().getEstimatedEndTime()));
        endCol.setCellFactory(cell -> new TableDateCell<>());
        venueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGame().getVenue()));
        tippedWinnerCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getWinner()));
        placedCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPlaced()));

        gameTable.setItems(dataService.bets());

        gameTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::selectionChanged);

        winnerTeamField.setItems(opposingTeams);

        winnerTeamField.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::winnerTeamSelectionChanged);
        gameTable.getSortOrder().add(startCol);
    }

    public void placeBet(ActionEvent actionEvent) {
        final Game selectedGame = gameTable.getSelectionModel().getSelectedItem().getGame();
        final User user = stateService.getCurrentUser();
        final Task<Bet> betByUserAndGameTask = betClientService.findBetByUserAndGame(user, selectedGame);
        betByUserAndGameTask
                .addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                        event -> Platform.runLater(
                                () -> upsertBet(betByUserAndGameTask.getValue())
                        )
                );
    }

    private void upsertBet(Bet bet) {
        final Game selectedGame = gameTable.getSelectionModel().getSelectedItem().getGame();
        final Team selectedTeam = winnerTeamField.getValue();
        final User user = stateService.getCurrentUser();
        PlacementTime placementTime = PlacementTime.BEFORE;
        if (selectedGame.getStartTime().isBefore(LocalDateTime.now())) {
            placementTime = PlacementTime.DURING;
        }
        if (bet == null) {
            final Bet newBet = new Bet(user, selectedGame, selectedTeam, placementTime);
            betClientService.insertBet(newBet);
        } else {
            bet.setWinner(selectedTeam);
            bet.setPlaced(placementTime);
            betClientService.updateBet(bet);
        }
    }

    private void selectionChanged(Observable observable) {
        final Bet selectedBet = gameTable.getSelectionModel().getSelectedItem();
        if (selectedBet != null) {
            final User user = stateService.getCurrentUser();
            final Game selectedGame = selectedBet.getGame();
            opposingTeams.setAll(selectedGame.getTeam1(), selectedGame.getTeam2());
            final Task<Bet> betByUser = betClientService.findBetByUserAndGame(user, selectedGame);
            betByUser.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                final Bet bet = betByUser.getValue();
                Platform.runLater(() -> {
                    if (bet != null) {
                        winnerTeamField.getSelectionModel().select(bet.getWinner());
                        placeBetBtn.setText("Update Bet");
                    } else {
                        winnerTeamField.getSelectionModel().selectFirst();
                        placeBetBtn.setText("Place Bet");
                    }
                });
            });
        }
    }

    private void winnerTeamSelectionChanged(Observable observable) {
        placeBetBtn.setDisable(
                winnerTeamField.getSelectionModel().getSelectedItem() == null
                        || gameTable.getSelectionModel()
                        .getSelectedItem()
                        .getGame()
                        .getEstimatedEndTime()
                        .isBefore(LocalDateTime.now())
        );
    }
}

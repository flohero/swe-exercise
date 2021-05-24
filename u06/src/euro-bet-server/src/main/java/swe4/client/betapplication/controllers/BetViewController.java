package swe4.client.betapplication.controllers;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.domain.*;
import swe4.repositories.BetRepository;
import swe4.repositories.GameRepository;
import swe4.repositories.RepositoryFactory;
import swe4.services.StateService;
import swe4.utils.TableDateCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class BetViewController implements Initializable {


    @FXML
    private TableView<Game> gameTable;
    @FXML
    private TableColumn<Game, String> statusCol;

    @FXML
    private TableColumn<Game, LocalDateTime> startCol;
    @FXML
    private TableColumn<Game, LocalDateTime> endCol;
    @FXML
    private TableColumn<Game, String> tippedWinnerCol;
    @FXML
    private TableColumn<Game, String> placedCol;
    @FXML
    private ComboBox<Team> winnerTeamField;
    @FXML
    private Button placeBetBtn;

    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final BetRepository betRepository = RepositoryFactory.betRepositoryInstance();
    private final StateService stateService = StateService.getInstance();
    private final ObservableList<Game> games = FXCollections.observableArrayList();
    private final ObservableList<Team> opposingTeams = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus(LocalDateTime.now())));
        startCol.setCellFactory(cell -> new TableDateCell<>());
        endCol.setCellFactory(cell -> new TableDateCell<>());
        tippedWinnerCol.setCellValueFactory(this::getBetWinner);
        placedCol.setCellValueFactory(this::getPlacementTime);

        refreshGames();
        gameTable.setItems(games);
        gameTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::selectionChanged);
        winnerTeamField.setItems(opposingTeams);

        winnerTeamField.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::winnerTeamSelectionChanged);
        gameTable.getSortOrder().add(startCol);
    }

    private void refreshGames() {
        games.setAll(gameRepository.findAllGames());
    }

    public void placeBet(ActionEvent actionEvent) {
        final Game selectedGame = gameTable.getSelectionModel().getSelectedItem();
        final Team selectedTeam = winnerTeamField.getValue();
        final User user = stateService.getCurrentUser();
        final Optional<Bet> betByUser = betRepository.findBetByUserAndGame(user, selectedGame);
        PlacementTime placementTime = PlacementTime.BEFORE;
        if (selectedGame.getStartTime().isBefore(LocalDateTime.now())) {
            placementTime = PlacementTime.DURING;
        }
        if (betByUser.isEmpty()) {
            final Bet newBet = new Bet(user, selectedGame, selectedTeam, placementTime);
            betRepository.insertBet(newBet);
        } else {
            final Bet updatedBet = betByUser.get();
            updatedBet.setWinner(selectedTeam);
            updatedBet.setPlaced(placementTime);
            betRepository.updateBet(updatedBet);
        }
        refreshGames();
    }

    private void selectionChanged(Observable observable) {
        final Game selectedGame = gameTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            User user = stateService.getCurrentUser();
            final Optional<Bet> betByUser = betRepository.findBetByUserAndGame(user, selectedGame);
            opposingTeams.setAll(selectedGame.getTeam1(), selectedGame.getTeam2());
            if (betByUser.isPresent()) {
                winnerTeamField.getSelectionModel().select(betByUser.get().getWinner());
                placeBetBtn.setText("Update Bet");
            } else {
                winnerTeamField.getSelectionModel().selectFirst();
                placeBetBtn.setText("Place Bet");
            }
        }
    }

    private void winnerTeamSelectionChanged(Observable observable) {
        placeBetBtn.setDisable(
                winnerTeamField.getSelectionModel().getSelectedItem() == null
                        || gameTable.getSelectionModel()
                        .getSelectedItem()
                        .getEstimatedEndTime()
                        .isBefore(LocalDateTime.now())
        );
    }

    private SimpleStringProperty getBetWinner(TableColumn.CellDataFeatures<Game, String> cell) {
        String name = "";
        Optional<Bet> optBet = betRepository.findBetByUserAndGame(
                stateService.getCurrentUser(),
                cell.getValue());
        if (optBet.isPresent()) {
            name = optBet.get().getWinner().toString();
        }
        return new SimpleStringProperty(name);
    }

    private SimpleStringProperty getPlacementTime(TableColumn.CellDataFeatures<Game, String> cell) {
        String name = "";
        Optional<Bet> optBet = betRepository.findBetByUserAndGame(
                stateService.getCurrentUser(),
                cell.getValue());
        if (optBet.isPresent()) {
            name = optBet.get().getPlaced().name();
        }
        return new SimpleStringProperty(name);
    }
}

package swe4.betapplication.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.domain.Game;
import swe4.domain.Team;
import swe4.repositories.GameRepository;
import swe4.repositories.RepositoryFactory;
import swe4.repositories.TeamRepository;
import swe4.utils.TableDateCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BetViewController implements Initializable {

    @FXML
    private TableView<Game> gameTable;
    @FXML
    private TableColumn<Game, String> statusCol;
    @FXML
    private TableColumn<Game, String> teamCol;
    @FXML
    private TableColumn<Game, String> scoreCol;
    @FXML
    private TableColumn<Game, LocalDateTime> startCol;
    @FXML
    private TableColumn<Game, LocalDateTime> endCol;
    @FXML
    private TableColumn<Game, String> venueCol;
    @FXML
    private ComboBox<Team> winnerTeamField;
    @FXML
    private Button placeBetBtn;

    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();
    private final ObservableList<Game> games = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var teams = teamRepository.findAllTeams().toArray();
        gameRepository.insertGame(new Game((Team) teams[0], (Team) teams[1], 0, 0, LocalDateTime.now(), "Linz"));

        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus(LocalDateTime.now())));
        startCol.setCellFactory(column -> new TableDateCell<>());
        endCol.setCellFactory(column -> new TableDateCell<>());

        games.setAll(gameRepository.findAllGames());
        gameTable.setItems(games);
    }
}

package swe4.managementtool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import swe4.domain.Game;
import swe4.domain.Team;
import swe4.repositories.GameRepository;
import swe4.repositories.RepositoryFactory;
import swe4.repositories.TeamRepository;
import swe4.utils.DialogUtils;
import swe4.utils.TableDateCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    @FXML
    private TableView<Game> gameTableView;
    @FXML
    private TableColumn<Game, String> team1Col;
    @FXML
    private TableColumn<Game, String> team2Col;
    @FXML
    private TableColumn<Game, Integer> team1ScoreCol;
    @FXML
    private TableColumn<Game, Integer> team2ScoreCol;
    @FXML
    private TableColumn<Game, LocalDateTime> startTimeCol;
    @FXML
    private TableColumn<Game, String> venueCol;
    @FXML
    private Button deleteBtn;

    private final ObservableList<Game> games = FXCollections.observableArrayList();
    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var teams = teamRepository.findAllTeams().toArray();
        gameRepository.insertGame(new Game((Team) teams[0], (Team) teams[1], 0, 0, LocalDateTime.now(), "Linz"));

        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));

        team1ScoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreTeam1"));
        team1ScoreCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        team1ScoreCol.setOnEditCommit(event -> {
            final Game game = event.getTableView().getItems().get(event.getTablePosition().getRow());
            game.setScoreTeam1(event.getNewValue() >= 0 ? event.getNewValue() : event.getOldValue());
            gameRepository.updateGame(game);
            refreshGames();
        });

        team2ScoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreTeam2"));
        team2ScoreCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        team2ScoreCol.setOnEditCommit(event -> {
            final Game game = event.getTableView().getItems().get(event.getTablePosition().getRow());
            game.setScoreTeam2(event.getNewValue() >= 0 ? event.getNewValue() : event.getOldValue());
            gameRepository.updateGame(game);
            refreshGames();
        });

        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

        startTimeCol.setCellFactory(column -> new TableDateCell<>());

        refreshGames();
        gameTableView.setItems(games);
        gameTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> deleteBtn.setDisable(newValue == null));
    }

    private void refreshGames() {
        games.setAll(gameRepository.findAllGames());
    }

    public void onAdd(ActionEvent actionEvent) {
        DialogUtils.showDialog("/swe4/managementtool/AddGameDialog.fxml");
        refreshGames();
    }

    public void onDelete(ActionEvent actionEvent) {
        Game game = gameTableView.getSelectionModel().getSelectedItem();
        gameRepository.deleteGame(game);
        refreshGames();
    }
}

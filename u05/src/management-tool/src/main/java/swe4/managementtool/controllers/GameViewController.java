package swe4.managementtool.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.managementtool.data.GameData;
import swe4.managementtool.domain.Game;
import swe4.managementtool.domain.Team;
import swe4.managementtool.repositories.GameRepository;
import swe4.managementtool.repositories.RepositoryFactory;
import swe4.managementtool.repositories.TeamRepository;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameViewController implements Initializable {
    public static final String TIME_FORMAT = "HH:mm";
    @FXML
    private TableView<GameData> gameTableView;
    @FXML
    private TableColumn<GameData, String> team1Col;
    @FXML
    private TableColumn<GameData, String> team2Col;
    @FXML
    private TableColumn<GameData, Integer> team1ScoreCol;
    @FXML
    private TableColumn<GameData, Integer> team2ScoreCol;
    @FXML
    private TableColumn<GameData, String> startTimeCol;
    @FXML
    private TableColumn<GameData, String> startDateCol;

    private final ObservableList<GameData> games = FXCollections.observableArrayList();
    private final ObjectProperty<GameData> currentGame = new SimpleObjectProperty<>();
    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var teams = teamRepository.findAllTeams().toArray();
        gameRepository.insertGame(new Game((Team) teams[0], (Team) teams[1], 0, 0, LocalDateTime.now()));

        team1Col.setCellValueFactory(cellData -> cellData.getValue().team1Property());
        team2Col.setCellValueFactory(cellData -> cellData.getValue().team2Property());
        team1ScoreCol.setCellValueFactory(cellData -> cellData.getValue().scoreTeam1Property().asObject());
        team2ScoreCol.setCellValueFactory(cellData -> cellData.getValue().scoreTeam2Property().asObject());
        startTimeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        startDateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        currentGame.bind(gameTableView.getSelectionModel().selectedItemProperty());

        gameTableView.setItems(games);
        refreshGames();
    }

    private void refreshGames() {
        var convertedGames = gameRepository
                .findAllGames()
                .stream()
                .map(GameViewController::gameToGameData)
                .collect(Collectors.toList());
        games.setAll(convertedGames);
    }

    private static GameData gameToGameData(Game game) {
        return new GameData(
                game.getTeam1().toString(),
                game.getTeam2().toString(),
                game.getScoreTeam1(),
                game.getScoreTeam2(),
                game.getStartTime().toLocalDate().toString(),
                game.getStartTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT))
        );
    }


}

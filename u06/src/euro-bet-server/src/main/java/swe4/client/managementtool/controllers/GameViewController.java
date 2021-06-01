package swe4.client.managementtool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.client.services.clients.GameClientService;
import swe4.client.utils.DialogUtils;
import swe4.client.utils.TableDateCell;
import swe4.domain.Game;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private final GameClientService gameClientService = ServiceFactory.gameClientServiceInstance();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));

        team1ScoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreTeam1"));
        team1ScoreCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        team1ScoreCol.setOnEditCommit(event -> {
            final Game game = event.getTableView().getItems().get(event.getTablePosition().getRow());
            game.setScoreTeam1(event.getNewValue() >= 0 ? event.getNewValue() : event.getOldValue());
            gameClientService.updateGame(game);
        });

        team2ScoreCol.setCellValueFactory(new PropertyValueFactory<>("scoreTeam2"));
        team2ScoreCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        team2ScoreCol.setOnEditCommit(event -> {
            final Game game = event.getTableView().getItems().get(event.getTablePosition().getRow());
            game.setScoreTeam2(event.getNewValue() >= 0 ? event.getNewValue() : event.getOldValue());
            gameClientService.updateGame(game);
        });

        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

        startTimeCol.setCellFactory(cell -> new TableDateCell<>());

        gameTableView.setItems(dataService.games());
        gameTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> deleteBtn.setDisable(newValue == null));
        gameTableView.getSortOrder().add(startTimeCol);
    }

    public void onAdd(ActionEvent actionEvent) {
        ServiceFactory.stopRefreshService();
        DialogUtils.showDialog("/swe4/client/managementtool/AddGameDialog.fxml");
        dataService.refreshGames();
        ServiceFactory.startRefreshService();
    }

    public void onDelete(ActionEvent actionEvent) {
        gameClientService.deleteGame(
                gameTableView.getSelectionModel().getSelectedItem());
    }
}

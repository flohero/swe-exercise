package swe4.client.managementtool.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import swe4.client.dialogs.BaseDialogController;
import swe4.client.services.ServiceFactory;
import swe4.client.services.clients.GameClientService;
import swe4.domain.Game;
import swe4.domain.Team;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddGameDialogController extends BaseDialogController implements Initializable {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private ComboBox<Team> team1Field;
    @FXML
    private ComboBox<Team> team2Field;
    @FXML
    private Spinner<Integer> scoreTeam1Field;
    @FXML
    private Spinner<Integer> scoreTeam2Field;
    @FXML
    private DatePicker gameDateField;
    @FXML
    private TextField gameTimeField;
    @FXML
    private TextField venueField;
    @FXML
    private Text errorMessageField;
    @FXML
    private Button addBtn;


    private final GameClientService gameClientService = ServiceFactory.gameClientServiceInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        team1Field.valueProperty().addListener(this::onChange);
        team2Field.valueProperty().addListener(this::onChange);
        scoreTeam1Field.valueProperty().addListener(this::onChange);
        scoreTeam2Field.valueProperty().addListener(this::onChange);
        gameDateField.valueProperty().addListener(this::onChange);
        gameDateField.setValue(LocalDate.now());

        gameTimeField.textProperty().addListener(this::onChange);
        gameTimeField.setText(LocalTime.now().format(TIME_PATTERN));

        venueField.textProperty().addListener(this::onChange);

        final ObservableList<Team> teams = ServiceFactory.dataServiceInstance().teams();
        team1Field.setItems(teams);
        team2Field.setItems(teams);
    }

    private void onChange(Observable observable) {
        errorMessageField.setText("");

        final boolean validTime = isValidTime(gameTimeField.getText().trim());
        if (validTime) {
            final LocalDateTime startTime = LocalDateTime.of(gameDateField.getValue(),
                    LocalTime.parse(gameTimeField.getText(), TIME_PATTERN));
            final LocalDateTime endTime = Game.calculateEstimatedEndTime(startTime);

            Task<Long> overlappingGamesTask =
                    gameClientService.findOverlappingGames(team1Field.getValue(), team2Field.getValue(),
                            startTime, endTime);

            overlappingGamesTask.addEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING,
                    event -> Platform.runLater(() -> addBtn.setDisable(true)));

            overlappingGamesTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    event -> Platform.runLater(() -> validateInput(overlappingGamesTask.getValue())));
        } else {
            addBtn.setDisable(true);
        }

    }

    private void validateInput(long livegames) {
        boolean isTeamOccupied = false;
        if (livegames > 0) {
            errorMessageField.setText("One Team already plays\nduring this time!");
            isTeamOccupied = true;
        }
        addBtn.setDisable(
                team1Field.getValue() == null
                        || team2Field.getValue() == null
                        || team1Field.getValue().equals(team2Field.getValue())
                        || scoreTeam1Field.getValue() < 0
                        || scoreTeam2Field.getValue() < 0
                        || gameTimeField.getText().trim().isEmpty()
                        || venueField.getText().trim().isEmpty()
                        || isTeamOccupied
        );
    }

    public void onAdd(ActionEvent actionEvent) {
        LocalTime time = LocalTime.parse(gameTimeField.getText(), TIME_PATTERN);
        Game game = new Game(team1Field.getValue(), team2Field.getValue(),
                scoreTeam1Field.getValue(), scoreTeam2Field.getValue(),
                LocalDateTime.of(gameDateField.getValue(), time), venueField.getText());
        gameClientService.insertGame(game);
        close(actionEvent);
    }

    private boolean isValidTime(String timeStr) {
        try {
            LocalTime.parse(timeStr, TIME_PATTERN);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}

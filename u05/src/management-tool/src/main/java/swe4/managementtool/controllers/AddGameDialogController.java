package swe4.managementtool.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import swe4.managementtool.domain.Game;
import swe4.managementtool.domain.Team;
import swe4.managementtool.repositories.GameRepository;
import swe4.managementtool.repositories.RepositoryFactory;
import swe4.managementtool.repositories.TeamRepository;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AddGameDialogController extends BaseDialogController implements Initializable {
    public static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("HH:mm");

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


    private final GameRepository gameRepository = RepositoryFactory.gameRepositoryInstance();
    private final TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();


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
        team1Field.setItems(teams);
        team2Field.setItems(teams);
        teams.setAll(teamRepository.findAllTeams());
    }

    private void onChange(Observable observable) {
        errorMessageField.setText("");

        final boolean validTime = isValidTime(gameTimeField.getText().trim());
        boolean isTeamOccupied = false;
        if (validTime) {
            LocalDateTime dateTime = LocalDateTime.of(gameDateField.getValue(),
                    LocalTime.parse(gameTimeField.getText(), TIME_PATTERN));
            long livegames = Stream.concat(
                    gameRepository
                            .findByTeamAndGameIsDuringTime(team1Field.getValue(), dateTime)
                            .stream(),
                    gameRepository
                            .findByTeamAndGameIsDuringTime(team2Field.getValue(), dateTime)
                            .stream()
            ).count();
            if (livegames > 0) {
                errorMessageField.setText("One Team already plays\nduring this time!");
                isTeamOccupied = true;
            }
        }
        addBtn.setDisable(
                team1Field.getValue() == null
                        || team2Field.getValue() == null
                        || team1Field.getValue().equals(team2Field.getValue())
                        || scoreTeam1Field.getValue() < 0
                        || scoreTeam2Field.getValue() < 0
                        || gameTimeField.getText().trim().isEmpty()
                        || !validTime
                        || venueField.getText().trim().isEmpty()
                        || isTeamOccupied
        );
    }

    public void onAdd(ActionEvent actionEvent) {
        LocalTime time = LocalTime.parse(gameTimeField.getText(), TIME_PATTERN);
        Game game = new Game(team1Field.getValue(), team2Field.getValue(),
                scoreTeam1Field.getValue(), scoreTeam2Field.getValue(),
                LocalDateTime.of(gameDateField.getValue(), time), venueField.getText());
        gameRepository.insertGame(game);
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

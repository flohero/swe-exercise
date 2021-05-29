package swe4.client.betapplication.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.domain.dto.UserScoreDto;
import swe4.domain.entities.User;

import java.net.URL;
import java.util.ResourceBundle;

public class HighscoreViewController implements Initializable {
    @FXML
    private TableView<UserScoreDto> highscoreTable;
    @FXML
    private TableColumn<UserScoreDto, Float> totalScoreCol;

    private final DataService dataService = ServiceFactory.dataServiceInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        highscoreTable.getSortOrder().add(totalScoreCol);
        highscoreTable.setItems(dataService.userScoreDtos());
    }
}

package swe4.client.managementtool.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.domain.entities.Team;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamViewController implements Initializable {
    @FXML
    private TableView<Team> teamTableView;

    @FXML
    private TableColumn<Team, String> teamNameCol;

    private final DataService dataService = ServiceFactory.dataServiceInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teamNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        teamTableView.setItems(dataService.teams());
    }
}

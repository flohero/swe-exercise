package swe4.managementtool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import swe4.managementtool.domain.Team;
import swe4.managementtool.repositories.RepositoryFactory;
import swe4.managementtool.repositories.TeamRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamViewController implements Initializable {
    @FXML
    private TableView<Team> teamTableView;

    @FXML
    private TableColumn<Team, String> teamNameCol;

    private final TeamRepository teamRepository = RepositoryFactory.teamsRepositoryInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teamNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamTableView.getItems().setAll(teamRepository.findAllTeams());
    }
}

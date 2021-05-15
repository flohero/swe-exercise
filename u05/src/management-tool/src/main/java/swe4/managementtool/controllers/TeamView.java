package swe4.managementtool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import swe4.managementtool.domain.Team;
import swe4.managementtool.repositories.TeamsRepository;
import swe4.managementtool.repositories.FakeTeamRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamView implements Initializable {
    @FXML
    private TableView<Team> teamTableView;

    @FXML
    private TableColumn<Team, String> teamName;

    private final TeamsRepository teamsRepository = new FakeTeamRepository();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teamName.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamTableView.getItems().setAll(teamsRepository.findAllTeams());
    }
}

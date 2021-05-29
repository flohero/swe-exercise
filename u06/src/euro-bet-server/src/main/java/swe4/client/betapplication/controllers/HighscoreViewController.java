package swe4.client.betapplication.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.domain.User;
import swe4.server.services.BetService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class HighscoreViewController implements Initializable {
    @FXML
    private TableView<User> highscoreTable;
    @FXML
    private TableColumn<User, Float> totalScoreCol;

    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private final BetService betService = ServiceFactory.betServiceInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        highscoreTable.getSortOrder().add(totalScoreCol);
        totalScoreCol.setCellValueFactory(cell -> {
            try {
                return new SimpleObjectProperty<>((float) betService.totalScorePerUser(cell.getValue()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return new SimpleObjectProperty<>(0.0f);
        });

        highscoreTable.setItems(dataService.users());
    }
}

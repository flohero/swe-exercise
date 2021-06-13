package swe4.client.betapplication.controllers;

import javafx.fxml.Initializable;
import swe4.client.services.ServiceFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class BetDashboardController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceFactory.startRefreshService();
    }
}

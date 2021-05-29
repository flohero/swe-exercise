package swe4.client.betapplication.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import swe4.client.services.ServiceFactory;
import swe4.client.services.StateService;
import swe4.client.services.clients.UserClientService;
import swe4.client.utils.WindowUtils;
import swe4.domain.User;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Text errorMessageField;

    private final UserClientService userClientService = ServiceFactory.userClientServiceInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.textProperty().addListener(this::onChange);
        password.textProperty().addListener(this::onChange);
    }

    private void onChange(Observable observable) {
        loginBtn.setDisable(
                username.getText().trim().isEmpty()
                || password.getText().trim().isEmpty()
        );
    }

    public void onLogin(ActionEvent actionEvent) {
        errorMessageField.setText("");
        Task<User> userIsPresent = userClientService.findUserByUsernameAndPassword(username.getText(), password.getText());
        userIsPresent.run();
        userIsPresent.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> Platform.runLater(() -> {
            User user = userIsPresent.getValue();
            if(user != null) {
                StateService stateService = StateService.getInstance();
                stateService.setCurrentUser(user);
                Stage root = WindowUtils.getWindowRoot(actionEvent);
                final Pane betView = (Pane) WindowUtils.loadFXML("/swe4/client/betapplication/BetDashboard.fxml");
                root.setWidth(betView.getPrefWidth());
                root.setHeight(betView.getPrefHeight());
                root.getScene()
                        .setRoot(betView);
            } else {
                errorMessageField.setText("Username/Password incorrect");
            }
        }));
    }
}

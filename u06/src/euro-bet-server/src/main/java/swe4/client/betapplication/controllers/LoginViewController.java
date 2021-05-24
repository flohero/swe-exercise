package swe4.client.betapplication.controllers;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import swe4.domain.User;
import swe4.services.StateService;
import swe4.services.UserService;
import swe4.utils.WindowUtils;

import java.net.URL;
import java.util.Optional;
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

    private final UserService userService = new UserService();

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
        Optional<User> optUser = userService.getUser(username.getText(), password.getText());
        if(optUser.isPresent()) {
            StateService stateService = StateService.getInstance();
            stateService.setCurrentUser(optUser.get());
            Stage root = WindowUtils.getWindowRoot(actionEvent);
            final Pane betView = (Pane) WindowUtils.loadFXML("/swe4/betapplication/BetDashboard.fxml");
            root.setWidth(betView.getPrefWidth());
            root.setHeight(betView.getPrefHeight());
            root.getScene()
                    .setRoot(betView);
        } else {
            errorMessageField.setText("Username/Password incorrect");
        }
    }
}

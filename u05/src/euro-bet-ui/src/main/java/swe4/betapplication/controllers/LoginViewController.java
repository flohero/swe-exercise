package swe4.betapplication.controllers;

import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import swe4.services.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginBtn;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.textProperty().addListener(this::onChange);
        password.textProperty().addListener(this::onChange);
        userService.insertUser("Florian", "Weingartshofer", "flo", "cool");
    }

    private void onChange(Observable observable) {
        loginBtn.setDisable(
                username.getText().trim().isEmpty()
                || password.getText().trim().isEmpty()
        );
    }

    public void onLogin(ActionEvent actionEvent) {
        boolean userExists = userService.userExists(username.getText(), password.getText());
        if(userExists) {
            System.out.println("User LoggedIn");
        } else {
            System.out.println("User does not exist");
        }
    }
}

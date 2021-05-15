package swe4.managementtool.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import swe4.managementtool.services.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserDialogController implements Initializable {

    private final UserService userService = new UserService();

    @FXML
    private Button addBtn;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> firstnameField.requestFocus());
        firstnameField.textProperty().addListener(this::onChange);
        lastnameField.textProperty().addListener(this::onChange);
        usernameField.textProperty().addListener(this::onChange);
        passwordField.textProperty().addListener(this::onChange);
    }

    public void onAddUser(ActionEvent actionEvent) {
        userService
                .insertUser(
                        firstnameField.getText(),
                        lastnameField.getText(),
                        usernameField.getText(),
                        passwordField.getText()
                );
        closeStage(actionEvent);
    }

    public void onCancel(ActionEvent actionEvent) {
        closeStage(actionEvent);
    }

    private void onChange(Observable observable) {
        addBtn.setDisable(
                firstnameField.getText().trim().isEmpty()
                        || lastnameField.getText().trim().isEmpty()
                        || usernameField.getText().trim().isEmpty()
                        || passwordField.getText().trim().isEmpty()
        );
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

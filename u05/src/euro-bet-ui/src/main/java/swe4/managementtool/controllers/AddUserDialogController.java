package swe4.managementtool.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import swe4.domain.User;
import swe4.services.UserService;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddUserDialogController extends BaseDialogController implements Initializable {

    private final UserService userService = new UserService();

    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessageField;
    @FXML
    private Button addBtn;

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
        close(actionEvent);
    }

    private void onChange(Observable observable) {
        final boolean usernameEmpty = usernameField.getText().trim().isEmpty();
        boolean usernameUnique = true;
        if (!usernameEmpty) {
            if (userService.findUserByUsername(usernameField.getText()).isPresent()) {
                errorMessageField.setText("Username is already in use");
                usernameUnique = false;
            }
        }
        addBtn.setDisable(
                firstnameField.getText().trim().isEmpty()
                        || lastnameField.getText().trim().isEmpty()
                        || usernameEmpty
                        || passwordField.getText().trim().isEmpty()
                        || !usernameUnique
        );
    }


}

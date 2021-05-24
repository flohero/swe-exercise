package swe4.client.managementtool.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import swe4.client.services.ServiceFactory;
import swe4.server.services.UserService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;


public class AddUserDialogController extends BaseDialogController implements Initializable {

    private final UserService userService = ServiceFactory.userServiceInstance();

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
        try {
            userService
                    .insertUser(
                            firstnameField.getText(),
                            lastnameField.getText(),
                            usernameField.getText(),
                            passwordField.getText()
                    );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        close(actionEvent);
    }

    private void onChange(Observable observable) {
        errorMessageField.setText("");
        final boolean usernameEmpty = usernameField.getText().trim().isEmpty();
        boolean usernameUnique = true;
        if (!usernameEmpty) {
            try {
                if (userService.userByUsernameIsPresent(usernameField.getText())) {
                    errorMessageField.setText("Username is already in use");
                    usernameUnique = false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
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

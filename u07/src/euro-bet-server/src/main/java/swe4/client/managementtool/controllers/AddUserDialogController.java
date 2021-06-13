package swe4.client.managementtool.controllers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import swe4.client.dialogs.BaseDialogController;
import swe4.client.services.ServiceFactory;
import swe4.client.services.clients.UserClientService;

import java.net.URL;
import java.util.ResourceBundle;


public class AddUserDialogController extends BaseDialogController implements Initializable {

    private final UserClientService userClientService = ServiceFactory.userClientServiceInstance();

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
        userClientService.insertUser(
                firstnameField.getText(),
                lastnameField.getText(),
                usernameField.getText(),
                passwordField.getText()
        );
        close(actionEvent);
    }

    private void onChange(Observable observable) {
        errorMessageField.setText("");
        final boolean usernameEmpty = usernameField.getText().trim().isEmpty();
        Task<Boolean> userNamePresent = userClientService.userByUsernameIsPresent(usernameField.getText());
        userNamePresent.run();

        final boolean inputInvalid = firstnameField.getText().trim().isEmpty()
                || lastnameField.getText().trim().isEmpty()
                || usernameEmpty
                || passwordField.getText().trim().isEmpty();

        // Disable the add button if the task is still running or the username is already in use
        userNamePresent.addEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING,
                event -> Platform.runLater(() -> addBtn.setDisable(true)));

        userNamePresent.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                event -> {
                    final Boolean isPresent = userNamePresent.getValue();
                    Platform.runLater(() -> {
                        addBtn.setDisable(inputInvalid || isPresent);
                        if (isPresent) {
                            errorMessageField.setText("Username is already in use");
                        } else {
                            errorMessageField.setText("");
                        }
                    });
                });
    }
}

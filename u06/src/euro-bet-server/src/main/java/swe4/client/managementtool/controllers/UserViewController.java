package swe4.client.managementtool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import swe4.client.services.DataService;
import swe4.client.services.ServiceFactory;
import swe4.client.utils.DialogUtils;
import swe4.domain.User;
import swe4.server.services.UserService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UserViewController implements Initializable {

    private final UserService userService = ServiceFactory.userServiceInstance();
    private final DataService dataService = ServiceFactory.dataServiceInstance();

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> firstnameCol;
    @FXML
    private TableColumn<User, String> lastnameCol;
    @FXML
    private TableColumn<User, String> userNameCol;
    @FXML
    private TableColumn<User, String> passwordCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));

        lastnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        firstnameCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            user.setFirstname(value);
            updateAndRefresh(user);
        });

        lastnameCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            user.setLastname(value);
            updateAndRefresh(user);
        });

        userNameCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            user.setUsername(value);
            updateAndRefresh(user);
        });

        passwordCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            try {
                userService.updateUserPassword(user, value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            dataService.refreshUsers();
        });

        userTableView.setItems(dataService.users());
    }

    public void addUser(ActionEvent actionEvent) {
        DialogUtils.showDialog("/swe4/client/managementtool/AddUserDialog.fxml");
        dataService.refreshUsers();
    }

    private void updateAndRefresh(User user) {
        try {
            this.userService.updateUser(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        dataService.refreshUsers();
    }

    private static String getEventValue(TableColumn.CellEditEvent<User, String> event) {
        return emptyString(event.getNewValue()).orElse(event.getOldValue());
    }

    private static User getUserOfEvent(TableColumn.CellEditEvent<User, String> event) {
        return event.getTableView().getItems().get(event.getTablePosition().getRow());
    }

    private static Optional<String> emptyString(String string) {
        return Optional.ofNullable(string).filter(Predicate.not(String::isEmpty));
    }
}

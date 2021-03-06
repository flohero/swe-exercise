package swe4.managementtool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import swe4.domain.User;
import swe4.services.UserService;
import swe4.utils.DialogUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UserViewController implements Initializable {

    private final UserService userService = new UserService();
    private final ObservableList<User> users = FXCollections.observableArrayList();

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
            userService.updateUserPassword(user, value);
            refreshUsers();
        });

        refreshUsers();
        userTableView.setItems(users);
    }

    public void addUser(ActionEvent actionEvent) {
        DialogUtils.showDialog("/swe4/managementtool/AddUserDialog.fxml");
        refreshUsers();
    }

    private void updateAndRefresh(User user) {
        this.userService.updateUser(user);
        refreshUsers();
    }

    private void refreshUsers() {
        users.setAll(userService.findAllUsers());
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

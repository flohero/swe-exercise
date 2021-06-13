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
import swe4.client.services.clients.UserClientService;
import swe4.client.utils.DialogUtils;
import swe4.domain.User;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UserViewController implements Initializable {

    private final DataService dataService = ServiceFactory.dataServiceInstance();
    private final UserClientService userClientService = ServiceFactory.userClientServiceInstance();

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
            userClientService.updateUser(user);
        });

        lastnameCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            user.setLastname(value);
            userClientService.updateUser(user);
        });

        userNameCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            user.setUsername(value);
            userClientService.updateUser(user);
        });

        passwordCol.setOnEditCommit(event -> {
            final String value = getEventValue(event);
            final User user = getUserOfEvent(event);
            userClientService.updateUserPassword(user, value);
        });

        userTableView.setItems(dataService.users());
    }

    public void addUser(ActionEvent actionEvent) {
        DialogUtils.showDialog("/swe4/client/managementtool/AddUserDialog.fxml");
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

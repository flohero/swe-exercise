package swe4.betapplication.controllers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import swe4.domain.Bet;
import swe4.domain.User;
import swe4.repositories.BetRepository;
import swe4.repositories.RepositoryFactory;
import swe4.repositories.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class HighscoreViewController implements Initializable {
    @FXML
    private TableView<User> highscoreTable;
    @FXML
    private TableColumn<User, Float> totalScoreCol;

    private final UserRepository userRepository = RepositoryFactory.userRepositoryInstance();
    private final BetRepository betRepository = RepositoryFactory.betRepositoryInstance();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        highscoreTable.getSortOrder().add(totalScoreCol);
        totalScoreCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(
                        (float) betRepository
                                .findBetsByUser(cell.getValue())
                                .stream()
                                .filter(Bet::wasSuccessful)
                                .mapToDouble(Bet::getPoints)
                                .sum()
                ));

        users.setAll(userRepository.findAllUsers());
        highscoreTable.setItems(users);
    }
}

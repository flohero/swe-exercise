package swe4.managementtool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogUtils {

    private DialogUtils() {
        throw new AssertionError("No DialogUtils instances for you!");
    }

    static void showDialog(String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(DialogUtils.class.getResource(path));
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

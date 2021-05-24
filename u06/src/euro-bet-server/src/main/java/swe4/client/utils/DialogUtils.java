package swe4.client.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogUtils {

    private DialogUtils() {
        throw new AssertionError("No DialogUtils instances for you!");
    }

    public static void showDialog(String path) {
        Parent parent = (Parent) WindowUtils.loadFXML(path);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

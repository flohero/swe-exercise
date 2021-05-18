package swe4.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowUtils {

    private WindowUtils() {
        throw new AssertionError("No WindowUtils instances for you!");
    }


    public static Object loadFXML(String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(DialogUtils.class.getResource(path));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stage getWindowRoot(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }
}

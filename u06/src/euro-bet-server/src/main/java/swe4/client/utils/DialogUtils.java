package swe4.client.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import swe4.client.dialogs.ErrorDialogController;

import java.io.IOException;

public class DialogUtils {

    private static boolean errorDialogHasBeenShown = false;

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

    public static void showErrorDialog(Exception exception) {
        if (!errorDialogHasBeenShown) {
            Platform.runLater(() -> {
                errorDialogHasBeenShown = true;
                FXMLLoader fxmlLoader = new FXMLLoader(
                        WindowUtils.class.getResource("/swe4/client/dialogs/ErrorDialog.fxml"));
                Parent parent;
                try {
                    parent = fxmlLoader.load();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
                ((ErrorDialogController) fxmlLoader.getController()).setError(exception);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            });
        }
    }
}

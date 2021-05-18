package swe4.managementtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import swe4.services.LoadFakeDataService;

import java.io.IOException;
import java.net.URL;

public class ManagementTool extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new LoadFakeDataService().load();
        try {
            FXMLLoader loader = new FXMLLoader();
            final URL resource = ManagementTool.class.getResource("Dashboard.fxml");
            loader.setLocation(resource);
            AnchorPane root = loader.load();

            final Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

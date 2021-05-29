package swe4.client.managementtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import swe4.client.services.RefreshService;
import swe4.client.services.ServiceFactory;

import java.io.IOException;
import java.net.URL;

public class ManagementTool extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            final URL resource = ManagementTool.class.getResource("Dashboard.fxml");
            loader.setLocation(resource);
            AnchorPane root = loader.load();

            final Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
            ServiceFactory.startRefreshService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        ServiceFactory.stopRefreshService();
    }
}

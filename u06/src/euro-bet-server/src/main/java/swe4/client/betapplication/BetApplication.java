package swe4.client.betapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import swe4.client.services.ServiceFactory;

import java.io.IOException;
import java.net.URL;

public class BetApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            final URL resource = BetApplication.class.getResource("/swe4/client/betapplication/LoginView.fxml");
            loader.setLocation(resource);
            VBox root = loader.load();

            final Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        ServiceFactory.stopRefreshService();
    }
}

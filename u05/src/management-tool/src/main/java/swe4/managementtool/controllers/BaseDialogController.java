package swe4.managementtool.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class BaseDialogController {
    public void onClose(ActionEvent actionEvent) {
        close(actionEvent);
    }

    protected void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

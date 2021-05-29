package swe4.client.dialogs;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import swe4.client.utils.WindowUtils;

public abstract class BaseDialogController {
    public void onClose(ActionEvent actionEvent) {
        close(actionEvent);
    }

    protected void close(ActionEvent actionEvent) {
        Stage root = WindowUtils.getWindowRoot(actionEvent);
        root.close();
    }
}

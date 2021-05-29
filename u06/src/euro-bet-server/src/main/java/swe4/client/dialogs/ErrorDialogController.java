package swe4.client.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialogController extends BaseDialogController{
    @FXML
    private TextArea errorArea;

    public void setError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.errorArea.setText(sw.toString());
    }
}

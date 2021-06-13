package swe4.client.utils;

import javafx.scene.control.TableCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TableDateCell<T> extends TableCell<T, LocalDateTime> {
    @Override
    protected void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
    }
}

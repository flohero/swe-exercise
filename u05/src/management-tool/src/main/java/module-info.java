module management.tool.main {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    exports swe4.managementtool;
    opens swe4.managementtool;
    exports swe4.managementtool.controllers;
    opens swe4.managementtool.controllers;
}
module euro.bet.ui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports swe4.managementtool;
    opens swe4.managementtool;
    exports swe4.managementtool.controllers;
    opens swe4.managementtool.controllers;

    exports swe4.betapplication;
    opens swe4.betapplication;
    exports swe4.betapplication.controllers;
    opens swe4.betapplication.controllers;

    exports swe4.domain;
    opens swe4.domain;
    exports swe4.utils;
    opens swe4.utils;
}
module euro.bet.server {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports swe4.client.managementtool;
    opens swe4.client.managementtool;
    exports swe4.client.managementtool.controllers;
    opens swe4.client.managementtool.controllers;

    /*exports swe4.client.betapplication;
    opens swe4.client.betapplication;
    exports swe4.client.betapplication.controllers;
    opens swe4.client.betapplication.controllers;*/

    exports swe4.domain;
    opens swe4.domain;
    exports swe4.client.utils;
    opens swe4.client.utils;

    exports swe4.server.services;
}
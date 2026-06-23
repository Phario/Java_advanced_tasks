module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.web;
    requires java.management;
    requires lib;

    opens pl.pwr.ite.dynak.app to
            javafx.fxml,
            javafx.graphics,
            javafx.controls,
            javafx.web;
}
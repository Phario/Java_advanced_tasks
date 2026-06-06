module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.web;

    opens pl.pwr.ite.dynak.app to
            javafx.fxml,
            javafx.graphics,
            javafx.controls,
            javafx.web;

    requires lib;
    requires java.desktop;
    requires javafx.swing;
}
module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    opens pl.pwr.ite.dynak.app to
            javafx.fxml,
            javafx.graphics,
            javafx.controls;

    requires lib;
    requires java.desktop;
    requires javafx.swing;
}
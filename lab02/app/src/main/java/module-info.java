module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lib;

    opens pl.pwr.ite.dynak.app to javafx.graphics, javafx.fxml, javafx.controls;
}
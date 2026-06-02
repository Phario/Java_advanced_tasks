module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires lib;

    exports pl.pwr.ite.dynak.app;
    opens pl.pwr.ite.dynak.app to javafx.fxml;
}
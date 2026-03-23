module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    exports pl.pwr.ite.dynak.app;
    opens pl.pwr.ite.dynak.app to javafx.fxml;
}
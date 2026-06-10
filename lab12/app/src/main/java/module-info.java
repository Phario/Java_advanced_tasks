module app {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires lombok;
    requires lib;

    opens pl.pwr.ite.dynak.app to javafx.graphics, javafx.fxml;
}
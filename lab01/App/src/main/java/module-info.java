module App {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    opens pl.pwr.ite.dynak.gui to javafx.fxml;
    exports pl.pwr.ite.dynak.gui;
}
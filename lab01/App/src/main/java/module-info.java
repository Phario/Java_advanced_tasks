module App {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires Zipper;
    opens pl.pwr.ite.dynak.gui to javafx.fxml;
    exports pl.pwr.ite.dynak.gui;
}
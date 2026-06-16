module lab13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.scripting;
    requires org.openjdk.nashorn;

    opens pl.pwr.ite.dynak.app to javafx.graphics, javafx.fxml;

}
import ex.api.AnalysisService;

module app {
    requires serviceloader.example;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    opens pl.pwr.ite.dynak.app to
            javafx.fxml,
            javafx.graphics,
            javafx.controls;
    opens pl.pwr.ite.dynak.app.utils to javafx.controls, javafx.fxml, javafx.graphics;

    uses AnalysisService;
}
package pl.pwr.ite.dynak.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class AppController {

    @FXML private Button chooseClassDirectoryButton;

    @FXML private TableView<String> classDirectoryTableView;

    @FXML
    private void initialize() {
        chooseClassDirectoryButton.setOnAction(e -> directoryButtonHandler());
    }

    private void directoryButtonHandler() {

    }
}

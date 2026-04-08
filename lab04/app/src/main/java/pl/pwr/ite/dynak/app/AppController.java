package pl.pwr.ite.dynak.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.pwr.ite.dynak.loader.CustomClassLoader;

public class AppController {

    @FXML private TreeTableColumn<?, ?> classTreeTableView;

    @FXML private Button chooseClassDirectoryButton;

    @FXML private Button loadClassButton;

    @FXML private Button getClassInfoButton;

    @FXML private Button sendRequestButton;

    @FXML private TextArea classInfoTextField;

    @FXML private TextField requestTextField;

    @FXML private TextArea inputTextArea;

    @FXML private TextArea ouputTextArea;

    @FXML private Label progressLabel;

    @FXML private ProgressBar progressBar;

    @FXML private TextArea logTextArea;

    private CustomClassLoader classLoader;
    private 

    private void initialize() {
        chooseClassDirectoryButton.setOnAction(e -> directoryButtonHandler());
    }

    private void directoryButtonHandler() {

    }
}

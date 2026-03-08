package pl.pwr.ite.dynak.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppController {
    @FXML
    private Button chooseFilesButton;

    @FXML
    private Button zipButton;

    @FXML
    private Button checkHashButton;

    @FXML
    private TextArea filesChosenTextArea;

    @FXML
    private TextArea resultTextArea;

    private ArrayList<String> chosenFiles = new ArrayList<>();

    @FXML
    private void initialize() {
        chooseFilesButton.setOnAction(event -> chooseFiles());
        zipButton.setOnAction(event -> zip());
        checkHashButton.setOnAction(event -> checkHash());
    }

    private void chooseFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose files to zip");

        Stage stage = (Stage) chooseFilesButton.getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            chosenFiles.clear();
            filesChosenTextArea.clear();
            for (File file : files) {
                chosenFiles.add(file.getAbsolutePath());
                filesChosenTextArea.appendText(file.getAbsolutePath() + "\n");
            }
            resultTextArea.appendText("Selected " + chosenFiles.size() + " files\n");
        } else {
            resultTextArea.appendText("No files selected\n");
        }
    }

    private void zip() {

    }

    private void checkHash() {

    }
}

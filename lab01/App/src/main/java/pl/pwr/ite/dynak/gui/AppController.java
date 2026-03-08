package pl.pwr.ite.dynak.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static pl.pwr.ite.dynak.zipper.Hasher.checkHash;
import static pl.pwr.ite.dynak.zipper.Hasher.generateHashFile;
import static pl.pwr.ite.dynak.zipper.Zipper.zip;

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

    @FXML
    private Button hashButton;

    private ArrayList<String> chosenFiles = new ArrayList<>();

    private String hashFilePath;

    @FXML
    private void initialize() {
        chooseFilesButton.setOnAction(event -> chooseFiles());
        zipButton.setOnAction(event -> zipHandler());
        checkHashButton.setOnAction(event -> checkHashHandler());
        hashButton.setOnAction(event -> {
            try {
                generateHashHandler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void chooseFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose files");

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

    private void zipHandler() {
        if (chosenFiles.isEmpty()) {
            resultTextArea.appendText("No files selected\n");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ZIP file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));
        fileChooser.setInitialFileName("archive.zip");

        Stage stage = (Stage) zipButton.getScene().getWindow();
        File outputFile = fileChooser.showSaveDialog(stage);

        if (outputFile != null) {
            String outputPath = outputFile.getAbsolutePath();
            try {
                zip(chosenFiles, outputPath);
                resultTextArea.appendText("ZIP created at: " + outputPath + "\n");
            } catch (Exception e) {
                resultTextArea.appendText("Error during zipping: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        } else {
            resultTextArea.appendText("ZIP creation cancelled\n");
        }

        chosenFiles.clear();
        filesChosenTextArea.clear();
    }

    private void checkHashHandler() {
        String fileToCheck = chosenFiles.get(0);

        if (fileToCheck != null && checkHash(fileToCheck, hashFilePath)) {
            resultTextArea.appendText("Hash matches\n");
        }
        else {
            resultTextArea.appendText("No file selected\n");
        }
    }

    private void generateHashHandler() throws IOException, NoSuchAlgorithmException {
        hashFilePath = generateHashFile(chosenFiles.get(0));
        resultTextArea.appendText("Hash generated at " + hashFilePath + "\n");
    }
}

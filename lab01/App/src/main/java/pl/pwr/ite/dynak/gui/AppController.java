package pl.pwr.ite.dynak.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.zipper.Zipper;

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

    @FXML
    private Button chooseHashFileButton;

    @FXML
    private Button chooseDirectoryButton;

    @FXML
    private Button clearFilesButton;

    @FXML
    private Button unzipFilesButton;

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
            } catch (IOException | NoSuchAlgorithmException _) {
                throw new RuntimeException();
            }
        });
        chooseHashFileButton.setOnAction(event -> chooseHashFileHandler());
        chooseDirectoryButton.setOnAction(event -> chooseDirectory());
        clearFilesButton.setOnAction(event -> clearFiles());
        unzipFilesButton.setOnAction(event -> unzipFiles());
    }

    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder");

        Stage stage = (Stage) chooseDirectoryButton.getScene().getWindow();
        File folder = directoryChooser.showDialog(stage);

        if (folder != null) {
            String folderPath = folder.getAbsolutePath();
            chosenFiles.add(folderPath);
            filesChosenTextArea.appendText(folderPath + "\n");
            resultTextArea.appendText("Selected folder: " + folder.getAbsolutePath() + "\n");
        }
        else {
            resultTextArea.appendText("No folder selected\n");
        }
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
        String fileToCheck = chosenFiles.getFirst();

        if (fileToCheck != null && checkHash(fileToCheck, hashFilePath)) {
            resultTextArea.appendText("Hash matches\n");
        }
        else if (fileToCheck != null && !checkHash(fileToCheck, hashFilePath)) {
            resultTextArea.appendText("Hash does not match\n");
        }
        else {
            resultTextArea.appendText("No file selected\n");
        }
    }

    private void generateHashHandler() throws IOException, NoSuchAlgorithmException {
        generateHashFile(chosenFiles.getFirst());
        resultTextArea.appendText("Hash generated at " + hashFilePath + "\n");
    }

    private void chooseHashFileHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose hash file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Hash Files", "*.sha256"));

        Stage stage = (Stage) chooseHashFileButton.getScene().getWindow();
        File hashFile = fileChooser.showOpenDialog(stage);

        if (hashFile != null) {
            hashFilePath = hashFile.getAbsolutePath();
            resultTextArea.appendText("Hash file selected: " + hashFilePath + "\n");
        } else {
            resultTextArea.appendText("No hash file selected\n");
        }
    }

    private void clearFiles() {
        filesChosenTextArea.clear();
        chosenFiles.clear();
        resultTextArea.appendText("Files cleared\n");
    }

    private void unzipFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose ZIP file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));

        Stage stage = (Stage) unzipFilesButton.getScene().getWindow();
        File zipFile = fileChooser.showOpenDialog(stage);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose destination for unzipping");
        File destinationDir = directoryChooser.showDialog(stage);

        if (zipFile != null && destinationDir != null) {
            try {
                Zipper.unzipFile(zipFile, destinationDir);
                resultTextArea.appendText("Files unzipped to: " + destinationDir.getAbsolutePath() + "\n");
            } catch (IOException e) {
                resultTextArea.appendText("Error during unzipping: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }
}

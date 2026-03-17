package pl.pwr.ite.dynak.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import pl.pwr.ite.dynak.lib.cache.WeakCache;
import pl.pwr.ite.dynak.lib.cache.WeakCacheResult;
import pl.pwr.ite.dynak.lib.models.CsvData;
import pl.pwr.ite.dynak.lib.models.ImageData;
import pl.pwr.ite.dynak.lib.models.ParsedCsvData;
import pl.pwr.ite.dynak.lib.parser.CsvParser;
import pl.pwr.ite.dynak.lib.parser.ImageParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AppController {

    @FXML
    private Button chooseFolderButton;

    @FXML
    private TreeView<File> treeView;

    @FXML
    private TextArea logTextArea;

    @FXML
    private TextArea fileTextArea;

    @FXML
    private ImageView imageViewArea;

    private final WeakCache<ParsedCsvData> csvCache = new WeakCache<>(new CsvParser());

    private final WeakCache<ImageData> imageCache = new WeakCache<>(new ImageParser());

    @FXML
    private void initialize() {
        chooseFolderButton.setOnAction(e -> chooseFolder());

        treeView.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    String name = item.getName();
                    setText(name == null || name.isBlank() ? item.getPath() : name);
                }
            }
        });

        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getValue() == null) {
                return;
            }

            File selectedFile = newValue.getValue();
            if (selectedFile.isFile()) {
                showPreview(selectedFile.toPath());
            }
        });

        fileTextArea.setVisible(false);
        imageViewArea.setVisible(false);
    }

    private void chooseFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder");

        File selectedDirectory = directoryChooser.showDialog(
                chooseFolderButton.getScene() != null ? chooseFolderButton.getScene().getWindow() : null
        );

        if (selectedDirectory == null) {
            log("Folder selection cancelled.");
            return;
        }

        TreeItem<File> rootItem = createTreeItem(selectedDirectory);
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);

        log("Opened folder: " + selectedDirectory.getAbsolutePath());
    }

    private TreeItem<File> createTreeItem(File file) {
        TreeItem<File> item = new TreeItem<>(file);

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children != null) {
                for (File child : children) {
                    item.getChildren().add(createTreeItem(child));
                }
            }
        }

        return item;
    }

    private void showPreview(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            hidePreview();
            log("Unsupported file type: " + fileName);
            return;
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();

        try {
            switch (extension) {
                case "png", "jpg", "jpeg" -> showImage(filePath);
                case "csv" -> showCsv(filePath);
                default -> {
                    hidePreview();
                    log("Unsupported file type: " + fileName);
                }
            }
        } catch (Exception e) {
            hidePreview();
            log("Failed to open " + fileName + ": " + e.getMessage());
        }
    }

    private void showImage(Path filePath) throws IOException {

        WeakCacheResult<ImageData> result = imageCache.get(filePath.toFile());
        double[] imageSize = new double[]{ result.fileData().height(), result.fileData().width() };

        imageViewArea.setImage(result.fileData().image());
        imageViewArea.setVisible(true);

        fileTextArea.clear();
        fileTextArea.setVisible(false);

        log((result.fromCache() ? "Loaded from cache: " : "Loaded image: ") + filePath.getFileName() + " (" + imageSize[0] + "x" + imageSize[1] + ")");
    }

    private void showCsv(Path filePath) throws IOException {
        WeakCacheResult<ParsedCsvData> result = csvCache.get(filePath.toFile());
        ParsedCsvData data = result.fileData();

        StringBuilder builder = new StringBuilder();
        builder.append("CSV preview: ").append(filePath.getFileName()).append("\n\n");
        builder.append("Most common car: ").append(data.mostCommonCar()).append("\n");
        builder.append("Longest name: ").append(data.longestName()).append("\n");
        builder.append("Most common gender: ").append(data.mostCommonGender()).append("\n");
        builder.append("Shortest last name: ").append(data.shortestLastName()).append("\n");
        builder.append("Most common name: ").append(data.mostCommonName()).append("\n");
        builder.append("Tax dodger amount: ").append(data.taxDodgerAmount()).append("\n");
        builder.append("Most common city: ").append(data.mostCommonCity()).append("\n\n");
        builder.append("First rows:\n");

        for (CsvData row : data.csvDataList()) {
            builder.append("- ")
                    .append(row.name()).append(" ")
                    .append(row.lastName())
                    .append(", ").append(row.gender())
                    .append(", ").append(row.car())
                    .append(", taxDodger=").append(row.taxDodger())
                    .append(", ").append(row.city())
                    .append("\n");
        }

        fileTextArea.setText(builder.toString());
        fileTextArea.setVisible(true);

        imageViewArea.setImage(null);
        imageViewArea.setVisible(false);
        log((result.fromCache() ? "Loaded from cache: " : "Parsed file: ") + filePath.getFileName());
    }

    private void hidePreview() {
        fileTextArea.clear();
        fileTextArea.setVisible(false);

        imageViewArea.setImage(null);
        imageViewArea.setVisible(false);
    }

    private void log(String message) {
        if (!logTextArea.getText().isEmpty()) {
            logTextArea.appendText("\n");
        }
        logTextArea.appendText(message);
    }
}

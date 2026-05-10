package pl.pwr.ite.dynak.app;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.filters.GaussianFilter;
import pl.pwr.ite.dynak.filters.MedianFilter;
import pl.pwr.ite.dynak.utils.ImageFilterer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public class AppController {

    private ImageFilterer filterer = new ImageFilterer();
    private String currentFilter = "Gaussian";

    @FXML
    private ImageView inputImageView;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button swapFilterButton;

    @FXML
    private TextField filterPassAmountTextField;

    @FXML
    private TextField filterRadiusTextField;

    @FXML
    private Button runNativeButton;

    @FXML
    private Button runJavaButton;

    @FXML
    private ImageView outputImageView;

    @FXML
    private TextArea logTextField;

    @FXML
    private void initialize() {
        runJavaButton.setOnAction(e -> runJavaButtonHandler());
        runNativeButton.setOnAction(e -> runNativeButtonHandler());
        swapFilterButton.setOnAction(e -> swapFilterButtonHandler());
        selectImageButton.setOnAction(e -> selectImageButtonHandler());
    }

    private void runJavaButtonHandler() {
        int passAmount = Integer.parseInt(filterPassAmountTextField.getText());
        int radius = Integer.parseInt(filterRadiusTextField.getText());

        Image inputImage = inputImageView.getImage();
        Instant start = Instant.now();
        Image outputImage = filterer.applyFilterToImage(inputImage, passAmount, radius, currentFilter);
        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();
        outputImageView.setImage(outputImage);

        log("Time elapsed: " + timeElapsed + "ms for Java written " + currentFilter + " filter");
    }

    private void runNativeButtonHandler() {
        int passAmount = Integer.parseInt(filterPassAmountTextField.getText());
        int radius = Integer.parseInt(filterRadiusTextField.getText());

        Image inputImage = inputImageView.getImage();
        Instant start = Instant.now();
        Image outputImage = filterer.applyFilterToImageNative(inputImage, passAmount, radius, currentFilter);
        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();
        outputImageView.setImage(outputImage);

        log("Time elapsed: " + timeElapsed + "ms for C written " + currentFilter + " filter");
    }

    private void swapFilterButtonHandler() {
        if (currentFilter.equals("Gaussian")) {
            currentFilter = "Median";
            log("Swapped to Median filter");
        }
        else {
            currentFilter = "Gaussian";
            log("Swapped to Gaussian filter");
        }
    }

    private void selectImageButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");

        Stage stage = (Stage) selectImageButton.getScene().getWindow();


        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File image = fileChooser.showOpenDialog(stage);

        if (image != null) {
            inputImageView.setImage(new Image(image.toURI().toString()));
            log("Selected file: " + image.getAbsolutePath());
        } else {
            log("No file selected");
        }
    }

    private void log(String message) {
        if (!logTextField.getText().isEmpty()) {
            logTextField.appendText("\n");
        }
        logTextField.appendText(message);
    }

}

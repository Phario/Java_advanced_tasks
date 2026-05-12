package pl.pwr.ite.dynak.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.lib.processors.DomProcessor;
import pl.pwr.ite.dynak.lib.processors.IProcessor;
import pl.pwr.ite.dynak.lib.processors.JaxbProcessor;
import pl.pwr.ite.dynak.lib.processors.SaxProcessor;
import pl.pwr.ite.dynak.lib.utils.RecallResponse;
import pl.pwr.ite.dynak.lib.utils.XsltTransformer;

import java.io.File;
import java.nio.file.Files;

public class AppController {
    private RecallResponse recallResponse = null;
    private File xmlFile = null;

    private File xsltFile = null;

    private IProcessor processor = null;

    private XsltTransformer xsltTransformer = new XsltTransformer();

    @FXML
    private Button loadXmlButton;

    @FXML
    private Button loadXsltButton;

    @FXML
    private Button jaxbParseButton;

    @FXML
    private Button domParseButton;

    @FXML
    private Button saxParseButton;

    @FXML
    private Button clearObjectViewButton;

    @FXML
    private TextArea logTextField;

    @FXML
    private TextArea objectViewTextField;

    @FXML
    private WebView htmlWebView;

    @FXML
    private void initialize() {
        loadXsltButton.setOnAction(e -> loadXsltButtonHandler());
        jaxbParseButton.setOnAction(e -> jaxbParseButtonHandler());
        domParseButton.setOnAction(e -> domParseButtonHandler());
        saxParseButton.setOnAction(e -> saxParseButtonHandler());
        clearObjectViewButton.setOnAction(e -> clearObjectViewButtonHandler());
        loadXmlButton.setOnAction(e -> selectXmlButtonHandler());
    }

    private void saxParseButtonHandler() {
        processor = new SaxProcessor();

        try {
            recallResponse = processor.process(xmlFile);
            objectViewTextField.setText(parseRecallResponse());
            log("Object view updated with SaX processor");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    private void domParseButtonHandler() {
        processor = new DomProcessor();

        try {
            recallResponse = processor.process(xmlFile);
            objectViewTextField.setText(parseRecallResponse());
            log("Object view updated with DOM processor");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    private void jaxbParseButtonHandler() {
        processor = new JaxbProcessor();

        try {
            recallResponse = processor.process(xmlFile);
            objectViewTextField.setText(parseRecallResponse());
            log("Object view updated with JAXB processor");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    private void clearObjectViewButtonHandler() {
        if (objectViewTextField != null) {
            objectViewTextField.clear();
            log("Object view cleared");
        }
        else {
            log("Object view is not initialized");
        }
    }

    private void loadXsltButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XSLT file");

        Stage stage = (Stage) loadXmlButton.getScene().getWindow();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XSLT Files", "*.xslt")
        );

        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            log("No file selected");
            return;
        }

        xsltFile = file;
        log("Selected XSLT file: " + file.getAbsolutePath());


        try {
            String xml = Files.readString(xmlFile.toPath());
            String xslt = Files.readString(xsltFile.toPath());
            String html = xsltTransformer.transform(xml, xslt);
            htmlWebView.getEngine().loadContent(html);
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    private void selectXmlButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML file");

        Stage stage = (Stage) loadXmlButton.getScene().getWindow();


        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            xmlFile = file;
            log("Selected XML file: " + file.getAbsolutePath());
        }
        else {
            log("No file selected");
        }
    }

    private void log(String message) {
        if (!logTextField.getText().isEmpty()) {
            logTextField.appendText("\n");
        }
        logTextField.appendText(message);
    }

    private String parseRecallResponse() {
        StringBuilder sb = new StringBuilder();

        int i = 1;

        for (var recall : recallResponse.getRecalls()) {
            sb.append("======= Row number: " + i + " =======\n");
            sb.append("Report received date: " + recall.getReportReceivedDate() + "\n");
            sb.append("Manufacturer: " + recall.getManufacturer() + "\n");
            sb.append("Subject: " + recall.getSubject() + "\n");
            sb.append("Affected component: " + recall.getComponent() + "\n");
            sb.append("Potentially affected: " + recall.getPotentiallyAffected() + "\n");
            i++;
        }

        return sb.toString();
    }
}

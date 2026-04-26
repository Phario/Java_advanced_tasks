package pl.pwr.ite.dynak.app;

import ex.api.AnalysisException;
import ex.api.AnalysisService;
import ex.api.DataSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.pwr.ite.dynak.app.utils.AnalysisServiceLoader;
import pl.pwr.ite.dynak.app.utils.DataGenerator;

import java.util.ArrayList;
import java.util.List;

public class AppController {

    List<AnalysisService> services = new ArrayList<>();
    DataGenerator dataGenerator = new DataGenerator();
    int pointAmount;
    DataSet dataSet = null;
    DataSet resultDataSet = null;
    AnalysisServiceLoader loader = new AnalysisServiceLoader();

    @FXML
    private TableView<String[]> dataTableView;

    @FXML
    private TableColumn<String[], String> xColumn;

    @FXML
    private TableColumn<String[], String> yColumn;

    @FXML
    private TableView<String[]> resultTableView;

    @FXML
    private TableColumn<String[], String> xResult;

    @FXML
    private TableColumn<String[], String> yResult;

    @FXML
    private Button generateDataButton;

    @FXML
    private TableView<AnalysisService> algorithmsTableView;

    @FXML
    private TableColumn<AnalysisService, String> algorithmColumn;

    @FXML
    private Button loadOptionsButton;

    @FXML
    private TextField optionsTextField;

    @FXML
    private TextField pointAmountField;

    @FXML
    private Button calculateClustersButton;

    @FXML
    private Button setPointAmountButton;

    @FXML
    private TextArea logTextArea;

    @FXML
    private void initialize() {
        xColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()[0]));

        yColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()[1]));

        xResult.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()[0]));

        yResult.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()[1]));

        loadOptionsButton.setOnAction(e -> loadOptionsButtonHandler());
        generateDataButton.setOnAction(e -> generateDataButtonHandler());
        setPointAmountButton.setOnAction(e -> setPointAmountButtonHandler());
        calculateClustersButton.setOnAction(e -> calculateClustersButtonHandler());
        populateAlgorithmsTable();
    }

    private void calculateClustersButtonHandler(){
        if (dataSet == null) {
            log("No data set");
            return;
        }

        AnalysisService selectedService = algorithmsTableView.getSelectionModel().getSelectedItem();

        if (selectedService == null) {
            log("No algorithm selected");
            return;
        }

        try {
            selectedService.submit(dataSet);
            printResults();
        } catch (AnalysisException e) {
            log(e.getMessage());
        }
    }

    private void loadOptionsButtonHandler(){
        String[] options = optionsTextField.getText().split(",");
        for (AnalysisService service : services) {
            try {
                service.setOptions(options);
            } catch (AnalysisException e) {
                log(e.getMessage());
            }
        }
    }

    private void generateDataButtonHandler(){
        if (pointAmount <= 0) {
            log("Invalid point amount");
            return;
        }

        dataGenerator.setPointAmount(pointAmount);
        dataSet = dataGenerator.generateDataSet();
        populateDataTable();
    }

    private void populateAlgorithmsTable(){
        algorithmsTableView.getItems().clear();

        services.add(loader.loadAnalysisService("K-Mean"));
        services.add(loader.loadAnalysisService("K-Median"));


        algorithmColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        ObservableList<AnalysisService> algorithms = FXCollections.observableArrayList(services);
        algorithmsTableView.setItems(algorithms);
    }

    private void populateDataTable(){
        dataTableView.getItems().clear();

        if (dataSet == null) {
            log("No data set");
            return;
        }

        String[] header = dataSet.getHeader();

        if (header.length >= 2) {
            xColumn.setText(header[0]);
            yColumn.setText(header[1]);
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(dataSet.getData());
        dataTableView.setItems(data);
    }

    private void setPointAmountButtonHandler(){
        try {
            pointAmount = Integer.parseInt(pointAmountField.getText());
        }
        catch (Exception e){
            log("Invalid point amount");
        }
    }

    private void log(String message) {
        if (!logTextArea.getText().isEmpty()) {
            logTextArea.appendText("\n");
        }
        logTextArea.appendText(message);
    }

    private void printResults() {
        resultTableView.getItems().clear();

        try {
            resultDataSet = algorithmsTableView.getSelectionModel().getSelectedItem().retrieve(true);
        } catch (AnalysisException e) {
            log(e.getMessage());
        }

        if (resultDataSet == null) {
            log("No results");
            return;
        }

        String[] header = resultDataSet.getHeader();

        if (header.length >= 2) {
            xResult.setText(header[0]);
            yResult.setText(header[1]);
        }

        ObservableList<String[]> data = FXCollections.observableArrayList(resultDataSet.getData());
        resultTableView.setItems(data);
    }
}

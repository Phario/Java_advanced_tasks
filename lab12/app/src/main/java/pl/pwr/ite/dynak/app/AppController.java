package pl.pwr.ite.dynak.app;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import pl.pwr.ite.dynak.app.map.Map;
import pl.pwr.ite.dynak.lib.ScriptService;

import java.io.File;

public class AppController {
    ScriptService scriptService = new ScriptService();

    int[][] map = null;
    int[][] path = null;

    Map mapUI = null;

    @FXML
    public TextField mapSizeTextField;
    @FXML
    public TextField startingPointTextField;
    @FXML
    public TextField finishPointTextField;
    @FXML
    public TableColumn<String, String> scriptNameTableColumn;
    @FXML
    public TableView<String> scriptsTableView;
    @FXML
    public Button chooseScriptFolderButton;
    @FXML
    public Button generateMapButton;
    @FXML
    public Button findPathButton;
    @FXML
    public TextArea logTextArea;
    @FXML
    public StackPane mapStackPane;

    @FXML
    public Button clearScriptsButton;

    @FXML
    private void initialize() {
        findPathButton.setOnAction(event -> findPathButtonHandler());
        generateMapButton.setOnAction(event -> generateMapButtonHandler());
        chooseScriptFolderButton.setOnAction(event -> chooseScriptFolderButtonHandler());
        clearScriptsButton.setOnAction(event -> clearScripts());

        scriptNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue())
        );
    }

    private String getSelectedScriptName() {
        return scriptsTableView.getSelectionModel().getSelectedItem();
    }

    private void findPathButtonHandler() {
        String scriptName = getSelectedScriptName();
        if (scriptName == null || !scriptName.contains("Solver")) {
            log("Invalid or no script selected");
            return;
        }
        if (map == null) {
            log("Map not generated");
            return;
        }

        int[] startAndEndCells = getStartAndEndCells();

        if (startAndEndCells == null) {
            log("Invalid start or end coordinates");
            return;
        }

        path = scriptService.getPathFromScript(
                scriptName,
                map,
                startAndEndCells[0],
                startAndEndCells[1],
                startAndEndCells[2],
                startAndEndCells[3]
        );

        if (path == null) {
            log("No path found");
            return;
        }

        displayPath(path);

        log("Path found");
    }

    private void generateMapButtonHandler() {
        String scriptName = getSelectedScriptName();

        if (scriptName == null || !scriptName.contains("Generator")) {
            log("Invalid or no script selected");
            return;
        }

        int[] mapSize = getMapSize();
        if (mapSize == null) {
            log("Invalid map size");
            return;
        }

        map = scriptService.getMapFromScript(scriptName, mapSize[0], mapSize[1]);
        displayMap(map);

        log("Map generated with script: " + scriptName + "");
        log("Map generated with size: " + mapSize[0] + "x" + mapSize[1] + "");
    }

    private int[] getStartAndEndCells() {
        try {
            String startText = startingPointTextField.getText();
            String finishText = finishPointTextField.getText();

            String[] startParts = startText.split(",");
            String[] finishParts = finishText.split(",");

            if (startParts.length != 2 || finishParts.length != 2) {
                log("Invalid start or end coordinates");
                return null;
            }

            int startX = Integer.parseInt(startParts[0].trim());
            int startY = Integer.parseInt(startParts[1].trim());
            int endX = Integer.parseInt(finishParts[0].trim());
            int endY = Integer.parseInt(finishParts[1].trim());

            return new int[]{startX, startY, endX, endY};
        } catch (NumberFormatException e) {
            log("Invalid start or end coordinates");
            return null;
        }
    }

    private int[] getMapSize() {
        try {
            String mapSizeText = mapSizeTextField.getText();
            String[] parts = mapSizeText.split(",");

            if (parts.length != 2) {
                log("Invalid map size");
                return null;
            }

            int width = Integer.parseInt(parts[0].trim());
            int height = Integer.parseInt(parts[1].trim());

            return new int[]{width, height};
        } catch (NumberFormatException e) {
            log("Invalid map size");
            return null;
        }
    }

    private void chooseScriptFolderButtonHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose class directory");

        File selectedDirectory = directoryChooser.showDialog(chooseScriptFolderButton.getScene().getWindow());

        if (selectedDirectory == null) {
            log("No directory selected");
            return;
        }

        scriptService.loadContexts(selectedDirectory.getAbsolutePath());

        scriptsTableView.getItems().clear();
        scriptsTableView.getItems().addAll(scriptService.getLoadedScripts());

        log("Selected directory: " + selectedDirectory.getAbsolutePath());
        log("Loaded " + scriptService.getLoadedScripts().size() + " scripts.");
    }

    private void displayMap(int[][] mapData) {
        mapStackPane.getChildren().clear();

        int rows = mapData.length;
        int cols = mapData[0].length;

        mapUI = new Map(cols, rows, mapStackPane.getPrefWidth(), mapStackPane.getPrefHeight());

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                var cell = new pl.pwr.ite.dynak.app.map.Cell(c, r);

                if (mapData[r][c] == 1) {
                    cell.getStyleClass().add("wall");
                }

                mapUI.add(cell, c, r);
            }
        }

        mapStackPane.getChildren().add(mapUI);
    }

    private void displayPath(int[][] pathData) {
        for(int[] cell : pathData) {
            int x = cell[0];
            int y = cell[1];

            var pathCell = mapUI.getCell(x, y);

            pathCell.highlightPath();
        }
    }

    private void clearScripts() {
        scriptsTableView.getItems().clear();
        scriptService.unloadContexts();
        log("Cleared scripts");
    }

    private void log(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }
}
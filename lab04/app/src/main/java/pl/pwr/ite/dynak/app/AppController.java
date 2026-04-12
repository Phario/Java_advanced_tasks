package pl.pwr.ite.dynak.app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.Status;
import pl.pwr.ite.dynak.lib.StatusListener;
import pl.pwr.ite.dynak.loader.ClassState;
import pl.pwr.ite.dynak.loader.CustomClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    @FXML private TableView<ClassState> classTableView;

    @FXML private TableColumn<ClassState, String> classNameColumn;

    @FXML private TableColumn<ClassState, String> statusColumn;

    @FXML private Button chooseClassDirectoryButton;

    @FXML private Button loadClassButton;

    @FXML private Button unloadClassButton;

    @FXML private Button getClassInfoButton;

    @FXML private Button sendRequestButton;

    @FXML private TextArea classInfoTextField;

    @FXML private TextField requestTextField;

    @FXML private TextArea inputTextArea;

    @FXML private TextArea outputTextArea;

    @FXML private Label progressLabel;

    @FXML private ProgressBar progressBar;

    @FXML private TextArea logTextArea;

    private CustomClassLoader classLoader;

    private Class<?> loadedClass;

    private Method getInfoMethod;

    private Method submitTaskMethod;

    private Method getResultMethod;

    private Processor processor;

    private Path basePath;

    private String result;

    @FXML
    private void initialize() {
        chooseClassDirectoryButton.setOnAction(e -> directoryButtonHandler());

        classNameColumn.setCellValueFactory(
                param -> param.getValue().classNameProperty()
        );
        statusColumn.setCellValueFactory(
                param -> param.getValue().statusProperty()
        );

        loadClassButton.setOnAction(e -> loadClassButtonHandler());

        unloadClassButton.setOnAction(e -> unloadClassButtonHandler());

        getClassInfoButton.setOnAction(e -> getClassInfoButtonHandler());

        sendRequestButton.setOnAction(e -> sendRequestButtonHandler());
    }

    private void processRequest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    result = (String) getResultMethod.invoke(processor);
                } catch (InterruptedException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    System.out.println("Result: " + result);
                    Platform.runLater(() -> outputTextArea.setText(result));
                    result = null;
                    break;
                }
            }
            executorService.shutdown();
        });
    }

    private void sendRequestButtonHandler() {
        var request = requestTextField.getText();

        if (request == null || request.isEmpty()) {
            log("No request specified");
            return;
        }
        else if (submitTaskMethod == null || processor == null) {
            log("No class loaded");
            return;
        }

        try {
            // ajajec dodał te printlny ale zostawiam bo przydadzą się
            System.out.println("Calling submitTask with: '" + request + "' (length=" + request.length() + ")");
            System.out.println("Method: " + submitTaskMethod);
            System.out.println("Processor class: " + processor.getClass().getName());
            System.out.println("Processor classloader: " + processor.getClass().getClassLoader());

            String requestData = request + "\n" + inputTextArea.getText();

            var isTaskAccepted = (boolean)submitTaskMethod.invoke(processor, requestData, new ProgressListener(progressBar));

            System.out.println("Result: " + isTaskAccepted);

            if (isTaskAccepted) {
                log("Task accepted");
                processRequest();
            }
            else {
                log("Task rejected");
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            log("Error sending request: " + e.getMessage());
        }
    }

    private void getClassInfoButtonHandler() {
        if (loadedClass == null) {
            log("No class loaded");
            return;
        }

        try {
            classInfoTextField.setText((String)getInfoMethod.invoke(processor));

        } catch (InvocationTargetException | IllegalAccessException e) {
            log("Error getting class info: " + e.getMessage());
        }
    }

    private void unloadClassButtonHandler() {
        loadedClass = null;
        classLoader = null;
        classInfoTextField.setText("");
        inputTextArea.setText("");
        outputTextArea.setText("");

        ClassState classState = classTableView.getSelectionModel().getSelectedItem();

        String classStateName = classState.getClassName().getValue();

        log("The class loader and " + classStateName + " have been unloaded");

        classState.setStatus("Unloaded");
    }

    private void loadClassButtonHandler() {
        if (classLoader == null || basePath != null) {
            classLoader = new CustomClassLoader(basePath);
        }

        var classState = classTableView.getSelectionModel().getSelectedItem();

        String className = classState.getClassName().getValue();

        if (className == null) {
            log("No class selected");
            return;
        }
        try {
            loadedClass = classLoader.loadClass(className);
            log("Loaded class: " + className);
            classState.setStatus("Loaded");

            classLoader = null;
            for (var row : classTableView.getItems()) {
                if (!row.getClassName().getValue().equals(className)) {
                    row.setStatus("Unloaded");
                }
            }

            Constructor<?> constructor = loadedClass.getConstructor();
            processor = (Processor)constructor.newInstance();

            getInfoMethod = loadedClass.getDeclaredMethod("getInfo");
            submitTaskMethod = loadedClass.getDeclaredMethod("submitTask", String.class, StatusListener.class);
            getResultMethod = loadedClass.getDeclaredMethod("getResult");

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log("Class not found: " + className);
        }
    }

    private void directoryButtonHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose class directory");

        File selectedDirectory = directoryChooser.showDialog(chooseClassDirectoryButton.getScene().getWindow());


        if (selectedDirectory == null) {
            log("No directory selected");
            return;
        }

        basePath = selectedDirectory.toPath();
        loadedClass = null;
        classLoader = new CustomClassLoader(basePath);
        populateClassTable(basePath);

        log("Selected directory: " + selectedDirectory.getAbsolutePath());
    }

    private void populateClassTable(Path basePath) {
        classTableView.getItems().clear();

        try {
            Files.walkFileTree(basePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".class") && !file.toString().contains("module-info")) {
                        Path relativePath = basePath.relativize(file);
                        String className = relativePath.toString()
                                .replace(File.separatorChar, '.')
                                .replace(".class", "");
                        classTableView.getItems().add(
                                new ClassState(className)
                        );
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log("Error populating class table: " + e.getMessage());
        }

        log("Found " + classTableView.getItems().size() + " class(es)");
    }

    private void log(String message) {
        if (!logTextArea.getText().isEmpty()) {
            logTextArea.appendText("\n");
        }
        logTextArea.appendText(message);
    }
}

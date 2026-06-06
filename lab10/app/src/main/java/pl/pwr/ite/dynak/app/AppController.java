package pl.pwr.ite.dynak.app;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import pl.pwr.ite.dynak.lib.TlsClient;
import pl.pwr.ite.dynak.lib.TlsServer;
import pl.pwr.ite.dynak.lib.utils.Attachment;
import pl.pwr.ite.dynak.lib.utils.MessageListener;

import java.io.File;
import java.time.LocalDateTime;

public class AppController implements MessageListener {

    private TlsClient client;
    private TlsServer server;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private Button sendMessageButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button disconnectButton;

    @FXML
    private Button sendAttachmentButton;

    @FXML
    private Button downloadAttachmentButton;

    @FXML
    private Button createServerButton;

    @FXML
    private TextArea logTextArea;

    @FXML
    private TableView<Attachment> attachmentsTable;

    @FXML
    private TableColumn<Attachment, String> timeStampColumn;

    @FXML
    private TableColumn<Attachment, String> fileNameColumn;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField chatMessageTextField;

    @FXML
    private void initialize() {
        connectButton.setOnAction(e -> connect());
        createServerButton.setOnAction(e -> createServer());
        //disconnectButton.setOnAction(e -> disconnect());
        sendMessageButton.setOnAction(e -> sendMessage());
        downloadAttachmentButton.setOnAction(e -> downloadAttachment());
        sendAttachmentButton.setOnAction(e -> sendAttachment());

        timeStampColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeStamp());
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMessage());
    }

    private void downloadAttachment() {
        Attachment selected =
                attachmentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            log("No attachment selected");

            return;
        }

        FileChooser chooser = new FileChooser();

        chooser.setInitialFileName(
                selected.getFile().getName()
        );

        File destination = chooser.showSaveDialog(
                downloadAttachmentButton.getScene().getWindow()
        );

        if (destination == null) {
            return;
        }

        try {

            java.nio.file.Files.copy(
                    selected.getFile().toPath(),
                    destination.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            log("Attachment saved to: "
                    + destination.getAbsolutePath());

        } catch (Exception e) {

            log(e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            String message = usernameTextField.getText() + ": " + chatMessageTextField.getText();

            client.sendMessage(message);

            chatMessageTextField.clear();
            log("Message sent: " + message);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    private void createServer() {
        Thread thread = new Thread(() -> {
            try {
                server = new TlsServer(this);

                server.start(
                        Integer.parseInt(portTextField.getText())
                );
            } catch (Exception e) {
                log(e.getMessage());
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void connect() {
        try {
            client = new TlsClient(this);

            client.connect(
                    ipTextField.getText(),
                    Integer.parseInt(portTextField.getText())
            );
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    private void sendAttachment() {
        FileChooser chooser = new FileChooser();

        File file = chooser.showOpenDialog(
                sendAttachmentButton.getScene().getWindow()
        );

        if (file == null) {
            return;
        }

        try {
            client.sendFile(file);
            log("Attachment sent: " + file.getName());
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    private void addAttachmentToTable(File file) {
        Attachment attachment = new Attachment(
                new SimpleStringProperty(LocalDateTime.now().toString()),
                new SimpleStringProperty(file.getName()),
                file
        );

        attachmentsTable.getItems().add(attachment);
    }

    @Override
    public void onMessage(String message) {
        Platform.runLater(() -> {
            chatTextArea.appendText(message + "\n");
        });
    }

    @Override
    public void onFileReceived(File file) {
        Platform.runLater(() -> {
            addAttachmentToTable(file);

            log("Attachment received: " + file.getName());
        });
    }

    @Override
    public void onLog(String message) {
        Platform.runLater(() -> log(message));
    }

    private void log(String message) {
        if (!logTextArea.getText().isEmpty()) {
            logTextArea.appendText("\n");
        }
        logTextArea.appendText(message);
    }
}

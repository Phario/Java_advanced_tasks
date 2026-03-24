package pl.pwr.ite.dynak.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.pwr.ite.dynak.requester.interfaces.IQuestionBuilder;

import java.io.IOException;

public class Questionnaire extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Questionnaire.fxml"));
        Parent root = loader.load();

        var scene = new Scene(root);

        primaryStage.setTitle("Questionnaire");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

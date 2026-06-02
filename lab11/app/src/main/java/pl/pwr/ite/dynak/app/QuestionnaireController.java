package pl.pwr.ite.dynak.app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import pl.pwr.ite.dynak.requester.interfaces.IQuestionBuilder;
import pl.pwr.ite.dynak.requester.interfaces.IRequester;
import pl.pwr.ite.dynak.requester.models.Question;
import pl.pwr.ite.dynak.requester.util.QuestionBuilder;
import pl.pwr.ite.dynak.requester.util.Requester;

import java.util.Locale;
import java.util.ResourceBundle;

public class QuestionnaireController {

    private boolean QUIZ_STARTED = false;

    private Question currentQuestion = null;

    private Locale locale = new Locale("en", "US");

    private ResourceBundle bundle = ResourceBundle.getBundle("UIBundle", locale);

    private IRequester requester = new Requester();

    private IQuestionBuilder questionBuilder = new QuestionBuilder(locale, requester);

    @FXML
    private Label questionPromptLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private TextField answerField;

    @FXML
    private Button universalButton;

    @FXML
    private TextFlow wrongLabelField;

    @FXML
    private TextFlow correctLabelField;

    @FXML
    private Label correctLabel;

    @FXML
    private Label wrongLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Menu menuLang;

    @FXML
    private MenuItem menuLangEnglish;

    @FXML
    private MenuItem menuLangPolish;

    @FXML
    private void initialize() {
        initLabels();
        universalButton.setOnAction(event -> {
            try {
                handleButtonClick();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        menuLangEnglish.setOnAction(event -> changeLanguage("en", "US"));
        menuLangPolish.setOnAction(event -> changeLanguage("pl", "PL"));
    }

    private void changeLanguage(String languageCode, String countryCode) {
        Locale newLocale = new Locale(languageCode, countryCode);
        bundle = ResourceBundle.getBundle("UIBundle", newLocale);
        questionBuilder = new QuestionBuilder(newLocale, requester);
        initLabels();
    }

    private void showAnswerLabel(boolean isAnswerCorrect) {
        universalButton.setVisible(false);

        if (isAnswerCorrect) {
            correctLabel.setText(bundle.getString("correct_answer") + " " + currentQuestion.getAnswerText());
            correctLabelField.setVisible(true);
        } else {
            wrongLabel.setText(bundle.getString("wrong_answer") + " " + currentQuestion.getAnswerText());
            wrongLabelField.setVisible(true);
        }

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(2), e -> {
                    progressBar.setProgress(progressBar.getProgress() + 0.001);
                })
        );
        timeline.setCycleCount(1000);
        timeline.setOnFinished(e -> {
            correctLabelField.setVisible(false);
            wrongLabelField.setVisible(false);
            universalButton.setVisible(true);
            currentQuestion = questionBuilder.getQuestion();
            questionLabel.setText(currentQuestion.getQuestionText());
            progressBar.setProgress(0);
        });
        timeline.play();
    }

    private void handleButtonClick() throws InterruptedException {
        if (QUIZ_STARTED && currentQuestion != null) {
            int answer = Integer.parseInt(answerField.getText().replaceAll("\\p{Punct}", ""));
            showAnswerLabel(answer == currentQuestion.getAnswerValue());
        } else {
            QUIZ_STARTED = true;
            universalButton.setText(bundle.getString("button_next_prompt"));
            currentQuestion = questionBuilder.getQuestion();
            questionLabel.setText(currentQuestion.getQuestionText());
        }

    }

    private void initLabels() {
        questionPromptLabel.setText(bundle.getString("question_prompt"));
        questionLabel.setText(bundle.getString("question_label_prompt"));
        answerField.setPromptText(bundle.getString("answer_prompt"));
        universalButton.setText(bundle.getString("button_start_prompt"));
        progressLabel.setText(bundle.getString("progress_label"));
        correctLabel.setText(bundle.getString("correct_answer"));
        wrongLabel.setText(bundle.getString("wrong_answer"));
        menuLang.setText(bundle.getString("menu_lang"));
        menuLangPolish.setText(bundle.getString("menu_lang_polish"));
        menuLangEnglish.setText(bundle.getString("menu_lang_english"));

        QUIZ_STARTED = false;

        universalButton.setVisible(true);
        correctLabelField.setVisible(false);
        wrongLabelField.setVisible(false);
    }
}

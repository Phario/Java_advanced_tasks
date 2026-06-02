package pl.pwr.ite.dynak.requester.util;

import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.requester.enums.QuestionType;
import pl.pwr.ite.dynak.requester.interfaces.IQuestionBuilder;
import pl.pwr.ite.dynak.requester.interfaces.IRequester;
import pl.pwr.ite.dynak.requester.models.CharacterData;
import pl.pwr.ite.dynak.requester.models.PlanetData;
import pl.pwr.ite.dynak.requester.models.Question;
import pl.pwr.ite.dynak.requester.models.StarshipData;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionBuilder implements IQuestionBuilder {

    private Locale locale;
    private IRequester requester;
    private Random random = new Random();
    private ResourceBundle bundle;
    private String bundleName = "Bundle";

    public QuestionBuilder(Locale locale, IRequester requester) {
        this.locale = locale;
        this.requester = requester;
        this.bundle = ResourceBundle.getBundle(bundleName, locale);
    }

    @Override
    public Question getQuestion() {
        Question question = null;

        int randomInt = random.nextInt(QuestionType.values().length);

        if (randomInt == 0) question = createHeightQuestion();
        else if (randomInt == 1) question = createStarshipQuestion();
        else if (randomInt == 2) question = createPlanetQuestion();
        else throw new IllegalStateException("Invalid question type generated");

        if (question == null) {
            throw new IllegalStateException("Failed to generate a valid question");
        }

        return question;
    }

    private Question createHeightQuestion() {
        CharacterData character = requester.getRandomCharacterData();

        int genderNum = 0;

        String gender = character.getGender().toLowerCase();

        if (gender.equals("male")) genderNum = 0;
        else if (gender.equals("female")) genderNum = 1;
        else genderNum = 2;

        String questionText = MessageFormat.format(
                bundle.getString("height_question"),
                character.getName(),
                genderNum
        );

        String answerKey;
        int height = Integer.parseInt(character.getHeight());
        int lastDigit = height % 10;
        int lastTwoDigits = height % 100;

        if (height == 1) {
            answerKey = "height_answer_1";
        } else if (lastTwoDigits >= 12 && lastTwoDigits <= 14) {
            answerKey = "height_answer_other";
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            answerKey = "height_answer_2_4";
        } else {
            answerKey = "height_answer_other";
        }

        String answerText = MessageFormat.format(
                bundle.getString(answerKey),
                character.getName(),
                height,
                genderNum
        );

        return new Question(questionText, answerText, height);
    }

    private Question createStarshipQuestion() {
        StarshipData starship = requester.getRandomStarshipData();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(bundleName, locale);

        String questionText = java.text.MessageFormat.format(
                bundle.getString("passenger_question"),
                starship.getName()
        );

        String answerKey;
        int crew = Integer.parseInt(starship.getCrewCapacity().replaceAll("\\p{Punct}", ""));

        if (crew == 1) {
            answerKey = "passenger_answer_1";
        } else {
            answerKey = "passenger_answer_other";
        }

        String answerText = java.text.MessageFormat.format(
                bundle.getString(answerKey),
                starship.getName(),
                crew
        );

        return new Question(questionText, answerText, crew);
    }

    private Question createPlanetQuestion() {
        PlanetData planet = requester.getRandomPlanetData();

        ResourceBundle bundle = java.util.ResourceBundle.getBundle(bundleName, locale);

        String questionText = java.text.MessageFormat.format(
                bundle.getString("planet_rotation_question"),
                planet.getName()
        );

        String answerKey;
        int rotation = Integer.parseInt(planet.getRotationPeriod());
        int lastDigit = rotation % 10;
        int lastTwoDigits = rotation % 100;

        if (rotation == 1) {
            answerKey = "planet_rotation_answer_1";
        } else if (lastTwoDigits >= 12 && lastTwoDigits <= 14) {
            answerKey = "planet_rotation_answer_other";
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            answerKey = "planet_rotation_answer_2_4";
        } else {
            answerKey = "planet_rotation_answer_other";
        }

        String answerText = java.text.MessageFormat.format(
                bundle.getString(answerKey),
                planet.getName(),
                rotation
        );

        return new Question(questionText, answerText, rotation);
    }
}

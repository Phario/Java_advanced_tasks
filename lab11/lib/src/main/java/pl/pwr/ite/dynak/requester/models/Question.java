package pl.pwr.ite.dynak.requester.models;

import lombok.Getter;

@Getter
public class Question {
    private String questionText;
    private String answerText;
    private int answerValue;

    public Question(String questionText, String answerText, int answerValue) {
        this.questionText = questionText;
        this.answerText = answerText;
        this.answerValue = answerValue;
    }
}

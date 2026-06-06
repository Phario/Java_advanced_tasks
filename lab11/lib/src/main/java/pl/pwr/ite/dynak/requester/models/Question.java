package pl.pwr.ite.dynak.requester.models;

import lombok.Getter;

public class Question {
    private String questionText;
    private String answerText;
    private int answerValue;

    public Question(String questionText, String answerText, int answerValue) {
        this.questionText = questionText;
        this.answerText = answerText;
        this.answerValue = answerValue;
    }

    public String getAnswerText() {
        return answerText;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public String getQuestionText() {
        return questionText;
    }
}

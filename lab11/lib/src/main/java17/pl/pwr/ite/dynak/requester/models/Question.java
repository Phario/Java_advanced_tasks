package pl.pwr.ite.dynak.requester.models;

public record Question(
        String questionText,
        String answerText,
        int answerValue
) {}
package com.example.huanhm.duolingoclone.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class  Question {
    @SerializedName("question")
    @Expose
    public String question;

    @SerializedName("answer_a")
    @Expose
    private String answerA;

    @SerializedName("answer_b")
    @Expose
    private String answerB;

    @SerializedName("answer_c")
    @Expose
    private String answerC;

    @SerializedName("answer_d")
    @Expose
    private String answerD;

    public ArrayList<String> answers = new ArrayList<>();

    @SerializedName("correct_answer")
    @Expose
    public String correct;

    public void groupIntoAnswerList() {
        answers.add(answerA);
        answers.add(answerB);
        answers.add(answerC);
        answers.add(answerD);

        switch (correct) {
            case "A":
                correct = answerA;
                break;
            case "B":
                correct = answerB;
                break;
            case "C":
                correct = answerC;
                break;
            case "D":
                correct = answerD;
                break;
            default:
                correct = "Không có đáp án";
        }
    }
}

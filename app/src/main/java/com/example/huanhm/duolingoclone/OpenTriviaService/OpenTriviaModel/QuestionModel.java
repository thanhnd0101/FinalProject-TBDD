package com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaModel;

import android.os.Build;
import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel {
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("question")
    private String question;
    @SerializedName("correct_answer")
    private String correct_answer;
    @SerializedName("incorrect_answers")
    private List<String> incorrect_answers;

    public QuestionModel(String difficulty,String question, String correct_answer, List<String> incorrect_answer) {
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answer;
    }

    public String getQuestion() {
        return Html.fromHtml(question,Build.VERSION.SDK_INT).toString();
    }

    public String getDifficulty() {
        return  difficulty;
    }

    public String getCorrect_answer() {
        return Html.fromHtml(correct_answer,Build.VERSION.SDK_INT).toString();
    }

    public List<String> getIncorrect_answer() {
        List<String> outIncorrect_answers = new ArrayList<>();
        for(int i=0;i< incorrect_answers.size();++i){
            outIncorrect_answers.add(Html.fromHtml(incorrect_answers.get(i),Build.VERSION.SDK_INT).toString());
        }
        return outIncorrect_answers;
    }

}

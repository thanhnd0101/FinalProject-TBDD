package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.example.huanhm.duolingoclone.DataModel.Question;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionList {
    @SerializedName("results")
    @Expose
    public ArrayList<Question> questions;

    public void parseQuestions() {
        for(Question q : questions) {
            q.groupIntoAnswerList();
        }
    }
}

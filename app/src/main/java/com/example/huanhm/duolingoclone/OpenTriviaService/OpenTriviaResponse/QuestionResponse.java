package com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaResponse;

import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaModel.QuestionModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionResponse {
    @SerializedName("results")
    private ArrayList<QuestionModel> questionModelList;

    public ArrayList<QuestionModel> getQuestionModelList() {
        return questionModelList;
    }
}

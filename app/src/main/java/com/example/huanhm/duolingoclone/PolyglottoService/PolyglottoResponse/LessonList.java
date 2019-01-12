package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.example.huanhm.duolingoclone.DataModel.Lesson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LessonList {
    @SerializedName("results")
    @Expose
    public ArrayList<Lesson> lessons;
}

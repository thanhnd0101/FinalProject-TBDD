package com.example.huanhm.duolingoclone.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lesson {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("lesson")
    @Expose
    public String name;

    @SerializedName("score")
    @Expose
    public int score;

    public Lesson() {
        this.name = "Default lesson";
    }

    public Lesson(String name) {
        this.name = name;
    }
}

package com.example.huanhm.duolingoclone.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Topic {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("topic")
    @Expose
    public String name;

    public ArrayList<Lesson> lessons = new ArrayList<>();

    public Topic() {
        this.name = "Default Topic";
    }

    public Topic(String name) {
        this.name = name;
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }
}

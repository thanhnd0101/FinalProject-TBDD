package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.example.huanhm.duolingoclone.DataModel.Topic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TopicList {
    @SerializedName("results")
    @Expose
    public ArrayList<Topic> topics;
}

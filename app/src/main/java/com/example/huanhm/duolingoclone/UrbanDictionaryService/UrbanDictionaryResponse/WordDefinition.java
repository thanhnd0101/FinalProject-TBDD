package com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WordDefinition implements Serializable {
    @SerializedName("definition")
    private String definition;
    @SerializedName("example")
    private String example;
    @SerializedName("word")
    private String word;

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public String getWord() {
        return word;
    }
}

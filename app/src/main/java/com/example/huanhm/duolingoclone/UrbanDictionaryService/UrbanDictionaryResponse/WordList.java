package com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WordList implements Serializable {
    @SerializedName("list")
    private List<WordDefinition> list;

    public List<WordDefinition> getList() {
        return list;
    }
}

package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status implements Serializable {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}

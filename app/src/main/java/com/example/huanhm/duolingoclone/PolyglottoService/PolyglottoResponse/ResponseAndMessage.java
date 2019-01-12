package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAndMessage {
    @SerializedName("response")
    @Expose
    public String response;

    @SerializedName("message")
    @Expose
    public String message;
}

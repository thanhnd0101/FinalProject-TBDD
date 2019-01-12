package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("email")
    @Expose
    public String email;
}

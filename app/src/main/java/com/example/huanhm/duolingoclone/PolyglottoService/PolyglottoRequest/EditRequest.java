package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EditRequest implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("email")
    private String email;

    public EditRequest(String username, String phonenumber, String email) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.email = email;
    }

}

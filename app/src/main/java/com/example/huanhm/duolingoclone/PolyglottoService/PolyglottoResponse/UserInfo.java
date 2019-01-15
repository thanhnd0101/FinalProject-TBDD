package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("email")
    private String email;
    @SerializedName("achievements")
    private List<Integer> achievements;
    @SerializedName("new_account")
    private boolean new_account;

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getAchievements() {
        return achievements;
    }

    public boolean isNew_account() {
        return new_account;
    }
}

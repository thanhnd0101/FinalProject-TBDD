package com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("phonenuber")
    private String phonenuber;
    @SerializedName("email")
    private String email;
    @SerializedName("achievements")
    private List<Integer> achievements;

    public String getUsername() {
        return username;
    }

    public String getPhonenuber() {
        return phonenuber;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getAchievements() {
        return achievements;
    }
}

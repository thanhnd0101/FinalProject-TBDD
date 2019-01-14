package com.example.huanhm.duolingoclone.PolyglottoService;

import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.EditRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.LoginRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.RegisterRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.LessonList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.QuestionList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.Status;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.TopicList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PolyglottoService {
    private static Retrofit retrofit;
    private static PolyglottoAPI api;
    private static PolyglottoService service;


    protected PolyglottoService() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://polyglotto.herokuapp.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PolyglottoAPI.class);
    }

    private static PolyglottoService getInstance() {
        if (service == null) {
            service = new PolyglottoService();
        }
        return service;
    }

    public static Call<UserInfo> getUserInfo(String accessToken,String userid){
        getInstance();
        return api.getUserInfo(accessToken,userid);
    }
    public static Call<UserInfo> getUserDefault(){
        getInstance();
        return api.getUserInfoDefault();
    }
    public static Call<String> postModifiedUserInfo(String access, String userid, EditRequest editRequest){
        getInstance();
        return api.postModifyUserInfo(access,userid,editRequest);
    }

    public static Call<String> postUserAchievement(String access,String userid,int achievementid,String hmac){
        getInstance();
        return api.postAchievement(access,userid,achievementid,hmac);
    }
}

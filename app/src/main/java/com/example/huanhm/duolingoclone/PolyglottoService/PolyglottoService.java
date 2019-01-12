package com.example.huanhm.duolingoclone.PolyglottoService;

import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.LoginRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.RegisterRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.LessonList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.QuestionList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.TopicList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolyglottoService {
    private static Retrofit retrofit;
    private static PolyglottoAPI api;
    private static PolyglottoService service;

    protected PolyglottoService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://thevncore-lab.mooo.com:12904/")
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

    public static Call<ResponseAndMessage> login(LoginRequest request) {
        return getInstance().api.login(request);
    }

    public static Call<ResponseAndMessage> checkuser(String username) {
        return getInstance().api.checkuser(username);
    }

    public static Call<ResponseAndMessage> checkemail(String email) {
        return getInstance().api.checkemail(email);
    }

    public static Call<ResponseAndMessage> register(RegisterRequest request) {
        return getInstance().api.register(request);
    }

    public static Call<TopicList> getTopicList() {
        return getInstance().api.getTopicList();
    }

    public static Call<LessonList> getLessonList(int id) {
        return getInstance().api.getLessonList(id);
    }

    public static Call<QuestionList> getQuestionList(int id) {
        return getInstance().api.getQuestionList(id);
    }
}

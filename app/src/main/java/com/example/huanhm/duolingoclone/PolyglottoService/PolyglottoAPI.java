package com.example.huanhm.duolingoclone.PolyglottoService;

import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.LoginRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.RegisterRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.LessonList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.QuestionList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.TopicList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PolyglottoAPI {
    @POST("login")
    Call<ResponseAndMessage> login(@Body LoginRequest request);

    @GET("checkuser/{username}")
    Call<ResponseAndMessage> checkuser(@Path("username") String username);

    @GET("checkemail/{email}")
    Call<ResponseAndMessage> checkemail(@Path("email") String email);

    @POST("register")
    Call<ResponseAndMessage> register(@Body RegisterRequest request);

    @GET("chude/all")
    Call<TopicList> getTopicList();

    @GET("baihoc/all/{idbaihoc}")
    Call<LessonList> getLessonList(@Path("idbaihoc") int id);

    @GET("cauhoi")
    Call<QuestionList> getQuestionList(@Query("baihoc") int id);
}

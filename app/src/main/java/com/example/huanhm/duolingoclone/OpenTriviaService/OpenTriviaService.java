package com.example.huanhm.duolingoclone.OpenTriviaService;

import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaResponse.QuestionResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenTriviaService {
    private static final String BASE_URL="https://opentdb.com/";
    private static Retrofit retrofit;
    private static OpenTriviaAPI api;
    private static OpenTriviaService service;

    private OpenTriviaService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenTriviaAPI.class);
    }

    public static OpenTriviaService getOpenTriviaServiceInstance() {
        if(service == null){
            service = new OpenTriviaService();
        }
        return service;
    }

    public static Call<QuestionResponse> getQuestionsWithAmountOf(int amount,int category,String diff,String type){
        getOpenTriviaServiceInstance();
        return api.getQuestions(amount,category,diff,type);
    }
}

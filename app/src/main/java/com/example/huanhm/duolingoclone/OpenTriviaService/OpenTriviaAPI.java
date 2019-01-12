package com.example.huanhm.duolingoclone.OpenTriviaService;

import com.example.huanhm.duolingoclone.OpenTriviaService.OpenTriviaResponse.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenTriviaAPI {
    @GET("api.php")
    Call<QuestionResponse> getQuestions(
            @Query("amount") int amount,
            @Query("category")int category,
            @Query("difficulty")String difficulty,
            @Query("type")String type);

}

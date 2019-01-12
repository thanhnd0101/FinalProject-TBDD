package com.example.huanhm.duolingoclone.UrbanDictionaryService;

import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse.WordList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UrbanDictionaryAPI {

    @Headers({
            "X-RapidAPI-Key: Q8B4q6y4Z6mshoohwajZuDZ0ZvwOp1YVZXVjsnfQkjbxqQRT6u"
    })
    @GET("/define")
    Call<WordList> getDefinition(@Query("term") String term);
}

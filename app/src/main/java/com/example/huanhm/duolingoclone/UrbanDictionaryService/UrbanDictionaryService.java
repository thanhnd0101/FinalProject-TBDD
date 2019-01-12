package com.example.huanhm.duolingoclone.UrbanDictionaryService;

import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse.WordList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UrbanDictionaryService {
    private static Retrofit retrofit;
    private static UrbanDictionaryAPI api;
    private static UrbanDictionaryService service;

    protected UrbanDictionaryService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://mashape-community-urban-dictionary.p.rapidapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(UrbanDictionaryAPI.class);
    }
    private static UrbanDictionaryService getInstance() {
        if (service == null) {
            service = new UrbanDictionaryService();
        }
        return service;
    }
    public static Call<WordList> getDefinition(String term) {
        return getInstance().api.getDefinition(term);
    }
}

package com.example.huanhm.duolingoclone.PolyglottoService;


import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.EditRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface PolyglottoAPI {

    @GET("/user/{userid}/info")
    Call<UserInfo> getUserInfo(@Header("access-token") String accessToken, @Path("userid") String userid);

    @Headers({"access-token: POLYGLOTTO000"})
    @GET("/user/000/info")
    Call<UserInfo> getUserInfoDefault();

    @POST("/user/{userid}/edit/all")
    Call<String> postModifyUserInfo(@Header("access-token") String accessToken,@Path("userid") String userid, @Body EditRequest editRequest);

    @POST("/user/{userid}/unlock/{achievementid}")
    Call<String> postAchievement(@Header("access-token") String accessToken,@Path("userid") String userid,@Path("achievementid") int achievementid, @Body String hmac);
}

package com.example.huanhm.duolingoclone.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.EditRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.LoginRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.UserInfo;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity {

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginButton loginButton;
    public static AccessToken accessToken = AccessToken.getCurrentAccessToken();
    public static Profile profile = Profile.getCurrentProfile();

    public static UserInfo userInfo;
    public static UserInfo userInfoDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.END));
        getWindow().setExitTransition(new Slide(Gravity.START));

        setContentView(R.layout.activity_login);

        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(this);

        if (checkFacebookLoginStatus()){
            LoginManager.getInstance().logOut();
        }
        //Initialize layout and set up events
        initFacebookLoginLayout();
        initFacebookLoginEvents();
    }

    private void loginToServer() {
        Call<UserInfo> call = PolyglottoService.getUserInfo(accessToken.getToken(),accessToken.getUserId());

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                if(response.isSuccessful()){
                    LoginActivity.userInfo = response.body();
                    if(LoginActivity.userInfo.isNew_account()){
                        upLoadUserInfo();
                    }else {
                        toDashboardActivity();
                        Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String status;
    private void upLoadUserInfo() {
        EditRequest editRequest = new EditRequest(profile.getName(),
                "", "");
        Call<String> call = PolyglottoService.postModifiedUserInfo(LoginActivity.accessToken.getToken()
                ,profile.getId(),editRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    status = response.body();
                    if(Objects.requireNonNull(status).equals("success")){
                        Toast.makeText(getApplicationContext(), "Upload info successfuly", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(DashboardActivity.viewPager.getAdapter()).notifyDataSetChanged();
                        toDashboardActivity();
                    }else{
                        Toast.makeText(getApplicationContext(), "Upload info failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Upload info failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        helper.finish();
        //super.onBackPressed();
    }

    private void initFacebookLoginLayout(){
        loginButton = findViewById(R.id.login_fb_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        callbackManager = CallbackManager.Factory.create();
    }
    private boolean checkFacebookLoginStatus(){
        accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }
    private void initFacebookLoginEvents(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            profile = Profile.getCurrentProfile();
                            /*Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
                            toDashboardActivity();*/
                            loginToServer();
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    profile = Profile.getCurrentProfile();
                    loginToServer();
                    /*Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
                    toDashboardActivity();*/
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void toDashboardActivity() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        SwipeBackActivityHelper.startSwipeActivity(LoginActivity.this, intent, true, true, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void toLoginDefault() {
        Call<UserInfo> call = PolyglottoService.getUserDefault();
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
                if(response.isSuccessful()){
                    LoginActivity.userInfoDefault = response.body();
                    Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
                    toDashboardActivity();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doCaptcha(View view) {
        SafetyNet.getClient(this).verifyWithRecaptcha("6LcKkoIUAAAAANjWWMJri50ehKyT9As_vekdv0wK")
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            handleSiteVerify(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    private void handleSiteVerify(String tokenResult) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=6LcKkoIUAAAAAGr7V7qaXuaymoghBnqXJWcflTbt&response=" + tokenResult;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                toLoginDefault();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
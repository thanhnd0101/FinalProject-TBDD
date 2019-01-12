package com.example.huanhm.duolingoclone.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.LoginRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginButton loginButton;
    public static AccessToken accessToken = AccessToken.getCurrentAccessToken();
    public static Profile profile = Profile.getCurrentProfile();

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

        /*if (checkFacebookLoginStatus()){
            toDashboardActivity();
        }
        //Initialize layout and set up events
        initFacebookLoginLayout();
        initFacebookLoginEvents();*/
    }

    @Override
    public void onBackPressed() {
        helper.finish();
        //super.onBackPressed();
    }

    /*public void login(View view) {
        String username = ((EditText)findViewById(R.id.username_login)).getText().toString();
        String password = ((EditText)findViewById(R.id.password_login)).getText().toString();
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;

        Call<ResponseAndMessage> call = PolyglottoService.login(request);
        call.enqueue(new Callback<ResponseAndMessage>() {
            @Override
            public void onResponse(Call<ResponseAndMessage> call, Response<ResponseAndMessage> response) {
                try {
                    ResponseAndMessage respObj = response.body();
                    if (respObj.response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        SwipeBackActivityHelper.startSwipeActivity(LoginActivity.this, intent, true, true, false);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sai mật khẩu hoặc tên đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu hoặc tên đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAndMessage> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Đã xảy ra sự cố, vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void toForgetPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        SwipeBackActivityHelper.startSwipeActivity(this, intent, true, true, false);
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
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
                toDashboardActivity();
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
}

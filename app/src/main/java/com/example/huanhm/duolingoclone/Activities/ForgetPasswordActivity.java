package com.example.huanhm.duolingoclone.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huanhm.duolingoclone.R;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class ForgetPasswordActivity extends Activity {

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();
    private EditText email;
    private boolean emailOk = false;
    private Button captcha;
    private boolean captchaOk = false;
    Drawable warning;
    Drawable ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.END));

        setContentView(R.layout.activity_forget_password);

        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(this);

        warning = getDrawable(R.drawable.ic_warning);
        warning.setBounds(0, 0, warning.getIntrinsicWidth(), warning.getIntrinsicHeight());

        ok = getDrawable(R.drawable.ic_ok_tick);
        ok.setBounds(0, 0, ok.getIntrinsicWidth(), ok.getIntrinsicHeight());

        email = findViewById(R.id.email_forget);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = Patterns.EMAIL_ADDRESS.matcher(s).matches();
                if (!valid) {
                    email.setError("Địa chỉ mail không hợp lệ", warning);
                    emailOk = false;
                } else {
                    email.setError(null);
                    emailOk = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        captcha = findViewById(R.id.captcha_forget);
    }

    @Override
    public void onBackPressed() {
        helper.finish();
        //super.onBackPressed();
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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (!json.getBoolean("success")) {
                                captcha.setError("Captcha không hợp lệ", warning);
                                captchaOk = false;
                            } else {
                                captcha.setError("", ok);
                                captchaOk = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void confirmEmail(View view) {
        if (!emailOk) {
            if (email.getError() == null)
                email.setError("Địa chỉ mail không hợp lệ", warning);
            email.requestFocus();
        } else if (!captchaOk) {
            if (captcha.getError() == null)
                captcha.setError("Vui lòng nhập Captcha", warning);
            captcha.requestFocus();
        }
        else {
            Toast.makeText(this, "Gửi thông tin", Toast.LENGTH_SHORT).show();
            //email.setVisibility(View.GONE);
            //captcha.setVisibility(View.GONE);
//            email.animate().alpha(0.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    email.setVisibility(View.INVISIBLE);
//                }
//            });
//            captcha.animate().alpha(0.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    captcha.setVisibility(View.INVISIBLE);
//                }
//            });
//            final View overlay = findViewById(R.id.overlay_forget);
//            overlay.animate().alpha(1.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    overlay.setVisibility(View.VISIBLE);
//                }
//            });
//            ((Button)findViewById(R.id.confirm_forget)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    backToLogin(v);
//                }
//            });
            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Thành công")
                    .setContentText("Một email đặt lại mật khẩu đã được gửi đến địa chỉ mail của bạn. Vui lòng làm theo hướng dẫn để đặt lại mật khẩu")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            backToLogin(null);
                        }
                    });
            dialog.show();
        }
    }

    public void backToLogin(View view) {
        helper.finish();
    }
}

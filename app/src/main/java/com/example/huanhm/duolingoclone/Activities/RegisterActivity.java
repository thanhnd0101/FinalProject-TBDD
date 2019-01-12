package com.example.huanhm.duolingoclone.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.RegisterRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.ResponseAndMessage;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();
    EditText passEdit;
    TextWatcher passWatcher;
    boolean passOk = false;

    EditText userEdit;
    TextWatcher userWatcher;
    boolean userOk = false;

    EditText passAgainEdit;
    TextWatcher passwordAgainWatcher;
    boolean passAgainOk = false;
    Zxcvbn strengthEstimator = new Zxcvbn();

    EditText emailEdit;
    TextWatcher emailWatcher;
    boolean emailOk = false;

    Drawable warning;

    View.OnFocusChangeListener userEditFinishListener;
    View.OnFocusChangeListener emailEditFinishListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.END));

        setContentView(R.layout.activity_register);

        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(this);

        passEdit = findViewById(R.id.password_register);
        userEdit = findViewById(R.id.username_register);
        passAgainEdit = findViewById(R.id.password_register_again);
        emailEdit = findViewById(R.id.email_register);

        warning = getDrawable(R.drawable.ic_warning);
        warning.setBounds(0, 0, warning.getIntrinsicWidth(), warning.getIntrinsicHeight());

        passWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5 || s.length() > 20) {
                    passEdit.setBackground(getDrawable(R.drawable.roundbutton_input));
                    passEdit.setError("Mật khẩu phải từ 5 đến 20 ký tự", warning);
                    passOk = false;
                } else {
                    passEdit.setError(null);
                    int strength = strengthEstimator.measure(s.toString()).getScore();
                    if (strength < 2) {
                        passEdit.setError("Mật khẩu quá yếu", null);
                        passEdit.setBackground(getDrawable(R.drawable.roundbutton_input_invalid));
                        passOk = false;
                    } else {
                        passEdit.setError(null);
                        if (strength < 3) {
                            passEdit.setBackground(getDrawable(R.drawable.roundbutton_input_justfine));
                        } else {
                            passEdit.setBackground(getDrawable(R.drawable.roundbutton_input_ok));
                        }
                        passOk = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = passAgainEdit.getText().toString();
                if (!pass.equals(s.toString())) {
                    passAgainEdit.setError("Mật khẩu không khớp", warning);
                    passAgainOk = false;
                } else {
                    passAgainEdit.setError(null);
                    passAgainOk = true;
                }
            }
        };

        userWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5 || s.length() > 20) {
                    userEdit.setError("Tên đăng nhập phải từ 5 đến 20 ký tự", warning);
                    userOk = false;
                } else {
                    checkUsernameAvailable(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        passwordAgainWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = passEdit.getText().toString();
                if (!pass.equals(s.toString())) {
                    passAgainEdit.setError("Mật khẩu không khớp", warning);
                    passAgainOk = false;
                } else {
                    passAgainEdit.setError(null);
                    passAgainOk = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        emailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = Patterns.EMAIL_ADDRESS.matcher(s).matches();
                if (!valid) {
                    emailEdit.setError("Địa chỉ mail không hợp lệ", warning);
                    emailOk = false;
                } else {
                    checkEmailAvailable(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        setEditTextFocusChangeListener();

        userEditFinishListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsernameAvailable(userEdit.getText().toString());
                }
            }
        };

        emailEditFinishListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkEmailAvailable(emailEdit.getText().toString());
                }
            }
        };

        userEdit.setOnFocusChangeListener(userEditFinishListener);
        emailEdit.setOnFocusChangeListener(emailEditFinishListener);
    }

    private void checkEmailAvailable(String s) {
        Call<ResponseAndMessage> call = PolyglottoService.checkemail(s);
        call.enqueue(new Callback<ResponseAndMessage>() {
            @Override
            public void onResponse(Call<ResponseAndMessage> call, Response<ResponseAndMessage> response) {
                ResponseAndMessage body = response.body();
                if (body.message.equals("Unavailable")) {
                    emailEdit.setError("Địa chỉ mail đã được sử dụng");
                    emailOk = false;
                } else {
                    emailEdit.setError(null);
                    emailOk = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseAndMessage> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Đã xảy ra sữ cố, vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT);
                toast.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                toast.show();
            }
        });
    }

    private void checkUsernameAvailable(String s) {
        Call<ResponseAndMessage> call = PolyglottoService.checkuser(s);
        call.enqueue(new Callback<ResponseAndMessage>() {
            @Override
            public void onResponse(Call<ResponseAndMessage> call, Response<ResponseAndMessage> response) {
                ResponseAndMessage body = response.body();
                if (body.message.equals("Unavailable")) {
                    userEdit.setError("Tên đăng nhập đã được sử dụng");
                    userOk = false;
                } else {
                    userEdit.setError(null);
                    userOk = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseAndMessage> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Đã xảy ra sữ cố, vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT);
                toast.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        helper.finish();
    }

    public void backToIntro(View view) {
        helper.finish();
    }

    private void setEditTextFocusChangeListener() {
        passEdit.addTextChangedListener(passWatcher);
        userEdit.addTextChangedListener(userWatcher);
        passAgainEdit.addTextChangedListener(passwordAgainWatcher);
        emailEdit.addTextChangedListener(emailWatcher);
    }

    public void sendRegisterInfo(View view) throws InterruptedException {
        if (!userOk) {
            if (userEdit.getError() == null)
                userEdit.setError("Tên đăng nhập phải từ 5 đến 20 ký tự", warning);
            userEdit.requestFocus();
        } else if (!passOk) {
            if (passEdit.getError() == null)
                passEdit.setError("Mật khẩu phải từ 5 đến 20 ký tự", warning);
            passEdit.requestFocus();
        } else if (!passAgainOk) {
            if (passAgainEdit.getError() == null)
                passAgainEdit.setError("Mật khẩu phải từ 5 đến 20 ký tự", warning);
            passAgainEdit.requestFocus();
        } else if (!emailOk) {
            if (emailEdit.getError() == null)
                emailEdit.setError("Địa chỉ mail không hợp lệ", warning);
            emailEdit.requestFocus();
        } else {
            RegisterRequest request = new RegisterRequest();
            request.username = userEdit.getText().toString();
            request.password = passEdit.getText().toString();
            request.email = emailEdit.getText().toString();
            registerNewUser(request);
        }
    }

    private void registerNewUser(RegisterRequest request) {
        PolyglottoService.register(request).enqueue(new Callback<ResponseAndMessage>() {
            @Override
            public void onResponse(Call<ResponseAndMessage> call, Response<ResponseAndMessage> response) {
                ResponseAndMessage body = response.body();
                if (body.response.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    helper.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAndMessage> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "Đã xảy ra sữ cố, vui lòng kiểm tra kết nối Internet", Toast.LENGTH_SHORT);
                toast.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                toast.show();
            }
        });
    }
}

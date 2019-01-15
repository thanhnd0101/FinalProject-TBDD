package com.example.huanhm.duolingoclone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoRequest.EditRequest;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.facebook.login.widget.ProfilePictureView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private String userid,username,userphone,email;
    private ProfilePictureView profilePictureView;
    private MaterialButton btCancel,btValidate;
    private TextInputEditText etUserName,etUserPhone,etUserEmail;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if(LoginActivity.userInfo != null) {
            getValues();
            initLayout();
            parseValuesToViews();
            setButtonFunction();
        }
    }

    private void setButtonFunction() {
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkModifiedUserInfo()){
                    EditRequest editRequest = new EditRequest(Objects.requireNonNull(etUserName.getText()).toString(),
                            Objects.requireNonNull(etUserPhone.getText()).toString(),
                            Objects.requireNonNull(etUserEmail.getText()).toString());
                    Call<String> call = PolyglottoService.postModifiedUserInfo(LoginActivity.accessToken.getToken()
                    ,userid,editRequest);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if(response.isSuccessful()){
                                status = response.body();
                                if(Objects.requireNonNull(status).equals("success")){
                                    Toast.makeText(getApplicationContext(), "Edit successfuly", Toast.LENGTH_SHORT).show();
                                    Objects.requireNonNull(DashboardActivity.viewPager.getAdapter()).notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(getApplicationContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkModifiedUserInfo() {
        return (!Objects.requireNonNull(etUserName.getText()).toString().equals(username) ||
        !Objects.requireNonNull(etUserPhone.getText()).toString().equals(userphone) ||
        !Objects.requireNonNull(etUserEmail.getText()).toString().equals(email));
    }

    private void parseValuesToViews() {
        profilePictureView.setProfileId(userid);
        etUserName.setText(username);
        etUserPhone.setText(userphone);
        etUserEmail.setText(email);
    }

    private void getValues() {
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        username = intent.getStringExtra("username");
        userphone = intent.getStringExtra("userphone");
        email = intent.getStringExtra("email");
    }

    private void initLayout() {
        profilePictureView = findViewById(R.id.edit_profile_image);
        etUserName = findViewById(R.id.et_edit_profile_user_name);
        etUserPhone = findViewById(R.id.et_edit_profile_phone);
        etUserEmail = findViewById(R.id.et_edit_profile_email);
        btCancel = findViewById(R.id.bt_edit_profile_cancel);
        btValidate = findViewById(R.id.bt_edit_profile_validate);
    }
}

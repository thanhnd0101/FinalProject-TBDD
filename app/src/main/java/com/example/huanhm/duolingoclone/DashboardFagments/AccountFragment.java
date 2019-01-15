package com.example.huanhm.duolingoclone.DashboardFagments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huanhm.duolingoclone.Activities.EditProfileActivity;
import com.example.huanhm.duolingoclone.Activities.LoginActivity;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.UserInfo;
import com.example.huanhm.duolingoclone.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class AccountFragment extends Fragment {
    FloatingActionButton editFloatingButton;
    TextView tvProfileUserName, tvProfileEmail, tvProfilePhone;
    ProfilePictureView profileIview;
    AccessToken accessToken = LoginActivity.accessToken;
    Profile profile = LoginActivity.profile;
    UserInfo userInfo = LoginActivity.userInfo;
    private Context context;

    public AccountFragment(Context context) {
        this.context = context;
    }

    public static AccountFragment getAccountFragmentInstance(Context context){
        AccountFragment accountFragment = new AccountFragment(context);
        Bundle bundle = new Bundle();
        accountFragment.setArguments(bundle);
        accountFragment.setRetainInstance(true);
        return accountFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment_profile,container,false);
        if(LoginActivity.userInfo != null) {
            getUserInfo(accessToken);
        }
        initLayout(view);
        setEditFunction();
        return view;
    }

    private void setEditFunction() {
        editFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfileActivity();
            }
        });
    }

    private void toEditProfileActivity() {
        Intent intent = new Intent(context,EditProfileActivity.class);
        if(LoginActivity.userInfo == null){
            intent.putExtra("userid", "");
            intent.putExtra("username", "");
            intent.putExtra("userphone", "");
            intent.putExtra("email", "");
        }else {
            intent.putExtra("userid", profile.getId());
            intent.putExtra("username", tvProfileUserName.getText().toString());
            intent.putExtra("userphone", tvProfilePhone.getText().toString());
            intent.putExtra("email", tvProfileEmail.getText().toString());
        }
        SwipeBackActivityHelper.startSwipeActivity((Activity) context, intent, true, true, false);
    }

    private void getUserInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new
                GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(object!=null){
                            String email = null;
                            try {
                                email = object.getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tvProfileEmail.setText(email);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void initLayout(View view){
        tvProfilePhone = view.findViewById(R.id.tv_profile_phone);
        tvProfileUserName = view.findViewById(R.id.tv_profile_user_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        profileIview = view.findViewById(R.id.profile_image);
        editFloatingButton = view.findViewById(R.id.floatting_bt_dashboard_profile);

        if(LoginActivity.userInfoDefault != null) {
            tvProfileUserName.setText(LoginActivity.userInfoDefault.getUsername());
            if(LoginActivity.userInfoDefault.getPhonenumber()!= null){
                tvProfilePhone.setText(LoginActivity.userInfoDefault.getPhonenumber());
            }
        }else {
            tvProfilePhone.setText(userInfo.getPhonenumber());
            tvProfileUserName.setText(profile.getName());
            profileIview.setProfileId(profile.getId());
        }
    }
}
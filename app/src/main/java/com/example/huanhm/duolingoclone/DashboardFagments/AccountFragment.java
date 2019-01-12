package com.example.huanhm.duolingoclone.DashboardFagments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huanhm.duolingoclone.Activities.LoginActivity;
import com.example.huanhm.duolingoclone.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class AccountFragment extends Fragment {
    TextView tvProfileFirstName, tvProfileEmail,tvProfileLastName;
    ProfilePictureView profileIview;
    AccessToken accessToken = LoginActivity.accessToken;
    Profile profile = LoginActivity.profile;
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
        getUserInfo(accessToken);
        initLayout(view);
        return view;
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
        tvProfileFirstName = view.findViewById(R.id.tv_profile_first_name);
        tvProfileLastName = view.findViewById(R.id.tv_profile_first_last_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        profileIview = view.findViewById(R.id.profile_image);

        tvProfileFirstName.setText(profile.getFirstName());
        tvProfileLastName.setText(profile.getLastName());
        profileIview.setProfileId(profile.getId());
    }
}
package com.example.huanhm.duolingoclone.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.R;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;

public class IntroActivity extends Activity {

    private int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Slide(Gravity.START));
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        backButtonCount = 0;
    }

    public void toSignInActivity(View view) {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

        SwipeBackActivityHelper.startSwipeActivity(this, intent, true, true, false);
    }

    public void toRegisterActivity(View view) {
        Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);

        SwipeBackActivityHelper.startSwipeActivity(this, intent, true, true, false);
    }

    @Override
    public void onBackPressed() {
        if(backButtonCount >= 1)
        {
            finishAndRemoveTask();
        }
        else
        {
            Toast toast = Toast.makeText(this, "Nhấn quay về 1 lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();
            backButtonCount++;
        }
    }
}

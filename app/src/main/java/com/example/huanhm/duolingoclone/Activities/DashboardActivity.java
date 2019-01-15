package com.example.huanhm.duolingoclone.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.DashboardAdapter.PagerAdapter;
import com.example.huanhm.duolingoclone.DashboardFagments.AccountFragment;
import com.example.huanhm.duolingoclone.DashboardFagments.AchievementFagment;
import com.example.huanhm.duolingoclone.DashboardFagments.LearningFragment;
import com.example.huanhm.duolingoclone.DashboardFagments.SearchingFragment;
import com.example.huanhm.duolingoclone.R;
import com.facebook.login.LoginManager;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {


    public static final int TOPIC_MARGIN_TOP = 256;
    public static final int TOPIC_MARGIN_LEFT = 64;
    public static final int LESSON_MARGIN_TOP = 128;

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.END));
        getWindow().setExitTransition(new Slide(Gravity.START));

        setContentView(R.layout.activity_dashboard);

        viewPager = findViewById(R.id.viewpager_dashboard);
        tabLayout = findViewById(R.id.tab_layout_dashboard);

        pagerAdapter.addFragment(LearningFragment.getLearningFragmentInstance(DashboardActivity.this),"Học");
        pagerAdapter.addFragment(AchievementFagment.getAchievementFagmentInstance(DashboardActivity.this),"Thành tích");
        pagerAdapter.addFragment(SearchingFragment.getSearchingFragmentInstance(DashboardActivity.this),"Từ điển");
        pagerAdapter.addFragment(AccountFragment.getAccountFragmentInstance(DashboardActivity.this),"Tài khoản");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for(int i=0;i<tabLayout.getTabCount();++i){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(DashboardActivity.this,i));
        }

        helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(DashboardActivity.this);
        Toolbar myToolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Tùy chỉnh", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Đăng xuất", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
                toLoginActivity();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void toLoginActivity() {
        helper.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        int chosen = ((LearningFragment)pagerAdapter.getFragment(0)).chosenTopic;
        boolean animating = ((LearningFragment)pagerAdapter.getFragment(0)).isAnimating;

        if (chosen < 0 && !animating) {
            LoginManager.getInstance().logOut();
            helper.finish();
        }
        else
            ((LearningFragment)pagerAdapter.getFragment(0)).goTobackToTopicList();
    }

}

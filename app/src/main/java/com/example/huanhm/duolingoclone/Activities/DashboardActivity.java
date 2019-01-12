package com.example.huanhm.duolingoclone.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.CustomViews.ConnectLineView;
import com.example.huanhm.duolingoclone.CustomViews.LessonNode;
import com.example.huanhm.duolingoclone.CustomViews.TopicNode;
import com.example.huanhm.duolingoclone.DashboardAdapter.PagerAdapter;
import com.example.huanhm.duolingoclone.DashboardFagments.AccountFragment;
import com.example.huanhm.duolingoclone.DashboardFagments.LearningFragment;
import com.example.huanhm.duolingoclone.DashboardFagments.SearchingFragment;
import com.example.huanhm.duolingoclone.DataModel.Lesson;
import com.example.huanhm.duolingoclone.DataModel.Topic;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.LessonList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.TopicList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.facebook.login.LoginManager;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    /*ScrollView scroll;
    ArrayList<Topic> topics = new ArrayList<>();
    ArrayList<TopicNode> nodeList = new ArrayList<>();
    ArrayList<ConnectLineView> lines = new ArrayList<>();
    ArrayList<View.OnTouchListener> listeners = new ArrayList<>();
    ConstraintLayout topicLayout;
    int chosenTopic = -1;*/

    public static final int TOPIC_MARGIN_TOP = 256;
    public static final int TOPIC_MARGIN_LEFT = 64;
    public static final int LESSON_MARGIN_TOP = 128;

    SwipeBackActivityHelper helper = new SwipeBackActivityHelper();
    /*private LearningFragment learningFragment = new LearningFragment();
    private AccountFragment accountFragment = new AccountFragment();*/

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    loadFragment(learningFragment);
                    if (learningFragment.chosenTopic >= 0) {
                        learningFragment.backToTopicList();
                    }
                    break;
                case R.id.navigation_account:
                    loadFragment(accountFragment);
                    return true;
            }
            return false;
        }
    };*/
    PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
    ViewPager viewPager;
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
        /*scroll = findViewById(R.id.scrollview_dashboard);
        topicLayout = findViewById(R.id.topic_layout_dashboard);
        try {
            getTopicsList();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //testBuild();
        */
        Toolbar myToolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        /*BottomNavigationView navigation = findViewById(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }
   /* private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }*/
    /*private void backToTopicList() {
        final ConstraintSet constraintSet = new ConstraintSet();
        final TopicNode node = nodeList.get(chosenTopic);
        final ConnectLineView line = lines.get(chosenTopic);

        node.setEnabled(true);
        node.hideLessonList();

        line.animate()
                .alpha(0.0f)
                .setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        line.setVisibility(View.GONE);
                    }
                });

        int deltaY = DashboardActivity.TOPIC_MARGIN_TOP + TopicNode.radius * 2;
        AdditiveAnimator nodeMover = new AdditiveAnimator().target(node).topMarginBy(chosenTopic * deltaY);
        nodeMover.setDuration(500 + 50 * chosenTopic);
        nodeMover.setInterpolator(new AccelerateDecelerateInterpolator());
        final int tempChosen = chosenTopic;
        chosenTopic = -1;

        nodeMover.addListener(new AnimatorListenerAdapter() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onAnimationEnd(Animator animation) {
                scroll.smoothScrollTo(0, node.getTop());

                if (tempChosen > 0) {
                    line.firstNode = nodeList.get(tempChosen - 1);

                    constraintSet.clone(topicLayout);
                    constraintSet.connect(line.getId(), ConstraintSet.TOP, line.firstNode.getId(), ConstraintSet.BOTTOM, 0);
                    constraintSet.connect(line.getId(), ConstraintSet.BOTTOM, line.secondNode.getId(), ConstraintSet.TOP, 0);
                    constraintSet.connect(line.getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, DashboardActivity.TOPIC_MARGIN_LEFT + TopicNode.radius - ConnectLineView.paintWidth / 2);
                    constraintSet.applyTo(topicLayout);

                    line.setAlpha(0.0f);
                    line.height = TOPIC_MARGIN_TOP;
                }

                for (int j = 0; j < nodeList.size(); j++) {
                    if (j != tempChosen) {
                        final TopicNode otherNode = nodeList.get(j);
                        otherNode.animate()
                                .alpha(1.0f)
                                .setDuration(300)
                                .withStartAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        otherNode.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                    final ConnectLineView otherLine = lines.get(j);
                    otherLine.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .withStartAction(new Runnable() {
                                @Override
                                public void run() {
                                    otherLine.setVisibility(View.VISIBLE);
                                    otherLine.setAlpha(1.0f);
                                }
                            });
                }
                node.setOnTouchListener(listeners.get(tempChosen));
            }
        });
        nodeMover.start();
    }*/

    /*@SuppressLint("ClickableViewAccessibility")
    private void addTouchListeners() {
        for (int i = 0; i < nodeList.size(); i++) {
            final TopicNode node = nodeList.get(i);
            final int index = i;
            final View.OnTouchListener listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final float x = event.getX();
                    final float y = event.getY();
                    int mask = event.getActionMasked();
                    switch (mask) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN: {
                            if (node.circleTouched(x, y)) {
                                node.setEnabled(false);

                                chosenTopic = index;
                                final ConnectLineView line = lines.get(index);

                                for (int j = 0; j < nodeList.size(); j++) {
                                    if (j != index) {
                                        final TopicNode otherNode = nodeList.get(j);
                                        otherNode.animate()
                                                .alpha(0.0f)
                                                .setDuration(300)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        otherNode.setVisibility(View.GONE);
                                                    }
                                                });
                                    }
                                    final ConnectLineView otherLine = lines.get(j);
                                    otherLine.animate()
                                            .alpha(0.0f)
                                            .setDuration(300)
                                            .withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    otherLine.setVisibility(View.GONE);
                                                }
                                            });
                                }

                                int deltaY = DashboardActivity.TOPIC_MARGIN_TOP + TopicNode.radius * 2;

                                AdditiveAnimator nodeMover = new AdditiveAnimator().target(node).topMarginBy(-index * deltaY);
                                nodeMover.setDuration(500 + 50 * index);
                                nodeMover.setInterpolator(new AccelerateDecelerateInterpolator());

                                nodeMover.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        node.setClickable(false);

                                        line.height = 0;
                                        line.setVisibility(View.VISIBLE);
                                        line.setAlpha(1.0f);
                                        line.firstNode = null;

                                        ConstraintSet constraintSet = new ConstraintSet();
                                        constraintSet.clone(topicLayout);
                                        constraintSet.connect(line.getId(), ConstraintSet.TOP, topicLayout.getId(), ConstraintSet.TOP, 0);
                                        constraintSet.connect(line.getId(), ConstraintSet.BOTTOM, node.getId(), ConstraintSet.TOP, 0);
                                        constraintSet.applyTo(topicLayout);

                                        final int height = ((ConstraintLayout.LayoutParams) node.getLayoutParams()).topMargin;
                                        ValueAnimator animator = ValueAnimator.ofInt(0, height);
                                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animator.setDuration(300);
                                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation) {
                                                line.height = (int) animation.getAnimatedValue();
                                                line.requestLayout();
                                            }
                                        });
                                        animator.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                getLessonList(chosenTopic);

                                                node.setEnabled(true);
                                                node.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        *//*final float ex = event.getX();
                                                        final float ey = event.getY();*//*
                                                        int mask = event.getActionMasked();
                                                        switch (mask) {
                                                            case MotionEvent.ACTION_DOWN:
                                                            case MotionEvent.ACTION_POINTER_DOWN: {
                                                                DashboardActivity.this.backToTopicList();
                                                            }
                                                            break;
                                                        }
                                                        return false;
                                                    }
                                                });
                                            }
                                        });
                                        animator.start();
                                    }
                                });
                                nodeMover.start();
                            }
                        }
                        break;
                    }
                    return false;
                }
            };
            node.setOnTouchListener(listener);
            listeners.add(listener);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void addLessonTouchListener(int chosenTopic) {
        if (chosenTopic < 0)
            return;

        TopicNode node = nodeList.get(chosenTopic);
        for (int i = 0; i < node.lessons.size(); i++) {
            final LessonNode lessonNode = node.lessons.get(i);
            final int exp = topics.get(chosenTopic).lessons.get(i).score;
            final int lessonId = topics.get(chosenTopic).lessons.get(i).id;

            lessonNode.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float y = event.getY();
                    int mask = event.getActionMasked();
                    switch (mask) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN: {
                            if (lessonNode.circleTouched(x, y)) {
                                Intent intent = new Intent(DashboardActivity.this, LessonActivity.class);
                                intent.putExtra("Exp", exp);
                                intent.putExtra("LessonId", lessonId);
                                SwipeBackActivityHelper.startSwipeActivity(DashboardActivity.this, intent, true, true, false);
                            }
                        }
                        break;
                    }

                    return false;
                }
            });
        }
    }

    private void showLessons(int chosenTopic) {
        nodeList.get(chosenTopic).showLessonList();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addLessonNodesToTopicNode(int chosenTopic) {
        if (chosenTopic < 0)
            return;

        TopicNode node = nodeList.get(chosenTopic);
        Topic topic = topics.get(chosenTopic);
        for (final Lesson lesson : topic.lessons) {
            final LessonNode lessonNode = new LessonNode(DashboardActivity.this);
            lessonNode.lessonName = lesson.name;
            lessonNode.setId(View.generateViewId());
            lessonNode.setParent(node);
            node.lessons.add(lessonNode);
        }
    }

    private void getLessonList(final int chosenTopic) {
        // do nothing
        final Topic topic = topics.get(chosenTopic);

        Call<LessonList> call = PolyglottoService.getLessonList(topic.id);

        call.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(@NonNull Call<LessonList> call, @NonNull Response<LessonList> response) {
                if (response.body() == null) {
                    SweetAlertDialog pDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");
                    pDialog.show();
                } else {
                    topic.lessons = response.body().lessons;

                    addLessonNodesToTopicNode(chosenTopic);
                    showLessons(chosenTopic);
                    addLessonTouchListener(chosenTopic);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LessonList> call, @NonNull Throwable t) {
                // use mock data
                SweetAlertDialog pDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");
                pDialog.show();

                addLessonNodesToTopicNode(chosenTopic);
                showLessons(chosenTopic);
                addLessonTouchListener(chosenTopic);
            }
        });
    }

    private void buildTopicsViews() {
        TopicNode firstNode = new TopicNode(DashboardActivity.this);
        firstNode.topicName = topics.get(0).name;
        firstNode.setId(View.generateViewId());
        nodeList.add(firstNode);
        for (int i = 1; i < topics.size(); i++) {
            TopicNode node = new TopicNode(DashboardActivity.this);
            node.topicName = topics.get(i).name;
            node.setId(View.generateViewId());
            nodeList.add(node);
        }

        ConnectLineView firstLine = new ConnectLineView(this);
        firstLine.parent = topicLayout;
        firstLine.secondNode = firstNode;
        firstNode.lines.add(firstLine);
        firstLine.setId(View.generateViewId());
        lines.add(firstLine);
        for (int i = 1; i < nodeList.size(); i++) {
            ConnectLineView line = new ConnectLineView(this);
            line.setId(View.generateViewId());

            line.parent = topicLayout;
            TopicNode first = nodeList.get(i - 1);
            TopicNode second = nodeList.get(i);
            line.firstNode = first;
            line.secondNode = second;

            first.lines.add(line);
            second.lines.add(line);

            lines.add(line);
        }
    }

    private void arrangeTopicNodes() {
        TopicNode firstNode = nodeList.get(0);
        for (int i = 0; i < nodeList.size(); i++) {
            TopicNode node = nodeList.get(i);
            topicLayout.addView(node);
        }

        int deltaY = DashboardActivity.TOPIC_MARGIN_TOP + TopicNode.radius * 2;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(topicLayout);
        constraintSet.connect(firstNode.getId(), ConstraintSet.TOP, topicLayout.getId(), ConstraintSet.TOP, TOPIC_MARGIN_TOP + 64);
        constraintSet.connect(firstNode.getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, TOPIC_MARGIN_LEFT);

        for (int i = 1; i < nodeList.size(); i++) {
            constraintSet.connect(nodeList.get(i).getId(), ConstraintSet.TOP, topicLayout.getId(), ConstraintSet.TOP, TOPIC_MARGIN_TOP + 64 + deltaY * i);
            constraintSet.connect(nodeList.get(i).getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, TOPIC_MARGIN_LEFT);
        }
        constraintSet.applyTo(topicLayout);

        ConnectLineView firstLine = lines.get(0);
        firstLine.height = ((ConstraintLayout.LayoutParams) firstLine.secondNode.getLayoutParams()).topMargin;
        topicLayout.addView(firstLine);
        for (int i = 1; i < lines.size(); i++) {
            ConnectLineView line = lines.get(i);
            line.height = TOPIC_MARGIN_TOP;
            topicLayout.addView(line);
        }

        constraintSet.clone(topicLayout);
        int lineLeft = (int) (TopicNode.radius + TOPIC_MARGIN_LEFT - ConnectLineView.paintWidth / 2);
        constraintSet.connect(firstLine.getId(), ConstraintSet.TOP, topicLayout.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.BOTTOM, firstNode.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, lineLeft);
        for (int i = 1; i < lines.size(); i++) {
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.TOP, nodeList.get(i - 1).getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.BOTTOM, nodeList.get(i).getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, lineLeft);
        }
        constraintSet.applyTo(topicLayout);
    }*/

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
        if (((LearningFragment)pagerAdapter.getFragment(0)).chosenTopic < 0)
            helper.finish();
        else
            ((LearningFragment)pagerAdapter.getFragment(0)).goTobackToTopicList();
    }

    /*private void getTopicsList() throws IOException, InterruptedException {

        Call<TopicList> call = PolyglottoService.getTopicList();

        call.enqueue(new Callback<TopicList>() {
            @Override
            public void onResponse(@NonNull Call<TopicList> call, @NonNull Response<TopicList> response) {
                if (response.body() == null) {
                    SweetAlertDialog pDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");
                    pDialog.show();
                } else {
                    topics = response.body().topics;

                    buildTopicsViews();
                    addTouchListeners();
                    arrangeTopicNodes();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopicList> call, @NonNull Throwable t) {
                SweetAlertDialog pDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Có sự cố xảy ra, vui lòng thử lại");
                pDialog.show();

                // use mock data
                for (int i = 0; i < 10; i++) {
                    Topic tp = new Topic("Chủ đề " + String.valueOf(i));
                    tp.id = i;
                    tp.addLesson(new Lesson("Chủ đề " + String.valueOf(i) + "-1"));
                    tp.addLesson(new Lesson("Chủ đề " + String.valueOf(i) + "-2"));
                    topics.add(tp);
                }

                buildTopicsViews();
                addTouchListeners();
                arrangeTopicNodes();
            }
        });
    }*/
}

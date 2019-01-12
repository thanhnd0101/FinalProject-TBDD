package com.example.huanhm.duolingoclone.DashboardFagments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ScrollView;

import com.example.huanhm.duolingoclone.Activities.DashboardActivity;
import com.example.huanhm.duolingoclone.Activities.LessonActivity;
import com.example.huanhm.duolingoclone.CustomViews.ConnectLineView;
import com.example.huanhm.duolingoclone.CustomViews.LessonNode;
import com.example.huanhm.duolingoclone.CustomViews.TopicNode;
import com.example.huanhm.duolingoclone.DataModel.Lesson;
import com.example.huanhm.duolingoclone.DataModel.Topic;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.LessonList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoResponse.TopicList;
import com.example.huanhm.duolingoclone.PolyglottoService.PolyglottoService;
import com.example.huanhm.duolingoclone.R;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Objects;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class LearningFragment extends Fragment {
    ScrollView scroll;
    ArrayList<Topic> topics = new ArrayList<>();
    ArrayList<TopicNode> nodeList = new ArrayList<>();
    ArrayList<ConnectLineView> lines = new ArrayList<>();
    ArrayList<View.OnTouchListener> listeners = new ArrayList<>();
    ConstraintLayout topicLayout;
    public int chosenTopic = -1;

    public static final int TOPIC_MARGIN_TOP = 256;
    public static final int TOPIC_MARGIN_LEFT = 64;
    public static final int LESSON_MARGIN_TOP = 128;

    //SwipeBackActivityHelper helper = new SwipeBackActivityHelper();

    Context context;

    public LearningFragment(Context context) {
        this.context = context;
    }

    public static LearningFragment getLearningFragmentInstance(Context context) {
        LearningFragment learningFragment = new LearningFragment(context);
        Bundle bundle = new Bundle();
        learningFragment.setArguments(bundle);
        learningFragment.setRetainInstance(true);
        return learningFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*helper.setEdgeMode(true)
                .setParallaxMode(true)
                .setParallaxRatio(1)
                .setNeedBackgroundShadow(true)
                .init(Objects.requireNonNull(getActivity()));*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment_learning, container, false);
        initLayout(view);
        getTopicsList();
        return view;
    }

    private String[] topicName = new String[]{"General Knowledge","Books","Film",
            "Music","Musicals & Theatres","Television","Video Games","Board Games",
            "Science & Nature","Science: Computers",
            "Mathematics","Mythology","Sports","Geography","History","Politics","Art","Celebrities",
            "Animals","Vehicles","Comics","Gadgets","Anime & Manga",
    "Cartoon & Animations"};
    private void getTopicsList() {
        topics.clear();

        // use mock data
        for (int i = 0; i < topicName.length; i++) {
            Topic tp = new Topic(topicName[i]);
            tp.id = i;
            tp.addLesson(new Lesson("Easy"));
            tp.addLesson(new Lesson("Medium"));
            tp.addLesson(new Lesson("Hard"));
            topics.add(tp);
        }

        buildTopicsViews();
        addTouchListeners();
        arrangeTopicNodes();

    }


    private void initLayout(View view) {
        scroll = view.findViewById(R.id.scrollview_dashboard_learning);
        topicLayout = view.findViewById(R.id.topic_layout_dashboard);
    }

    private void backToTopicList() {
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
    }

    private void buildTopicsViews() {
        nodeList.clear();
        lines.clear();

        TopicNode firstNode = new TopicNode(getContext());
        firstNode.topicName = topics.get(0).name;
        firstNode.setId(View.generateViewId());
        nodeList.add(firstNode);
        for (int i = 1; i < topics.size(); i++) {
            TopicNode node = new TopicNode(getContext());
            node.topicName = topics.get(i).name;
            node.setId(View.generateViewId());
            nodeList.add(node);
        }


        ConnectLineView firstLine = new ConnectLineView(getContext());
        firstLine.parent = topicLayout;
        firstLine.secondNode = firstNode;
        firstNode.lines.add(firstLine);
        firstLine.setId(View.generateViewId());
        lines.add(firstLine);
        for (int i = 1; i < nodeList.size(); i++) {
            ConnectLineView line = new ConnectLineView(getContext());
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

    @SuppressLint("ClickableViewAccessibility")
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
                                                        /*final float ex = event.getX();
                                                        final float ey = event.getY();*/
                                                        int mask = event.getActionMasked();
                                                        switch (mask) {
                                                            case MotionEvent.ACTION_DOWN:
                                                            case MotionEvent.ACTION_POINTER_DOWN: {
                                                                backToTopicList();
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

    private void getLessonList(final int chosenTopic) {
        // do nothing
        final Topic topic = topics.get(chosenTopic);

        addLessonNodesToTopicNode(chosenTopic);
        showLessons(chosenTopic);
        addLessonTouchListener(chosenTopic);
    }

    private void showLessons(int chosenTopic) {
        nodeList.get(chosenTopic).showLessonList();
    }

    private void addLessonNodesToTopicNode(int chosenTopic) {
        if (chosenTopic < 0)
            return;

        TopicNode node = nodeList.get(chosenTopic);
        Topic topic = topics.get(chosenTopic);
        for (final Lesson lesson : topic.lessons) {
            final LessonNode lessonNode = new LessonNode(getContext());
            lessonNode.lessonName = lesson.name;
            lessonNode.setId(View.generateViewId());
            lessonNode.setParent(node);
            node.lessons.add(lessonNode);
        }
    }

    private void arrangeTopicNodes() {
        TopicNode firstNode = nodeList.get(0);
        for (int i = 0; i < nodeList.size(); i++) {
            TopicNode node = nodeList.get(i);
            if (node.getParent() != null) {
                ((ViewGroup) node.getParent()).removeView(node);
            }
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
        if (firstLine.getParent() != null) {
            ((ViewGroup) firstLine.getParent()).removeView(firstLine);
        }
        topicLayout.addView(firstLine);
        for (int i = 1; i < lines.size(); i++) {
            ConnectLineView line = lines.get(i);
            line.height = TOPIC_MARGIN_TOP;

            if (line.getParent() != null) {
                ((ViewGroup) line.getParent()).removeView(line);
            }
            topicLayout.addView(line);
        }

        constraintSet.clone(topicLayout);
        int lineLeft = (TopicNode.radius + TOPIC_MARGIN_LEFT - ConnectLineView.paintWidth / 2);
        constraintSet.connect(firstLine.getId(), ConstraintSet.TOP, topicLayout.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.BOTTOM, firstNode.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, lineLeft);
        for (int i = 1; i < lines.size(); i++) {
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.TOP, nodeList.get(i - 1).getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.BOTTOM, nodeList.get(i).getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(lines.get(i).getId(), ConstraintSet.LEFT, topicLayout.getId(), ConstraintSet.LEFT, lineLeft);
        }
        constraintSet.applyTo(topicLayout);
    }

    private String[] difficulty = new String[]{"easy","medium","hard"};
    @SuppressLint("ClickableViewAccessibility")
    private void addLessonTouchListener(final int chosenTopic) {
        if (chosenTopic < 0)
            return;

        TopicNode node = nodeList.get(chosenTopic);
        for (int i = 0; i < node.lessons.size(); i++) {
            final LessonNode lessonNode = node.lessons.get(i);
            /*final int exp = topics.get(chosenTopic).lessons.get(i).score;
            final int lessonId = topics.get(chosenTopic).lessons.get(i).id;*/

            final int index = i;
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
                                Intent intent = new Intent(getContext(), LessonActivity.class);
                                /*intent.putExtra("Exp", exp);
                                intent.putExtra("LessonId", lessonId);*/
                                intent.putExtra("category",chosenTopic + 9);
                                intent.putExtra("difficulty", difficulty[index]);
                                SwipeBackActivityHelper.startSwipeActivity(Objects.requireNonNull(getActivity()), intent, true, true, false);
                            }
                        }
                        break;
                    }
                    return false;
                }
            });
        }
    }

    public void goTobackToTopicList() {
        if (chosenTopic >= 0) {
            backToTopicList();
        }
    }
}

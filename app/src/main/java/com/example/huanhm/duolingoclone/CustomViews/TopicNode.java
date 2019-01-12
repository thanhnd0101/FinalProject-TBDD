package com.example.huanhm.duolingoclone.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.Activities.DashboardActivity;
import com.example.huanhm.duolingoclone.DataModel.Lesson;

import java.util.ArrayList;

public class TopicNode extends NodeInterface {
    private static int index = 0;
    public Paint cirlcePaint;
    public TextPaint textPaint;
    public String topicName;
    public static final int radius = 100;
    public static final int textSize = 80;

    public ArrayList<ConnectLineView> lines = new ArrayList<>();
    public ArrayList<LessonNode> lessons = new ArrayList<>();
    public ArrayList<ConnectLineView> lessonLines = new ArrayList<>();

    int width;
    int height;
    Rect bounds = new Rect();

    private static int[] colors = new int[]{
            Color.parseColor("#FFCA28"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#64B5F6"),
            Color.parseColor("#BA68C8")};
    int i = 0;

    public TopicNode(Context context) {
        super(context);

        init();
    }

    public TopicNode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        cirlcePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cirlcePaint.setColor(colors[index]);
        index = (index + 1) % colors.length;

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        topicName = "Unknown Topic";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(radius, height / 2, radius, cirlcePaint);
        canvas.drawText(topicName, radius * 3, radius + bounds.height() / 3, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        textPaint.getTextBounds(topicName, 0, topicName.length(), bounds);

        width = (int) (radius * 3 + bounds.width() + 20);
        height = (int) Math.max(2 * radius, bounds.height());
        setMeasuredDimension(width, height);
    }

    public boolean circleTouched(float x, float y) {
        if (x < 0 || x > radius * 2)
            return false;
        if (y < 0 || y > radius * 2)
            return false;
        return true;
    }

    @Override
    public int getCircleColor() {
        return cirlcePaint.getColor();
    }

    public void showLessonList() {
        if (lessons.isEmpty()){
            Toast.makeText(getContext(), "Lesson list empty", Toast.LENGTH_SHORT);
            return;
        }

        ConstraintLayout layout = (ConstraintLayout)getParent();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)getLayoutParams();
        int left = params.leftMargin;

        LessonNode firstNode = lessons.get(0);
        if(firstNode.getParent() != null){
            ((ViewGroup)firstNode.getParent()).removeView(firstNode);
        }
        layout.addView(firstNode);

        ConnectLineView firstLine = new ConnectLineView(getContext());
        firstLine.firstNode = this;
        firstLine.secondNode = firstNode;
        firstLine.height = DashboardActivity.LESSON_MARGIN_TOP;
        lessonLines.add(firstLine);
        firstLine.setId(View.generateViewId());

        if(firstLine.getParent()!=null){
            ((ViewGroup)firstLine.getParent()).removeView(firstLine);
        }
        layout.addView(firstLine);
        for (int i = 1; i < lessons.size(); i++) {
            LessonNode node = lessons.get(i);
            if(node.getParent()!=null){
                ((ViewGroup)node.getParent()).removeView(node);
            }
            layout.addView(node);

            ConnectLineView line = new ConnectLineView(getContext());
            line.firstNode = lessons.get(i - 1);
            line.secondNode = node;
            line.height = DashboardActivity.LESSON_MARGIN_TOP;
            lessonLines.add(line);
            line.setId(View.generateViewId());
            if(line.getParent()!=null){
                ((ViewGroup)line.getParent()).removeView(line);
            }
            layout.addView(line);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        int deltaY = DashboardActivity.LESSON_MARGIN_TOP + LessonNode.radius * 2;
        constraintSet.connect(firstNode.getId(), ConstraintSet.TOP, this.getId(), ConstraintSet.BOTTOM, DashboardActivity.LESSON_MARGIN_TOP);
        constraintSet.connect(firstNode.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, left + radius - LessonNode.radius);

        constraintSet.connect(firstLine.getId(), ConstraintSet.TOP, this.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.BOTTOM, firstNode.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(firstLine.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, left + radius - ConnectLineView.paintWidth / 2);
        for (int i = 1; i < lessons.size(); i++) {
            LessonNode node = lessons.get(i);
            constraintSet.connect(node.getId(), ConstraintSet.TOP, this.getId(), ConstraintSet.BOTTOM, DashboardActivity.LESSON_MARGIN_TOP + deltaY * i);
            constraintSet.connect(node.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, left + radius - LessonNode.radius);

            ConnectLineView line = lessonLines.get(i);
            constraintSet.connect(line.getId(), ConstraintSet.TOP, line.firstNode.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(line.getId(), ConstraintSet.BOTTOM, node.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(line.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, left + radius - ConnectLineView.paintWidth / 2);
        }

        constraintSet.applyTo(layout);
    }

    public void hideLessonList() {
        ConstraintLayout layout = (ConstraintLayout)getParent();
        for (LessonNode node : lessons)
            layout.removeView(node);
        lessons.clear();
        for (ConnectLineView line : lessonLines)
            layout.removeView(line);
        lessonLines.clear();
    }
}

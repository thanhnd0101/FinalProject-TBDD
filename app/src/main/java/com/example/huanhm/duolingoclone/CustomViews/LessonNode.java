package com.example.huanhm.duolingoclone.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;


import java.util.ArrayList;

public class LessonNode extends NodeInterface {
    public Paint cirlcePaint;
    public TextPaint textPaint;
    public String lessonName;
    public TopicNode parent;
    public static final int radius = 80;
    public static final int textSize = 60;

    public ArrayList<ConnectLineView> lines = new ArrayList<>();

    int width;
    int height;
    Rect bounds = new Rect();

    public LessonNode(Context context) {
        super(context);
        init();
    }

    public LessonNode(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        cirlcePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        lessonName = "Unknown Lesson";
    }

    public void setParent(TopicNode parent) {
        cirlcePaint.setColor(parent.getCircleColor());
        this.parent = parent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        assert  parent != null;
        cirlcePaint.setColor(parent.cirlcePaint.getColor());
        canvas.drawCircle(radius, height / 2, radius, cirlcePaint);
        canvas.drawText(lessonName, radius * 3, radius + bounds.height() / 3, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        textPaint.getTextBounds(lessonName, 0, lessonName.length(), bounds);

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
}

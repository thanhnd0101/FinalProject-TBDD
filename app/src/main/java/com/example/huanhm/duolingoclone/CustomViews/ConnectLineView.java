package com.example.huanhm.duolingoclone.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.huanhm.duolingoclone.Activities.DashboardActivity;
import com.example.huanhm.duolingoclone.R;

import org.w3c.dom.Node;

public class ConnectLineView extends View {
    public static final int paintWidth = 30;
    public ConstraintLayout parent = null;
    public NodeInterface firstNode;
    public NodeInterface secondNode;
    public int height;
    public Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ConnectLineView(Context context) {
        super(context);
    }

    public ConnectLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(paintWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int firstX = 0, secondX = 0;
        int firstY = 0, secondY = height;
        if (firstNode != null && secondNode != null) {
            Shader shader = new LinearGradient(firstX, firstY, secondX, secondY,
                    firstNode.getCircleColor(), secondNode.getCircleColor(), Shader.TileMode.MIRROR);
            linePaint.setShader(shader);
            linePaint.setStrokeWidth(paintWidth);
            canvas.drawLine(firstX, firstY, secondX, secondY, linePaint);
        } else if (parent != null && secondNode != null) {
            Shader shader = new LinearGradient(secondX, firstY, secondX, secondY,
                    getContext().getColor(R.color.themeColor), secondNode.getCircleColor(), Shader.TileMode.MIRROR);
            linePaint.setShader(shader);
            linePaint.setStrokeWidth(paintWidth);
            canvas.drawLine(secondX, firstY, secondX, secondY, linePaint);
        }
    }
}

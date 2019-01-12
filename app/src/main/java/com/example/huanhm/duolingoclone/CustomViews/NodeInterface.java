package com.example.huanhm.duolingoclone.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public abstract class NodeInterface extends View {
    public NodeInterface(Context context) {
        super(context);
    }

    public NodeInterface(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract int getCircleColor();
}

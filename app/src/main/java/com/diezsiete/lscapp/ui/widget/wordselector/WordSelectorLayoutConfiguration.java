package com.diezsiete.lscapp.ui.widget.wordselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;

import com.diezsiete.lscapp.R;

public class WordSelectorLayoutConfiguration {
    private int orientation = WordSelectorFlowLayout.HORIZONTAL;
    private boolean debugDraw = false;
    private float weightDefault = 0;
    private int gravity = Gravity.LEFT | Gravity.TOP;
    private int layoutDirection = WordSelectorFlowLayout.LAYOUT_DIRECTION_LTR;

    public WordSelectorLayoutConfiguration(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout);
        try {
            this.setOrientation(a.getInteger(R.styleable.FlowLayout_android_orientation, WordSelectorFlowLayout.HORIZONTAL));
            this.setDebugDraw(a.getBoolean(R.styleable.FlowLayout_debugDraw, false));
            this.setWeightDefault(a.getFloat(R.styleable.FlowLayout_weightDefault, 0.0f));
            this.setGravity(a.getInteger(R.styleable.FlowLayout_android_gravity, Gravity.NO_GRAVITY));
            this.setLayoutDirection(a.getInteger(R.styleable.FlowLayout_layoutDirection, WordSelectorFlowLayout.LAYOUT_DIRECTION_LTR));
        } finally {
            a.recycle();
        }
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation == WordSelectorFlowLayout.VERTICAL) {
            this.orientation = orientation;
        } else {
            this.orientation = WordSelectorFlowLayout.HORIZONTAL;
        }
    }

    public boolean isDebugDraw() {
        return this.debugDraw;
    }

    public void setDebugDraw(boolean debugDraw) {
        this.debugDraw = debugDraw;
    }

    public float getWeightDefault() {
        return this.weightDefault;
    }

    public void setWeightDefault(float weightDefault) {
        this.weightDefault = Math.max(0, weightDefault);
    }

    public int getGravity() {
        return this.gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getLayoutDirection() {
        return layoutDirection;
    }

    public void setLayoutDirection(int layoutDirection) {
        if (layoutDirection == WordSelectorFlowLayout.LAYOUT_DIRECTION_RTL) {
            this.layoutDirection = layoutDirection;
        } else {
            this.layoutDirection = WordSelectorFlowLayout.LAYOUT_DIRECTION_LTR;
        }
    }
}

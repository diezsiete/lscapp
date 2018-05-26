package com.diezsiete.lscapp.ui.view.wordselector;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class WordSelectorView extends LinearLayout {

    public WordSelectorView(Context context) {
        super(context);
    }

    public WordSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(16, 16, 16, 16);
    }
}

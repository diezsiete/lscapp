package com.diezsiete.lscapp.ui.widget.wordselector;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WordSelectorLineDefinition {
    private final List<View> views = new ArrayList<>();
    private final int maxLength;
    private int lineLength;
    private int lineThickness;
    private int lineStartThickness;
    private int lineStartLength;

    public WordSelectorLineDefinition(int maxLength) {
        this.lineStartThickness = 0;
        this.lineStartLength = 0;
        this.maxLength = maxLength;
    }

    public void addView(View child) {
        this.addView(this.views.size(), child);
    }

    public void addView(int i, View child) {
        final WordSelectorFlowLayout.LayoutParams lp = (WordSelectorFlowLayout.LayoutParams) child.getLayoutParams();

        this.views.add(i, child);

        this.lineLength = this.lineLength + lp.getLength() + lp.getSpacingLength();
        this.lineThickness = Math.max(this.lineThickness, lp.getThickness() + lp.getSpacingThickness());
    }

    public boolean canFit(View child) {
        final WordSelectorFlowLayout.LayoutParams lp = (WordSelectorFlowLayout.LayoutParams) child.getLayoutParams();
        return lineLength + lp.getLength() + lp.getSpacingLength() <= maxLength;
    }

    public int getLineStartThickness() {
        return lineStartThickness;
    }

    public void setLineStartThickness(int lineStartThickness) {
        this.lineStartThickness = lineStartThickness;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public int getLineLength() {
        return lineLength;
    }

    public int getLineStartLength() {
        return lineStartLength;
    }

    public void setLineStartLength(int lineStartLength) {
        this.lineStartLength = lineStartLength;
    }

    public List<View> getViews() {
        return views;
    }

    public void setThickness(int thickness) {
        this.lineThickness = thickness;
    }

    public void setLength(int length) {
        this.lineLength = length;
    }
}

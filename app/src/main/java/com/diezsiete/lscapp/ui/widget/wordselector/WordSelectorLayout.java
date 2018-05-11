package com.diezsiete.lscapp.ui.widget.wordselector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

public class WordSelectorLayout extends WordSelectorFlowLayout {

    private int mTokensSize;
    private boolean mOnClickTokenActive = false;
    private int mLineSize = 0;
    private LayoutListener mLayoutListener;

    public interface LayoutListener{
        void onChange();
    }

    public WordSelectorLayout(Context context) {
        super(context);
    }

    public WordSelectorLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WordSelectorLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTokensSize(int tokensSize) {
        mTokensSize = tokensSize;
    }
    public void setOnClickTokenActive(boolean active) {
        mOnClickTokenActive = active;
    }
    public boolean getOnClickTokenActive() {
        return mOnClickTokenActive;
    }
    public int getLineSize() {
        return mLineSize;
    }
    public void setLayoutListener(LayoutListener listener) {
        mLayoutListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        final int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
        final int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        final int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);
        final int controlMaxLength = this.config.getOrientation() == HORIZONTAL ? sizeWidth : sizeHeight;
        final int controlMaxThickness = this.config.getOrientation() == HORIZONTAL ? sizeHeight : sizeWidth;
        final int modeLength = this.config.getOrientation() == HORIZONTAL ? modeWidth : modeHeight;
        final int modeThickness = this.config.getOrientation() == HORIZONTAL ? modeHeight : modeWidth;

        lines.clear();
        WordSelectorLineDefinition currentLine = new WordSelectorLineDefinition(controlMaxLength);
        lines.add(currentLine);

        final int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);

            if(child.getTag() != null && child.getTag() == "SEPARATOR" && mOnClickTokenActive){
                //Log.d("JOSE", "VISIBILITY : " + mLineSize + " = " + lines.size());
                int visible = isDebugDraw() ? VISIBLE : INVISIBLE;
                child.setVisibility(mLineSize == lines.size() ? visible : GONE);
            }

            if (child.getVisibility() == GONE) {
                continue;
            }

            final WordSelectorFlowLayout.LayoutParams lp = (WordSelectorFlowLayout.LayoutParams) child.getLayoutParams();

            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
                    getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height)
            );

            lp.orientation = this.config.getOrientation();
            if (this.config.getOrientation() == WordSelectorFlowLayout.HORIZONTAL) {
                lp.setLength(child.getMeasuredWidth());
                lp.setThickness(child.getMeasuredHeight());
            } else {
                lp.setLength(child.getMeasuredHeight());
                lp.setThickness(child.getMeasuredWidth());
            }

            boolean newLine = lp.isNewLine() || (modeLength != View.MeasureSpec.UNSPECIFIED && !currentLine.canFit(child));
            if (newLine) {
                currentLine = addLine(controlMaxLength);
            }

            if (this.config.getOrientation() == HORIZONTAL && this.config.getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                currentLine.addView(0, child);
            } else {
                currentLine.addView(child);
            }

            if(i + 1 == mTokensSize && !mOnClickTokenActive){
                mLineSize = lines.size();
                //Log.d("JOSE", "lineSize : " + mLineSize);
            }
        }

        this.calculateLinesAndChildPosition(lines);

        int contentLength = 0;
        final int linesCount = lines.size();
        for (int i = 0; i < linesCount; i++) {
            WordSelectorLineDefinition l = lines.get(i);
            contentLength = Math.max(contentLength, l.getLineLength());
        }
        int contentThickness = currentLine.getLineStartThickness() + currentLine.getLineThickness();

        int realControlLength = this.findSize(modeLength, controlMaxLength, contentLength);
        int realControlThickness = this.findSize(modeHeight, controlMaxThickness, contentThickness);

        this.applyGravityToLines(lines, realControlLength, realControlThickness);

        for (int i = 0; i < linesCount; i++) {
            WordSelectorLineDefinition line = lines.get(i);
            this.applyGravityToLine(line);
            this.applyPositionsToViews(line);
        }

        /* need to take padding into account */
        int totalControlWidth = this.getPaddingLeft() + this.getPaddingRight();
        int totalControlHeight = this.getPaddingBottom() + this.getPaddingTop();
        if (this.config.getOrientation() == HORIZONTAL) {
            totalControlWidth += contentLength;
            totalControlHeight += contentThickness;
        } else {
            totalControlWidth += contentThickness;
            totalControlHeight += contentLength;
        }
        this.setMeasuredDimension(resolveSize(totalControlWidth, widthMeasureSpec), resolveSize(totalControlHeight, heightMeasureSpec));

    }

    private WordSelectorLineDefinition addLine(final int controlMaxLength) {
        WordSelectorLineDefinition currentLine = new WordSelectorLineDefinition(controlMaxLength);
        if (this.config.getOrientation() == VERTICAL && this.config.getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            lines.add(0, currentLine);
        } else {
            lines.add(currentLine);
        }
        return currentLine;
    }


    protected int getGravityLine(LayoutParams lp, int lineCount) {
        return lineCount > mLineSize ? Gravity.CENTER :  getGravity(lp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed && mLayoutListener != null){
            mLayoutListener.onChange();
        }
    }


}

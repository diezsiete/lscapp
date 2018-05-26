package com.diezsiete.lscapp.ui.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.db.entity.Practice;

public class LSCRecyclerView extends RecyclerView {
    public View viewPrevSel;

    public LSCRecyclerView(Context context) {
        super(context);
    }

    public LSCRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LSCRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @BindingAdapter("practice")
    public static void getPracticeAnswer(LSCRecyclerView view, Practice practice) {
        if(practice != null) {
            if(practice.getAnswerUser() != null && practice.getAnswer() != null){
                View answerUser = view.getLayoutManager().findViewByPosition(practice.getAnswerUser());
                View answer = view.getLayoutManager().findViewByPosition(practice.getAnswer());
                if(answerUser != null && answer != null) {
                    if (!practice.getCompleted()) {
                        answerUser.setSelected(true);
                        if(view.viewPrevSel != null)
                            view.viewPrevSel.setSelected(false);
                        view.viewPrevSel = answerUser;
                    }else if (!practice.getAnswerCorrect()) {
                        setAnswerDrawable(answer, R.color.answer_success);
                        setAnswerDrawable(answerUser, R.color.answer_danger);
                    }else
                        setAnswerDrawable(answerUser, R.color.answer_success);
                }
            }
        }
    }

    private static void setAnswerDrawable(View answer, @ColorRes int colorRes) {
        int color = ContextCompat.getColor(answer.getContext(), colorRes);
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, new int[]  {color,color});
        gradient.setShape(GradientDrawable.RECTANGLE);
        gradient.setCornerRadius(10.f);
        answer.setBackgroundResource(0);
        answer.setBackground(gradient);
    }
}

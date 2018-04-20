/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diezsiete.lscapp.widget.practice;


import android.content.Context;

import android.os.Bundle;

import android.support.v4.view.MarginLayoutParamsCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.model.practice.Practice;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.widget.fab.CheckableFab;





public abstract class AbsPracticeView<Q extends Practice> extends FrameLayout {


    private final Q mPractice;

    private static final int ANSWER_HIDE_DELAY = 500;
    private static final int FOREGROUND_COLOR_CHANGE_DELAY = 750;
    private final int mSpacingDouble;
    private final LayoutInflater mLayoutInflater;


    private boolean mAnswered;
    private CheckableFab mSubmitAnswer;

    protected TextView mQuestionView;


    /**
     * Enables creation of views for quizzes.
     *
     * @param context The context for this view.
     * @param practice The actual {@link Practice} that is going to be displayed.
     */
    public AbsPracticeView(Context context, Q practice) {
        super(context);
        mPractice = practice;
        mSpacingDouble = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        mLayoutInflater = LayoutInflater.from(context);
        mSubmitAnswer = getSubmitButton();

        //setId(practice.getId());
        setUpQuestionView();
        LinearLayout container = createContainerLayout(context);
        View quizContentView = getInitializedContentView();
        addContentView(container, quizContentView);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
                removeOnLayoutChangeListener(this);
                addFloatingActionButton();
            }
        });
    }

    private CheckableFab getSubmitButton() {
        if (null == mSubmitAnswer) {
            mSubmitAnswer = (CheckableFab) getLayoutInflater()
                    .inflate(R.layout.answer_submit, this, false);
            mSubmitAnswer.hide();
            mSubmitAnswer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitAnswer(v);
                    mSubmitAnswer.setEnabled(false);
                }
            });
        }
        return mSubmitAnswer;
    }

    /**
     * Sets the behaviour for all question views.
     */
    protected void setUpQuestionView() {
        mQuestionView = (TextView) mLayoutInflater.inflate(R.layout.question, this, false);

        mQuestionView.setText(getPractice().getQuestion());
    }

    public Q getPractice() {
        return mPractice;
    }

    private LinearLayout createContainerLayout(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setId(R.id.absPracticeViewContainer);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private View getInitializedContentView() {
        View quizContentView = createPracticeContentView();
        quizContentView.setId(R.id.practice_content);
        quizContentView.setSaveEnabled(true);
        quizContentView.setPadding(mSpacingDouble, mSpacingDouble, mSpacingDouble, mSpacingDouble);

        if (quizContentView instanceof ViewGroup) {
            ((ViewGroup) quizContentView).setClipToPadding(false);
        }
        quizContentView.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.min_height_question));
        return quizContentView;
    }


    protected void addContentView(LinearLayout container, View quizContentView) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
    }


    private void addFloatingActionButton() {
        final int fabSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
        int bottomOfQuestionView = findViewById(R.id.question_view).getBottom();
        final LayoutParams fabLayoutParams = new LayoutParams(fabSize, fabSize,
                Gravity.END | Gravity.TOP);
        final int halfAFab = fabSize / 2;
        //TODO : Arreglar esta forma de posicionar el boton (+750)
        //orig : bottomOfQuestionView - halfAFab
        fabLayoutParams.setMargins(0, // left
                750, //top
                0, // right
                mSpacingDouble); // bottom
        MarginLayoutParamsCompat.setMarginEnd(fabLayoutParams, mSpacingDouble);
        addView(mSubmitAnswer, fabLayoutParams);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    /**
     * Implementations should create the content view for the type of
     * {@link com.diezsiete.lscapp.model.practice.Practice} they want to display.
     *
     * @return the created view to solve the quiz.
     */
    protected abstract View createPracticeContentView();

    /**
     * Implementations must make sure that the answer provided is evaluated and correctly rated.
     *
     * @return <code>true</code> if the question has been correctly answered, else
     * <code>false</code>.
     */
    protected abstract boolean isAnswerCorrect();

    /**
     * Save the user input to a bundle for orientation changes.
     *
     * @return The bundle containing the user's input.
     */
    public abstract Bundle getUserInput();

    /**
     * Restore the user's input.
     *
     * @param savedInput The input that the user made in a prior instance of this view.
     */
    public abstract void setUserInput(Bundle savedInput);



    protected boolean isAnswered() {
        return mAnswered;
    }

    /**
     * Sets the quiz to answered or unanswered.
     *
     * @param answered <code>true</code> if an answer was selected, else <code>false</code>.
     */
    protected void allowAnswer(final boolean answered) {
        if (null != mSubmitAnswer) {
            if (answered) {
                mSubmitAnswer.show();
            } else {
                mSubmitAnswer.hide();
            }
            mAnswered = answered;
        }
    }

    /**
     * Sets the quiz to answered if it not already has been answered.
     * Otherwise does nothing.
     */
    protected void allowAnswer() {
        if (!isAnswered()) {
            allowAnswer(true);
        }
    }

    /**
     * Allows children to submit an answer via code.
     */
    protected void submitAnswer() {
        submitAnswer(findViewById(R.id.submitAnswer));
    }

    @SuppressWarnings("UnusedParameters")
    private void submitAnswer(final View v) {
        final boolean answerCorrect = isAnswerCorrect();
        mPractice.setSolved(true);
    }
}

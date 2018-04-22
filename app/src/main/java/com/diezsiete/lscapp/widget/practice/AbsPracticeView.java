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


import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diezsiete.lscapp.activity.PracticeActivity;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.R;


public abstract class AbsPracticeView<Q extends Practice> extends FrameLayout {
    private final Q mPractice;

    private static final int ANSWER_HIDE_DELAY = 500;
    private static final int FOREGROUND_COLOR_CHANGE_DELAY = 750;
    private final int mSpacingDouble;
    private final LayoutInflater mLayoutInflater;


    private boolean mAnswered;
    private AppCompatButton mSubmitAnswer;

    protected TextView mQuestionView;

    private LinearLayout mContainer;


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

        setId(practice.getId());

        mContainer = (LinearLayout) mLayoutInflater.inflate(R.layout.practice_container, this, false);

        mSubmitAnswer = mContainer.findViewById(R.id.submitAnswer);
        mSubmitAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(v);
                mSubmitAnswer.setEnabled(false);
            }
        });

        mQuestionView = mContainer.findViewById(R.id.question_view);
        setUpQuestionView();

        View quizContentView = createPracticeContentView();
        quizContentView.setId(R.id.practice_content);
        mContainer.addView(quizContentView, 1);

        addView(mContainer);

    }

    /**
     * Sets the behaviour for all question views.
     */
    abstract protected void setUpQuestionView();

    public Q getPractice() {
        return mPractice;
    }


    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

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
                mSubmitAnswer.setVisibility(VISIBLE);
            } else {
                mSubmitAnswer.setVisibility(INVISIBLE);
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
        //mPractice.setSolved(true);
        moveViewOffScreen(answerCorrect);
    }

    private void moveViewOffScreen(final boolean answerCorrect) {
        if (getContext() instanceof PracticeActivity) {
            ((PracticeActivity) getContext()).proceed();
        }
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
}

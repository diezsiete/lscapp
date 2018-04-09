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

package com.lscapp.widget.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.lscapp.R;
import com.lscapp.adapter.OptionsQuizAdapter;
import com.lscapp.helper.ApiLevelHelper;
import com.lscapp.model.Category;
import com.lscapp.model.quiz.WhichOneVideoQuiz;
import com.lscapp.widget.SignVideoPlayer;

@SuppressLint("ViewConstructor")
public class WhichOneVideoQuizView extends AbsQuizView<WhichOneVideoQuiz> {

    private static final String KEY_ANSWER = "ANSWER";
    private int mAnswered = -1;
    private GridView mAnswerView;

    private SignVideoPlayer videoPlayer;

    public WhichOneVideoQuizView(Context context, Category category, WhichOneVideoQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected void setUpQuestionView() {
        super.setUpQuestionView();
        mQuestionView.setText(getResources().getString(R.string.quiz_select_option));
    }

    @Override
    protected View createQuizContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.quiz_which_one_video, this, false);

        mAnswerView = new GridView(getContext());
        mAnswerView.setSelector(R.drawable.selector_button);
        mAnswerView.setNumColumns(2);
        mAnswerView.setAdapter(new OptionsQuizAdapter(getQuiz().getOptions(),
                R.layout.item_answer));
        mAnswerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
                mAnswered = position;
            }
        });


        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);

        videoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        Resources res = getContext().getResources();
        int videoId = res.getIdentifier(getQuiz().getQuestion(), "raw", getContext().getPackageName());
        videoPlayer.addLocalResource(videoId);


        container.addView(mAnswerView);
        return container;
        //return mAnswerView;
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ANSWER, mAnswered);
        return bundle;
    }

    @Override
    @SuppressLint("NewApi")
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        mAnswered = savedInput.getInt(KEY_ANSWER);
        if (mAnswered != -1) {
            if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.KITKAT) && isLaidOut()) {
                setUpUserListSelection(mAnswerView, mAnswered);
            } else {
                addOnLayoutChangeListener(new OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top,
                                               int right, int bottom,
                                               int oldLeft, int oldTop,
                                               int oldRight, int oldBottom) {
                        v.removeOnLayoutChangeListener(this);
                        setUpUserListSelection(mAnswerView, mAnswered);
                    }
                });
            }
        }
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(new int[]{mAnswered});
    }


    @Override
    protected void onDetachedFromWindow() {
        videoPlayer.release();
        Log.d("lscapp", "DETACHED");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        videoPlayer.initialize();
        Log.d("lscapp", "ATTACHED");
        super.onAttachedToWindow();
    }
}

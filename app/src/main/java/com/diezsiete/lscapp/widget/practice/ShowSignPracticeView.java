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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.lscapp.R;
import com.lscapp.helper.SignVideoPlayerHelper;
import com.lscapp.model.Category;
import com.lscapp.model.quiz.ShowSignQuiz;
import com.lscapp.widget.SignVideoPlayer;

@SuppressLint("ViewConstructor")
public class ShowSignQuizView extends AbsQuizView<ShowSignQuiz> {

    private TextView mTextWord;

    private static final String KEY_SELECTION = "SELECTION";
    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, 1);

    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }

    private boolean mAnswer = true;

    public ShowSignQuizView(Context context, Category category, ShowSignQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected void setUpQuestionView() {
        super.setUpQuestionView();
        mQuestionView.setText("");
        mQuestionView.setVisibility(View.GONE);
    }

    @Override
    protected View createQuizContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.quiz_show_sign, this, false);

        if(!getQuiz().isSolved()) {
            SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
            SignVideoPlayer videoPlayer = SignVideoPlayerHelper.create(getContext(), exoPlayerView);

            Resources res = getContext().getResources();

            int videoId = res.getIdentifier(getQuiz().getAnswer(), "raw", getContext().getPackageName());
            videoPlayer.addLocalResource(videoId);
            SignVideoPlayerHelper.initialize();

            mTextWord = (TextView) container.findViewById(R.id.tv_show_sign);
            mTextWord.setText(getQuiz().getQuestion());
        }

        allowAnswer();

        return container;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return true;
        //return getQuiz().isAnswerCorrect(mAnswer);
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SELECTION, mAnswer);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
    }

}

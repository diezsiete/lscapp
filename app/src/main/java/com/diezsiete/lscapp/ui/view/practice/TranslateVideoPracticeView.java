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

package com.diezsiete.lscapp.ui.view.practice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.di.component.ActivityComponent;
import com.diezsiete.lscapp.ui.base.BasePracticeView;
import com.diezsiete.lscapp.utils.signvideoplayer.SignVideoPlayer;
import com.diezsiete.lscapp.utils.WordSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import javax.inject.Inject;


@SuppressLint("ViewConstructor")
public class TranslateVideoPracticeView extends BasePracticeView implements TranslateVideoPracticeContract.SubMvpView {

    @Inject
    TranslateVideoPracticeContract.Presenter<TranslateVideoPracticeContract.SubMvpView> mPresenter;

    private TextView mTextWord;
    private SignVideoPlayer mVideoPlayer;

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }


    @Override
    protected void submitAnswer() {
        mPresenter.onClickButtonSubmitAnswer();
    }

    public TranslateVideoPracticeView(Context context, Practice practice) {
        super(context, practice);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
        }
        mPresenter.onViewPrepared(practice);
    }

    @Override
    protected void setUpQuestionView() {
        mQuestionView.setText(R.string.practice_translate_video_question);
    }


    @Override
    protected View createPracticeContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.practice_translate_video, this, false);


        SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
        mVideoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        for(String video : getPractice().getVideos())
            mVideoPlayer.addExternalResource(video);



        ViewGroup wordSelectorView = container.findViewById(R.id.word_selector);
        WordSelector wordSelector = new WordSelector(wordSelectorView);
        //wordSelector.setOptions(getPractice().getOptions());


        return container;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return true;
        //return getQuiz().isAnswerCorrect(mAnswer);
    }

    @Override
    public Bundle getUserInput() {
        return new Bundle();
    }

    @Override
    public void setUserInput(Bundle savedInput) {
    }

    @Override
    protected void onDetachedFromWindow() {
        mVideoPlayer.release();
        Log.d("JOSE", "DETACHED");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        mVideoPlayer.initialize();
        Log.d("JOSE", "ATTACHED");
        super.onAttachedToWindow();
    }

    @Override
    protected void setUp() {

    }

}

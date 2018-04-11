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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.PracticeOptionsTextAdapter;
import com.diezsiete.lscapp.model.practice.ShowSignPractice;
import com.diezsiete.lscapp.model.practice.TranslateVideoPractice;
import com.diezsiete.lscapp.utils.SignVideoPlayerHelper;
import com.diezsiete.lscapp.widget.SignVideoPlayer;
import com.diezsiete.lscapp.widget.WordSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


@SuppressLint("ViewConstructor")
public class TranslateVideoPracticeView extends AbsPracticeView<TranslateVideoPractice> {

    private TextView mTextWord;
    private SignVideoPlayer mVideoPlayer;

    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

    static {
        LAYOUT_PARAMS.gravity = Gravity.CENTER;
    }


    public TranslateVideoPracticeView(Context context, TranslateVideoPractice practice) {
        super(context, practice);
    }


    @Override
    protected View createPracticeContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.practice_translate_video, this, false);

        if(!getPractice().isSolved()) {
            SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
            mVideoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
            for(String video : getPractice().getVideo())
                mVideoPlayer.addExternalResource(video);

        }

        ViewGroup wordSelectorView = container.findViewById(R.id.word_selector);
        WordSelector wordSelector = new WordSelector(wordSelectorView);
        wordSelector.setOptions(getPractice().getOptions());


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
        //Log.d("lscapp", "DETACHED");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        mVideoPlayer.initialize();
        //Log.d("lscapp", "ATTACHED");
        super.onAttachedToWindow();
    }

}

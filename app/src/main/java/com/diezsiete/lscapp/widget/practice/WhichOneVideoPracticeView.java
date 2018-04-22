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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.diezsiete.lscapp.adapter.PracticeOptionsTextAdapter;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.widget.SignVideoPlayer;



@SuppressLint("ViewConstructor")
public class WhichOneVideoPracticeView extends AbsPracticeView<Practice> {

    private static final String KEY_ANSWER = "ANSWER";
    private int mAnswered = -1;
    private GridView mAnswerView;

    private SignVideoPlayer videoPlayer;

    public WhichOneVideoPracticeView(Context context, Practice practice) {
        super(context, practice);
    }

    @Override
    protected void setUpQuestionView() {
        mQuestionView.setText(R.string.practice_which_one_video_question);
    }


    @Override
    protected View createPracticeContentView() {
        final ViewGroup container = (ViewGroup) getLayoutInflater().inflate(
                R.layout.practice_which_one_video, this, false);

        mAnswerView = new GridView(getContext());
        mAnswerView.setSelector(R.drawable.selector_button);
        mAnswerView.setNumColumns(2);
        mAnswerView.setAdapter(new PracticeOptionsTextAdapter(getPractice().getWords(), R.layout.practice_item_answer));
        mAnswerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
                mAnswered = position;
            }
        });

        /*SimpleExoPlayerView exoPlayerView = (SimpleExoPlayerView) container.findViewById(R.id.video_view);
        videoPlayer = new SignVideoPlayer(getContext(), exoPlayerView);
        for(String video : getPractice().getVideos())
            videoPlayer.addExternalResource(video);*/


        container.addView(mAnswerView);
        return container;

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
    }

    @Override
    protected boolean isAnswerCorrect() {
        return true;
        //return getPractice().isAnswerCorrect(new int[]{mAnswered});
    }


    @Override
    protected void onDetachedFromWindow() {
        //videoPlayer.release();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        //videoPlayer.initialize();
        super.onAttachedToWindow();
    }
}

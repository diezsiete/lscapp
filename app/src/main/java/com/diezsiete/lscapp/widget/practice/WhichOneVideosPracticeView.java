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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lscapp.R;
import com.lscapp.adapter.VideoQuizAdapter;
import com.lscapp.helper.ApiLevelHelper;
import com.lscapp.model.Category;
import com.lscapp.model.quiz.WhichOneVideosQuiz;

@SuppressLint("ViewConstructor")
public class WhichOneVideosQuizView extends AbsQuizView<WhichOneVideosQuiz> {

    private static final String KEY_ANSWER = "ANSWER";
    private int mAnswered = -1;
    private GridView mAnswerView;

    public WhichOneVideosQuizView(Context context, Category category, WhichOneVideosQuiz quiz) {
        super(context, category, quiz);
    }

    @Override
    protected View createQuizContentView() {
        mAnswerView = new GridView(getContext());
        mAnswerView.setSelector(R.drawable.selector_button);
        mAnswerView.setNumColumns(2);
        mAnswerView.setAdapter(new VideoQuizAdapter(getQuiz().getOptions(),
                R.layout.video_answer));
        mAnswerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
                mAnswered = position;
            }
        });
        return mAnswerView;
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
                addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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
}

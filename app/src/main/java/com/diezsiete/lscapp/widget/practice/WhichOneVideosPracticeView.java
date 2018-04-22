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
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.adapter.VideoPracticeAdapter;


@SuppressLint("ViewConstructor")
public class WhichOneVideosPracticeView extends AbsPracticeView<Practice> {

    private static final String KEY_ANSWER = "ANSWER";
    private int mAnswered = -1;
    private GridView mAnswerView;

    public WhichOneVideosPracticeView(Context context, Practice practice) {
        super(context, practice);
    }

    @Override
    protected void setUpQuestionView() {
        mQuestionView.setText(getPractice().getWords()[0]);
    }

    @Override
    protected View createPracticeContentView() {
        mAnswerView = new GridView(getContext());
        mAnswerView.setSelector(R.drawable.selector_button);
        mAnswerView.setNumColumns(2);
        mAnswerView.setAdapter(new VideoPracticeAdapter(getPractice().getVideos(), R.layout.video_answer));
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
        /*if (mAnswered != -1) {
            if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.KITKAT) && isLaidOut()) {
                setUpUserListSelection(mAnswerView, mAnswered);
            }
        }*/
    }

    @Override
    protected boolean isAnswerCorrect() {
        return true;
        //return getQuiz().isAnswerCorrect(new int[]{mAnswered});
    }
}

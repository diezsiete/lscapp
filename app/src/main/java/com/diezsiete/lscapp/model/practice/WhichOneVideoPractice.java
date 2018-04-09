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

package com.diezsiete.lscapp.model.practice;


import android.content.res.Resources;

import com.diezsiete.lscapp.R;

public final class WhichOneVideoPractice extends Practice {

    private String mQuestion;
    private String[] mVideo;
    private int mAnswer;
    private String[] mOptions;


    public WhichOneVideoPractice(String question, String[] video, String[] options, int answer) {
        super();
        mQuestion = question;
        mVideo = video;
        mOptions = options;
        mAnswer = answer;
    }

    @Override
    public PracticeType getType() {
        return PracticeType.WHICH_ONE_VIDEO;
    }


    @Override
    public String getQuestion() {
        return mQuestion;
    }

    public String[] getOptions() {
        return mOptions;
    }

    public String[] getVideo() {
        return mVideo;
    }

}

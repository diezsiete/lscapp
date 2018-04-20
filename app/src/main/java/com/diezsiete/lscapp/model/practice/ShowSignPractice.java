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


import org.json.JSONArray;
import org.json.JSONException;

public final class ShowSignPractice extends Practice {

    private String mMeaning;
    private String[] mVideo;

    public ShowSignPractice(String meaning, String[] video) {
        super();
        mMeaning = meaning;
        mVideo = video;
    }

    @Override
    public PracticeType getType() {
        return PracticeType.SHOW_SIGN;
    }

    @Override
    public String getQuestion(){
        return mMeaning;
    }


    public String getMeaning() { return  mMeaning; }

    public String[] getVideo() {
        return mVideo;
    }

}

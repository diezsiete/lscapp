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



import android.util.Log;

import com.diezsiete.lscapp.model.Image;
import com.diezsiete.lscapp.model.JsonAttributes;
import com.diezsiete.lscapp.utils.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public final class DiscoverImagePractice extends Practice {

    private String mQuestion;
    private String[] mVideo;
    private int mAnswer;
    private Image[] mImages;




    public DiscoverImagePractice(JSONObject json) throws JSONException {
        mQuestion = json.getString(JsonAttributes.QUESTION);
        mVideo = JsonHelper.jsonArrayToStringArray(json.getString(JsonAttributes.VIDEO));
        mAnswer = json.getInt(JsonAttributes.ANSWER);

        String[] images = JsonHelper.jsonArrayToStringArray(json.getString(JsonAttributes.IMAGES));
        mImages = new Image[images.length];

        for(int i = 0; i < images.length; i++){
            JSONObject imageJson = new JSONObject(images[i]);
            Image image = new Image();
            image.setWidth(imageJson.getInt("width"));
            image.setHeight(imageJson.getInt("height"));
            image.setUrl(imageJson.getString("url"));
            mImages[i] = image;
        }

    }

    @Override
    public PracticeType getType() {
        return PracticeType.DISCOVER_IMAGE;
    }


    @Override
    public String getQuestion() {
        return mQuestion;
    }

    public Image[] getImages() {
        return mImages;
    }

    public String[] getVideo() {
        return mVideo;
    }

}

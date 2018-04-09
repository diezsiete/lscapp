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

import com.diezsiete.lscapp.model.JsonAttributes;

/**
 * Available types of practices.
 * Maps {@link JsonAttributes.PracticeType} to subclasses of {@link Practice}.
 */
public enum PracticeType {

    SHOW_SIGN(JsonAttributes.PracticeType.SHOW_SIGN, ShowSignPractice.class),
    WHICH_ONE_VIDEOS(JsonAttributes.PracticeType.WHICH_ONE_VIDEOS, WhichOneVideosPractice.class),
    WHICH_ONE_VIDEO(JsonAttributes.PracticeType.WHICH_ONE_VIDEO, WhichOneVideoPractice.class);

    private final String mJsonName;
    private final Class<? extends Practice> mType;

    PracticeType(final String jsonName, final Class<? extends Practice> type) {
        mJsonName = jsonName;
        mType = type;
    }

    public String getJsonName() {
        return mJsonName;
    }

    public Class<? extends Practice> getType() {
        return mType;
    }
}

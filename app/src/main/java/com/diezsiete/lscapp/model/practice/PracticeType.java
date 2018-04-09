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

package com.lscapp.model.quiz;

import com.lscapp.model.JsonAttributes;

/**
 * Available types of quizzes.
 * Maps {@link JsonAttributes.QuizType} to subclasses of {@link Quiz}.
 */
public enum QuizType {

    SHOW_SIGN(JsonAttributes.QuizType.SHOW_SIGN, ShowSignQuiz.class),
    WHICH_ONE_VIDEOS(JsonAttributes.QuizType.WHICH_ONE_VIDEOS, WhichOneVideosQuiz.class),
    WHICH_ONE_VIDEO(JsonAttributes.QuizType.WHICH_ONE_VIDEO, WhichOneVideoQuiz.class);

    private final String mJsonName;
    private final Class<? extends Quiz> mType;

    QuizType(final String jsonName, final Class<? extends Quiz> type) {
        mJsonName = jsonName;
        mType = type;
    }

    public String getJsonName() {
        return mJsonName;
    }

    public Class<? extends Quiz> getType() {
        return mType;
    }
}

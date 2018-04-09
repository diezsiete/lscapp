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

package com.diezsiete.lscapp.model;

public interface JsonAttributes {


    String ID = "id";
    String NAME = "name";
    String OPTIONS = "options";
    String MEANING = "meaning";
    String VIDEO = "video";
    String QUESTION = "question";
    String ANSWER = "answer";
    String THEME = "theme";
    String TYPE = "type";
    String SCORES = "scores";
    String SOLVED = "solved";

    interface PracticeType {

        String SHOW_SIGN = "show-sign";
        String WHICH_ONE_VIDEOS = "which-one-videos";
        String WHICH_ONE_VIDEO  = "which-one-video";
        String TRANSLATE_VIDEO = "translate-video";
        String DISCOVER_IMAGE = "discover-image";
        String PAIR_ELEMENTS = "pair-elements";
        String ANSWER_QUESTION = "answer-question";
        String TRANSLATE_SENTENCE = "translate-sentence";
    }
}

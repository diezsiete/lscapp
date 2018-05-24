/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.diezsiete.lscapp.util;


import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeVideosData;
import com.diezsiete.lscapp.vo.PracticeVideosWord;
import com.diezsiete.lscapp.vo.PracticeVideosWordData;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.PracticeWords;
import com.diezsiete.lscapp.vo.Word;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static Level createLevel(String levelId, String name) {
        return createLevel(levelId, name,
                "Descripción",
                "https://s3.amazonaws.com/lsc-resources/imagenes/app/sustantivos.png",
                "#F4AD31");
    }

    public static Level createLevel(String levelId, String name, String description, String image, String color) {
        return new Level(levelId, name, description, image, color);
    }

    public static Lesson createLesson(String lessonId, String name) {
        Lesson lesson = new Lesson(lessonId, name,
                "https://s3.amazonaws.com/lsc-resources/imagenes/app/sustantivos.png",
                "#F4AD31");
        return lesson;
    }

    public static PracticeWithData createShowSign(Lesson lesson, String... values) {
        PracticeWithData practice = new PracticeWithData();
        practice.entity = createPractice("show-sign");
        practice.words = createPracticeWords(values);
        practice.videos = createPracticeVideos();
        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson);
        practice.lesson = lessonList;
        return practice;
    }

    public static PracticeWithData createWhichOneVideo(Lesson lesson, String... values) {
        PracticeWithData practice = new PracticeWithData();
        practice.entity = createPractice("which-one-video");
        practice.words = createPracticeWords(values);
        practice.videos = createPracticeVideos();
        List<Lesson> lessonList = new ArrayList<>();
        lessonList.add(lesson);
        practice.lesson = lessonList;
        practice.entity.answerUser = new ArrayList<>();
        practice.entity.answer = new ArrayList<>();
        return practice;
    }

    private static Practice createPractice(String code) {
        return new Practice(code);
    }

    private static List<PracticeWords> createPracticeWords(String... values){
        List<PracticeWords> practiceWords = new ArrayList<>();
        for (String value : values) {
            List<String> strings = new ArrayList<>();
            strings.add(value);
            PracticeWords pw = new PracticeWords();
            pw.words = strings;
            practiceWords.add(pw);
        }
        return practiceWords;
    }

    private static List<PracticeVideosData> createPracticeVideos(){
        List<PracticeVideosData> practiceVideosDataList = new ArrayList<>();
        PracticeVideosData pvd = new PracticeVideosData();
        pvd.entity = new PracticeVideos();
        pvd.videosWord = new ArrayList<>();

        PracticeVideosWordData pvwd = new PracticeVideosWordData();
        pvwd.entity = new PracticeVideosWord();
        pvwd.words = new ArrayList<>();
        pvwd.words.add(new Word("A", "https://s3.amazonaws.com/lsc-resources/videos/a.mp4"));

        pvd.videosWord.add(pvwd);

        practiceVideosDataList.add(pvd);

        return practiceVideosDataList;
    }

    /*
    public static User createUser(String login) {
        return new User(login, null,
                login + " name", null, null, null);
    }

    public static List<Repo> createRepos(int count, String owner, String name,
            String description) {
        List<Repo> repos = new ArrayList<>();
        for(int i = 0; i < count; i ++) {
            repos.add(createRepo(owner + i, name + i, description + i));
        }
        return repos;
    }
    */
}

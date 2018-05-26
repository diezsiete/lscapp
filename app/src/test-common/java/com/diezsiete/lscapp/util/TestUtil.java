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


import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.db.entity.LevelEntity;
import com.diezsiete.lscapp.db.entity.PracticeEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideos;
import com.diezsiete.lscapp.db.entity.PracticeVideosWordEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosWord;
import com.diezsiete.lscapp.db.entity.Practice;
import com.diezsiete.lscapp.db.entity.PracticeWordsEntity;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static LevelEntity createLevel(String levelId, String name) {
        return createLevel(levelId, name,
                "Descripción",
                "https://s3.amazonaws.com/lsc-resources/imagenes/app/sustantivos.png",
                "#F4AD31");
    }

    public static LevelEntity createLevel(String levelId, String name, String description, String image, String color) {
        return new LevelEntity(levelId, name, description, image, color);
    }

    public static LessonEntity createLesson(String lessonId, String name) {
        LessonEntity lessonEntity = new LessonEntity(lessonId, name,
                "https://s3.amazonaws.com/lsc-resources/imagenes/app/sustantivos.png",
                "#F4AD31");
        return lessonEntity;
    }

    public static Practice createShowSign(LessonEntity lessonEntity, String... values) {
        Practice practice = new Practice();
        practice.entity = createPractice("show-sign");
        practice.words = createPracticeWords(values);
        practice.videos = createPracticeVideos();
        List<LessonEntity> lessonEntityList = new ArrayList<>();
        lessonEntityList.add(lessonEntity);
        practice.lessonEntity = lessonEntityList;
        return practice;
    }

    public static Practice createWhichOneVideo(LessonEntity lessonEntity, String... values) {
        Practice practice = new Practice();
        practice.entity = createPractice("which-one-video");
        practice.words = createPracticeWords(values);
        practice.videos = createPracticeVideos();
        List<LessonEntity> lessonEntityList = new ArrayList<>();
        lessonEntityList.add(lessonEntity);
        practice.lessonEntity = lessonEntityList;
        practice.entity.answerUser = new ArrayList<>();
        practice.entity.answer = new ArrayList<>();
        return practice;
    }

    private static PracticeEntity createPractice(String code) {
        return new PracticeEntity(code);
    }

    private static List<PracticeWordsEntity> createPracticeWords(String... values){
        List<PracticeWordsEntity> practiceWordEntities = new ArrayList<>();
        for (String value : values) {
            List<String> strings = new ArrayList<>();
            strings.add(value);
            PracticeWordsEntity pw = new PracticeWordsEntity();
            pw.words = strings;
            practiceWordEntities.add(pw);
        }
        return practiceWordEntities;
    }

    private static List<PracticeVideos> createPracticeVideos(){
        List<PracticeVideos> practiceVideosList = new ArrayList<>();
        PracticeVideos pvd = new PracticeVideos();
        pvd.entity = new PracticeVideosEntity();
        pvd.videosWord = new ArrayList<>();

        PracticeVideosWord pvwd = new PracticeVideosWord();
        pvwd.entity = new PracticeVideosWordEntity();
        pvwd.wordEntities = new ArrayList<>();
        pvwd.wordEntities.add(new WordEntity("A", "https://s3.amazonaws.com/lsc-resources/videos/a.mp4"));

        pvd.videosWord.add(pvwd);

        practiceVideosList.add(pvd);

        return practiceVideosList;
    }

    /*
    public static UserEntity createUser(String login) {
        return new UserEntity(login, null,
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

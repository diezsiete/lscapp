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

package com.diezsiete.lscapp.di;

import android.app.Application;
import android.arch.persistence.room.Room;


import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.remote.PracticeWithDataDeserializer;
import com.diezsiete.lscapp.db.LSCAppDb;
import com.diezsiete.lscapp.db.dao.LessonDao;
import com.diezsiete.lscapp.db.dao.LevelDao;
import com.diezsiete.lscapp.db.dao.PracticeDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosWordDao;
import com.diezsiete.lscapp.db.dao.PracticeWordsDao;
import com.diezsiete.lscapp.db.dao.UserDao;
import com.diezsiete.lscapp.db.dao.WordDao;
import com.diezsiete.lscapp.util.AppConstants;
import com.diezsiete.lscapp.util.LiveDataCallAdapterFactory;
import com.diezsiete.lscapp.db.entity.Practice;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {

    private GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(Practice.class, new PracticeWithDataDeserializer());
        Gson myGson = gsonBuilder.create();
        return GsonConverterFactory.create(myGson);
    }

    @Singleton @Provides
    Api provideWebService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor()).build();

        return new Retrofit.Builder()
                .baseUrl(AppConstants.WEBSERVICE_URL)
                .client(okHttpClient)
                .addConverterFactory(buildGsonConverter())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(Api.class);
    }

    @Singleton @Provides
    LSCAppDb provideDb(Application app) {
        return Room.databaseBuilder(app, LSCAppDb.class, AppConstants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton @Provides
    LevelDao provideLevelDao(LSCAppDb db) {
        return db.levelDao();
    }

    @Singleton @Provides
    LessonDao provideLessonDao(LSCAppDb db) { return db.lessonDao(); }

    @Singleton @Provides
    PracticeDao providePracticeDao(LSCAppDb db) { return db.practiceDao(); }

    @Singleton @Provides
    PracticeWordsDao providePracticeWordsDao(LSCAppDb db) { return db.practiceWordsDao(); }

    @Singleton @Provides
    PracticeVideosDao providePracticeVideosDao(LSCAppDb db) { return db.practiceVideosDao(); }

    @Singleton @Provides
    PracticeVideosWordDao providePracticeVideosWordDao(LSCAppDb db) { return db.practiceVideosWordDao(); }

    @Singleton @Provides
    WordDao provideWordDao(LSCAppDb db){
        return db.wordDao();
    }

    @Singleton @Provides
    UserDao provideUserDao(LSCAppDb db) {
        return db.userDao();
    }
}

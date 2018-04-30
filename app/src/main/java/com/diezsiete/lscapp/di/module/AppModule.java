package com.diezsiete.lscapp.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.diezsiete.lscapp.di.ApplicationContext;
import com.diezsiete.lscapp.di.DatabaseInfo;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application app) {
        mApplication = app;
    }


    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "lscapp.db";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 2;
    }

    @Provides
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("lscapp-prefs", Context.MODE_PRIVATE);
    }


}

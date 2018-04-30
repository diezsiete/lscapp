package com.diezsiete.lscapp.di.component;

import android.app.Application;
import android.content.Context;

import com.diezsiete.lscapp.App;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.db.DbHelper;
import com.diezsiete.lscapp.data.prefs.PreferencesHelper;
import com.diezsiete.lscapp.di.ApplicationContext;
import com.diezsiete.lscapp.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    PreferencesHelper getPreferencesHelper();

    DbHelper getDbHelper();
}

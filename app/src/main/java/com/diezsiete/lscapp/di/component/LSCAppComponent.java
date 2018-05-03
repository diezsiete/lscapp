package com.diezsiete.lscapp.di.component;

import android.app.Application;
import android.content.Context;

import com.diezsiete.lscapp.LSCApp;
import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.data.db.DbHelper;
import com.diezsiete.lscapp.data.prefs.PreferencesHelper;
import com.diezsiete.lscapp.di.ApplicationContext;
import com.diezsiete.lscapp.di.module.LSCAppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = LSCAppModule.class)
public interface LSCAppComponent {
    void inject(LSCApp app);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    PreferencesHelper getPreferencesHelper();

    DbHelper getDbHelper();
}

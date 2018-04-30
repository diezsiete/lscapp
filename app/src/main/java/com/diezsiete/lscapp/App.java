package com.diezsiete.lscapp;

import android.app.Application;
import android.content.Context;


import android.app.Application;
import android.content.Context;

import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.di.component.AppComponent;
import com.diezsiete.lscapp.di.component.DaggerAppComponent;
import com.diezsiete.lscapp.di.module.AppModule;

import javax.inject.Inject;

public class App extends Application {

    private static Context mContext;

    protected AppComponent applicationComponent;


    @Inject
    DataManager dataManager;


    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        applicationComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public AppComponent getComponent(){
        return applicationComponent;
    }

    public static Context getContext(){
        return mContext;
    }
}
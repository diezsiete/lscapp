package com.diezsiete.lscapp;

import android.app.Application;
import android.content.Context;


import com.diezsiete.lscapp.data.DataManager;
import com.diezsiete.lscapp.di.component.LSCAppComponent;
import com.diezsiete.lscapp.di.component.DaggerLSCAppComponent;
import com.diezsiete.lscapp.di.module.LSCAppModule;

import javax.inject.Inject;

public class LSCApp extends Application {

    private static Context mContext;

    protected LSCAppComponent applicationComponent;


    @Inject
    DataManager dataManager;


    public static LSCApp get(Context context) {
        return (LSCApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        applicationComponent = DaggerLSCAppComponent
                .builder()
                .lSCAppModule(new LSCAppModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public LSCAppComponent getComponent(){
        return applicationComponent;
    }

    public static Context getContext(){
        return mContext;
    }
}
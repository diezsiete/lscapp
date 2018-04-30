package com.diezsiete.lscapp.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.di.ActivityContext;
import com.diezsiete.lscapp.di.PerActivity;
import com.diezsiete.lscapp.ui.level.LevelAdapter;
import com.diezsiete.lscapp.ui.level.LevelContract;
import com.diezsiete.lscapp.ui.level.LevelPresenter;
import com.diezsiete.lscapp.ui.login.LoginContract;
import com.diezsiete.lscapp.ui.login.LoginPresenter;
import com.diezsiete.lscapp.ui.main.MainContract;
import com.diezsiete.lscapp.ui.main.MainPresenter;
import com.diezsiete.lscapp.ui.register.RegisterContract;
import com.diezsiete.lscapp.ui.register.RegisterPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    MainContract.Presenter<MainContract.MvpView> provideMainPresenter(
            MainPresenter<MainContract.MvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginContract.Presenter<LoginContract.MvpView> provideLoginPresenter(
            LoginPresenter<LoginContract.MvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RegisterContract.Presenter<RegisterContract.MvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterContract.MvpView> presenter) {
        return presenter;
    }

    @Provides
    LevelContract.Presenter<LevelContract.MvpView> provideLevelPresenter(
            LevelPresenter<LevelContract.MvpView> presenter) {
        return presenter;
    }


    @Provides
    LevelAdapter provideLevelAdapter() {
        return new LevelAdapter(new Level[0]);
    }
}


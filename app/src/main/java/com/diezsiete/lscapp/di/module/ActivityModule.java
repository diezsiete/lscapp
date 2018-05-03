package com.diezsiete.lscapp.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.di.ActivityContext;
import com.diezsiete.lscapp.di.PerActivity;
import com.diezsiete.lscapp.ui.activity.LessonContract;
import com.diezsiete.lscapp.ui.activity.LessonPresenter;
import com.diezsiete.lscapp.ui.adapter.PracticeAdapter;
import com.diezsiete.lscapp.ui.adapter.LessonAdapter;
import com.diezsiete.lscapp.ui.adapter.LevelAdapter;
import com.diezsiete.lscapp.ui.activity.LevelContract;
import com.diezsiete.lscapp.ui.activity.LevelPresenter;
import com.diezsiete.lscapp.ui.fragment.LevelSelectionContract;
import com.diezsiete.lscapp.ui.fragment.LevelSelectionPresenter;
import com.diezsiete.lscapp.ui.activity.LoginContract;
import com.diezsiete.lscapp.ui.activity.LoginPresenter;
import com.diezsiete.lscapp.ui.activity.MainContract;
import com.diezsiete.lscapp.ui.activity.MainPresenter;
import com.diezsiete.lscapp.ui.activity.RegisterContract;
import com.diezsiete.lscapp.ui.activity.RegisterPresenter;
import com.diezsiete.lscapp.ui.view.practice.DiscoverImagePracticeContract;
import com.diezsiete.lscapp.ui.view.practice.DiscoverImagePracticePresenter;
import com.diezsiete.lscapp.ui.view.practice.ShowSignPracticeContract;
import com.diezsiete.lscapp.ui.view.practice.ShowSignPracticePresenter;
import com.diezsiete.lscapp.ui.view.practice.TakePicturePracticeContract;
import com.diezsiete.lscapp.ui.view.practice.TakePicturePracticePresenter;
import com.diezsiete.lscapp.ui.view.practice.TranslateVideoPracticeContract;
import com.diezsiete.lscapp.ui.view.practice.TranslateVideoPracticePresenter;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideoPracticeContract;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideoPracticePresenter;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideosPracticeContract;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideosPracticePresenter;

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
    AppCompatActivity provideActivity() {
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
    @PerActivity
    LevelContract.Presenter<LevelContract.MvpView> provideLevelPresenter(
            LevelPresenter<LevelContract.MvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LessonContract.Presenter<LessonContract.MvpView> provideLessonPresenter(
            LessonPresenter<LessonContract.MvpView> presenter){
        return presenter;
    }

    @Provides
    LevelSelectionContract.Presenter<LevelSelectionContract.MvpView> provideLevelSelectionPresenter(
            LevelSelectionPresenter<LevelSelectionContract.MvpView> presenter) {
        return presenter;
    }

    @Provides
    ShowSignPracticeContract.Presenter<ShowSignPracticeContract.SubMvpView> provideShowSignPracticePresenter(
            ShowSignPracticePresenter<ShowSignPracticeContract.SubMvpView> presenter) {
        return presenter;
    }
    
    @Provides
    DiscoverImagePracticeContract.Presenter<DiscoverImagePracticeContract.SubMvpView> provideDiscoverImagePracticePresenter(
                    DiscoverImagePracticePresenter<DiscoverImagePracticeContract.SubMvpView> presenter) {
        return presenter;
    }

    @Provides
    TakePicturePracticeContract.Presenter<TakePicturePracticeContract.SubMvpView> provideTakePicturePracticePresenter(
            TakePicturePracticePresenter<TakePicturePracticeContract.SubMvpView> presenter) {
        return presenter;
    }

    @Provides
    TranslateVideoPracticeContract.Presenter<TranslateVideoPracticeContract.SubMvpView> provideTranslateVideoPracticePresenter(
                    TranslateVideoPracticePresenter<TranslateVideoPracticeContract.SubMvpView> presenter) {
        return presenter;
    }

    @Provides
    WhichOneVideoPracticeContract.Presenter<WhichOneVideoPracticeContract.SubMvpView> provideWhichOneVideoPracticePresenter(
            WhichOneVideoPracticePresenter<WhichOneVideoPracticeContract.SubMvpView> presenter) {
        return presenter;
    }

    @Provides
    WhichOneVideosPracticeContract.Presenter<WhichOneVideosPracticeContract.SubMvpView> provideWhichOneVideosPracticePresenter(
            WhichOneVideosPracticePresenter<WhichOneVideosPracticeContract.SubMvpView> presenter) {
        return presenter;
    }

    
    @Provides
    LevelAdapter provideLevelAdapter() {
        return new LevelAdapter(new Level[0]);
    }

    @Provides
    LessonAdapter provideLessonAdapter() {
        return new LessonAdapter(new Lesson[0]);
    }

    @Provides
    PracticeAdapter providePracticeAdapter(AppCompatActivity activity) {
        return new PracticeAdapter(activity);
    }


}


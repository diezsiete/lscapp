package com.diezsiete.lscapp.di.component;

import com.diezsiete.lscapp.ui.activity.LessonActivity;
import com.diezsiete.lscapp.ui.activity.LevelActivity;
import com.diezsiete.lscapp.ui.fragment.LevelSelectionFragment;
import com.diezsiete.lscapp.ui.activity.LoginActivity;
import com.diezsiete.lscapp.ui.activity.MainActivity;
import com.diezsiete.lscapp.di.PerActivity;
import com.diezsiete.lscapp.di.module.ActivityModule;
import com.diezsiete.lscapp.ui.activity.RegisterActivity;
import com.diezsiete.lscapp.ui.base.BasePracticeView;
import com.diezsiete.lscapp.ui.view.practice.DiscoverImagePracticeView;
import com.diezsiete.lscapp.ui.view.practice.ShowSignPracticeView;
import com.diezsiete.lscapp.ui.view.practice.TakePicturePracticeView;
import com.diezsiete.lscapp.ui.view.practice.TranslateVideoPracticeView;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideoPracticeView;
import com.diezsiete.lscapp.ui.view.practice.WhichOneVideosPracticeView;

import dagger.Component;

@PerActivity
@Component(dependencies = LSCAppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(LevelActivity activity);

    void inject(LessonActivity activity);

    void inject(LevelSelectionFragment fragment);


    void inject(ShowSignPracticeView view);

    void inject(DiscoverImagePracticeView view);

    void inject(TakePicturePracticeView view);

    void inject(TranslateVideoPracticeView view);

    void inject(WhichOneVideoPracticeView view);

    void inject(WhichOneVideosPracticeView view);
}

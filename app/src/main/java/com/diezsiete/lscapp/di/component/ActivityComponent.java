package com.diezsiete.lscapp.di.component;

import com.diezsiete.lscapp.ui.level.LevelSelectionFragment;
import com.diezsiete.lscapp.ui.login.LoginActivity;
import com.diezsiete.lscapp.ui.main.MainActivity;
import com.diezsiete.lscapp.di.PerActivity;
import com.diezsiete.lscapp.di.module.ActivityModule;
import com.diezsiete.lscapp.ui.register.RegisterActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(LevelSelectionFragment fragment);
}

package com.diezsiete.lscapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.diezsiete.lscapp.ui.MainActivityViewModel;
import com.diezsiete.lscapp.ui.dictionary.DictionaryViewModel;
import com.diezsiete.lscapp.ui.level.LessonViewModel;
import com.diezsiete.lscapp.ui.level.LevelViewModel;
import com.diezsiete.lscapp.ui.practice.PracticeViewModel;
import com.diezsiete.lscapp.viewmodel.LSCAppViewModelFactory;
import com.diezsiete.lscapp.viewmodel.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LevelViewModel.class)
    abstract ViewModel bindLevelViewModel(LevelViewModel levelViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LessonViewModel.class)
    abstract ViewModel bindLessonViewModel(LessonViewModel lessonViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PracticeViewModel.class)
    abstract ViewModel bindPracticeViewModel(PracticeViewModel practiceViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DictionaryViewModel.class)
    abstract ViewModel bindDictionaryViewModel(DictionaryViewModel dictionaryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(LSCAppViewModelFactory factory);
}

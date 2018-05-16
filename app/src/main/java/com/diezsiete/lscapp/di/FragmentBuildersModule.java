/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diezsiete.lscapp.di;

import com.diezsiete.lscapp.ui.dictionary.DictionaryFragment;
import com.diezsiete.lscapp.ui.fragment.LoginFragment;
import com.diezsiete.lscapp.ui.fragment.RegisterFragment;
import com.diezsiete.lscapp.ui.lesson.LessonFragment;
import com.diezsiete.lscapp.ui.level.LevelFragment;
import com.diezsiete.lscapp.ui.level.LevelSelectionFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract LevelSelectionFragment contributeLevelSelectionFragment();

    @ContributesAndroidInjector
    abstract LevelFragment contributeLevelFragment();

    @ContributesAndroidInjector
    abstract LessonFragment contributeLessonFragment();

    @ContributesAndroidInjector
    abstract DictionaryFragment contributeDictionaryFragment();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector
    abstract RegisterFragment contributeRegisterFragment();
}

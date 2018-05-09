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

package com.diezsiete.lscapp.ui.common;

import android.support.v4.app.FragmentManager;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.dictionary.DictionaryFragment;
import com.diezsiete.lscapp.ui.lesson.LessonFragment;
import com.diezsiete.lscapp.ui.level.LevelFragment;
import com.diezsiete.lscapp.ui.level.LevelSelectionFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.main_container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToLevelSelection() {
        LevelSelectionFragment levelSelectionFragment = new LevelSelectionFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, levelSelectionFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToLevel(String levelId) {
        LevelFragment fragment = LevelFragment.create(levelId);
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToLesson(String lessonId) {
        LessonFragment fragment = LessonFragment.create(lessonId);
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToDictionary() {
        DictionaryFragment dictionaryFragment = new DictionaryFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, dictionaryFragment)
                .commitAllowingStateLoss();
    }
}

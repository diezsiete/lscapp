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

package com.diezsiete.lscapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;


import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.ui.MainActivity;
import com.diezsiete.lscapp.ui.fragment.DictionaryFragment;
import com.diezsiete.lscapp.ui.fragment.LoginFragment;
import com.diezsiete.lscapp.ui.fragment.RegisterFragment;
import com.diezsiete.lscapp.ui.fragment.LessonFragment;
import com.diezsiete.lscapp.ui.fragment.LevelFragment;
import com.diezsiete.lscapp.ui.fragment.LevelSelectionFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final MainActivity mainActivity;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.main_container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
        this.mainActivity = mainActivity;
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

    public void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, loginFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, registerFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToPermissions(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
        intent.setData(uri);
        mainActivity.startActivity(intent);
    }
}

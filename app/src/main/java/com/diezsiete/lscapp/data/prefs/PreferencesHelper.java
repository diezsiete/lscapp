package com.diezsiete.lscapp.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private static final String PREF_KEY_CURRENT_LEVEL_ID = "PREF_KEY_CURRENT_LEVEL_ID";
    private static final String PREF_KEY_CURRENT_LESSON_ID = "PREF_KEY_CURRENT_LESSON_ID";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public void setCurrentLevelId(String levelId) {
        mSharedPreferences.edit().putString(PREF_KEY_CURRENT_LEVEL_ID, levelId).apply();
    }

    public String getCurrentLevelId() {
        return mSharedPreferences.getString(PREF_KEY_CURRENT_LEVEL_ID, null);
    }

    public void setCurrentLessonId(String lessonId) {
        mSharedPreferences.edit().putString(PREF_KEY_CURRENT_LESSON_ID, lessonId).apply();
    }

    public String getCurrentLessonId() {
        return mSharedPreferences.getString(PREF_KEY_CURRENT_LESSON_ID, null);
    }
}

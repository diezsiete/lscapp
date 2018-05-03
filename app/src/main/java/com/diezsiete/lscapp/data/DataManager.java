package com.diezsiete.lscapp.data;

import com.diezsiete.lscapp.data.db.DbHelper;
import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.data.db.model.User;
import com.diezsiete.lscapp.data.db.model.Word;
import com.diezsiete.lscapp.data.network.Api;
import com.diezsiete.lscapp.data.network.ApiHelper;
import com.diezsiete.lscapp.data.prefs.PreferencesHelper;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Interface para acceder a los datos
 */
@Singleton
public class DataManager {

    private ApiHelper mApiHelper;
    private PreferencesHelper mPreferencesHelper;

    @Inject
    DataManager(DbHelper dbHelper,
                PreferencesHelper preferencesHelper,
                ApiHelper apiHelper) {
        mApiHelper = apiHelper;
        mPreferencesHelper = preferencesHelper;
    }

    private <T> DataManagerResponse<T> createDefaultResponse(final DataManagerResponse<T> callback) {
        return new DataManagerResponse<T>() {
            @Override
            public void onResponse(T response) {
                callback.onResponse(response);
            }
            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        };
    }

    public void getLevels(final DataManagerResponse<Level[]> callback) {
        mApiHelper.getLevels(createDefaultResponse(callback));
    }

    public void getLessonsByLevelId(String levelId, final DataManagerResponse<Lesson[]> callback) {
        mApiHelper.getLessonsByLevelId(levelId, createDefaultResponse(callback));
    }

    /**
     *
     * @param levelId
     * @param callback
     * @deprecated
     */
    public void getPracticesByLevel(String levelId, final DataManagerResponse<Practice[]> callback) {
        mApiHelper.getPracticesByLevel(levelId, createDefaultResponse(callback));
    }

    public void getPracticesByLessonId(String lessonId, final DataManagerResponse<Practice[]> callback) {
        mApiHelper.getPracticesByLessonId(lessonId, createDefaultResponse(callback));
    }

    public void getWords(final DataManagerResponse<Word[]> callback) {
        mApiHelper.getWords(createDefaultResponse(callback));
    }

    public void cntk(String tag, File photo, DataManagerResponse<Boolean> callback) {
        mApiHelper.cntk(tag, photo, createDefaultResponse(callback));
    }

    public void createUser(String email, String password, String passwordConfirm, DataManagerResponse<User> callback) {
        User user = new User(email, password, passwordConfirm);
        mApiHelper.createUser(user, createDefaultResponse(callback));
    }

    public void setAccessToken(String accessToken) {
        //mPreferencesHelper.setAccessToken(accessToken);
        //mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    public void setCurrentLevel(Level level) {
        mPreferencesHelper.setCurrentLevelId(level.getLevelId());
    }

    public void getCurrentLevel(DataManagerResponse<Level> callback) {
        String levelId = mPreferencesHelper.getCurrentLevelId();
        mApiHelper.getLevel(levelId, createDefaultResponse(callback));
    }

    public void setCurrentLesson(Lesson lesson) {
        mPreferencesHelper.setCurrentLessonId(lesson.getLessonId());
    }

    public String getCurrentLessonId() {
        return mPreferencesHelper.getCurrentLessonId();
    }


}

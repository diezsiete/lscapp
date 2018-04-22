package com.diezsiete.lscapp.data;

import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.data.db.model.User;
import com.diezsiete.lscapp.data.db.model.Word;
import com.diezsiete.lscapp.data.network.ApiHelper;

import java.io.File;


/**
 * Interface para acceder a los datos
 */

public class DataManager {


    private static <T> DataManagerResponse<T> createDefaultResponse(final DataManagerResponse<T> callback) {
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

    public static void getLevels(final DataManagerResponse<Level[]> callback) {
        ApiHelper.getLevels(createDefaultResponse(callback));
    }

    public static void getPracticesByLevel(String levelId, final DataManagerResponse<Practice[]> callback) {
        ApiHelper.getPracticesByLevel(levelId, createDefaultResponse(callback));
    }

    public static void getWords(final DataManagerResponse<Word[]> callback) {
        ApiHelper.getWords(createDefaultResponse(callback));
    }

    public static void cntk(String tag, File photo, DataManagerResponse<Boolean> callback) {
        ApiHelper.cntk(tag, photo, createDefaultResponse(callback));
    }

    public static void createUser(String email, String password, String passwordConfirm, DataManagerResponse<User> callback) {
        User user = new User(email, password, passwordConfirm);
        ApiHelper.createUser(user, createDefaultResponse(callback));
    }

}

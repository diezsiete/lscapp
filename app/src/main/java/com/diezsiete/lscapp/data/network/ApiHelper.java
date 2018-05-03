package com.diezsiete.lscapp.data.network;


import android.util.Log;

import com.diezsiete.lscapp.LSCApp;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.data.DataManagerResponse;
import com.diezsiete.lscapp.data.db.model.Lesson;
import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.data.db.model.User;
import com.diezsiete.lscapp.data.db.model.Word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class ApiHelper {

    private Api api;

    @Inject
    ApiHelper(){

    }

    private Api buildApi() {
        if(api == null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LSCApp.getContext().getResources().getString(R.string.rest_base_url))
                    .addConverterFactory(GsonConverterFactory.create());
            api = builder.build().create(Api.class);
        }
        return api;
    }

    private <T> Callback<T> createDefaultResponse(final DataManagerResponse<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()) {
                    callback.onResponse(response.body());
                }else{
                    switch(response.code()){
                        case 404:
                            break;
                        case 500:
                            break;
                        default:
                    }
                    //callback.onFailure(t);
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure(t);
            }
        };
    }


    public void getLevels(final DataManagerResponse<Level[]> callback) {
        buildApi().getLevels().enqueue(createDefaultResponse(callback));
    }

    public void getLevel(String levelId, final DataManagerResponse<Level> callback) {
        buildApi().getLevel(levelId).enqueue(createDefaultResponse(callback));
    }

    public void getPractice(String practiceId, final DataManagerResponse<Practice> callback) {
        buildApi().getPractice(practiceId).enqueue(createDefaultResponse(callback));
    }

    /**
     *
     * @param levelId
     * @param callback
     * @deprecated
     */
    public void getPracticesByLevel(String levelId, final DataManagerResponse<Practice[]> callback) {
        getLevel(levelId, new DataManagerResponse<Level>() {
            @Override
            public void onResponse(final Level level) {
                final List<Practice> practices = new ArrayList<>();

                final DataManagerResponse<Practice> innerCallback = new DataManagerResponse<Practice>() {
                    private boolean failure = false;
                    @Override
                    public void onResponse(Practice response) {
                        if(!failure) {
                            practices.add(response);
                            if (practices.size() < level.getPractices().length)
                                getPractice(level.getPractices()[practices.size()], this);
                            else
                                callback.onResponse(practices.toArray(new Practice[0]));
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        if(!failure){
                            failure = true;
                            callback.onFailure(t);
                        }
                    }
                };
                getPractice(level.getPractices()[practices.size()], innerCallback);
            }
            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getPracticesByLessonId(String lessonId, final DataManagerResponse<Practice[]> callback) {
        buildApi().getPractiesByLessonId(lessonId).enqueue(createDefaultResponse(callback));
    }

    public void getLessonsByLevelId(String levelId, final DataManagerResponse<Lesson[]> callback) {
        buildApi().getLessonsByLevelId(levelId).enqueue(createDefaultResponse(callback));
    }

    public void getWords(final DataManagerResponse<Word[]> callback) {
        buildApi().getWords().enqueue(createDefaultResponse(callback));
    }



    public void cntk(String tag, File file, final DataManagerResponse<Boolean> callback) {
        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part formData = MultipartBody.Part.createFormData("photo", file.getName(), filePart);

        buildApi().cntk(tag, formData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("JOSE", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("JOSE Upload error:", t.getMessage());
            }
        });
    }

    public void createUser(User user, final DataManagerResponse<User> callback) {
        buildApi().createUser(user).enqueue(createDefaultResponse(callback));
    }



}

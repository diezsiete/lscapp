package com.diezsiete.lscapp.rest;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.diezsiete.lscapp.LscApp;
import com.diezsiete.lscapp.R;
import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.JsonAttributes;
import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.model.practice.DiscoverImagePractice;
import com.diezsiete.lscapp.model.practice.Practice;
import com.diezsiete.lscapp.model.practice.ShowSignPractice;
import com.diezsiete.lscapp.model.practice.TakePicturePractice;
import com.diezsiete.lscapp.model.practice.TranslateVideoPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideoPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideosPractice;
import com.diezsiete.lscapp.utils.JsonHelper;
import com.diezsiete.lscapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Encargado de retornar la información ya sea del servidor o localmente
 */

public class ProxyApp {
    /**
     * TODO se podria usar un singleton?
     * @return
     */
    private static Retrofit buildRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(LscApp.getContext().getResources().getString(R.string.rest_base_url))
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    private static LSCAppClient getLSCAppClient() {
        return buildRetrofit().create(LSCAppClient.class);
    }

    public void uploadPhoto(String answer, File file) {
        RequestBody answerPart = RequestBody.create(MultipartBody.FORM, answer);

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part formData = MultipartBody.Part.createFormData("photo", file.getName(), filePart);

        Retrofit retrofit = buildRetrofit();

        LSCAppClient client = retrofit.create(LSCAppClient.class);


        Call<ResponseBody> call = client.uploadPhoto(answerPart, formData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("JOSE", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("JOSE Upload error:", t.getMessage());
            }
        });
    }

    public static void getDictionary(final LSCResponse<Concept[]> lscResponse) {
        Retrofit retrofit = buildRetrofit();
        LSCAppClient lscAppClient = retrofit.create(LSCAppClient.class);
        Call<Concept[]> call = lscAppClient.getDictionary();
        call.enqueue(new Callback<Concept[]>() {
            @Override
            public void onResponse(Call<Concept[]> call, Response<Concept[]> response) {
                lscResponse.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Concept[]> call, Throwable t) {
                t.printStackTrace();
                lscResponse.onFailure();
            }
        });
    }

    public static void getLevels(final LSCResponse<Level[]> lscResponse) {
        Retrofit retrofit = buildRetrofit();
        LSCAppClient lscAppClient = retrofit.create(LSCAppClient.class);
        Call<Level[]> call = lscAppClient.getLevels();
        call.enqueue(new Callback<Level[]>() {
            @Override
            public void onResponse(Call<Level[]> call, Response<Level[]> response) {
                lscResponse.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Level[]> call, Throwable t) {
                t.printStackTrace();
                lscResponse.onFailure();
            }
        });
    }

    public static void getPractices(String levelId) {
        Retrofit retrofit = buildRetrofit();
        LSCAppClient lscAppClient = retrofit.create(LSCAppClient.class);
        Call<Level> call = lscAppClient.getLevel(levelId);
        call.enqueue(new Callback<Level>() {
            @Override
            public void onResponse(Call<Level> call, Response<Level> response) {
                final Level level = response.body();
                Log.d("JOSE", "getPractices level.title : " + level.getTitle());
                for(String practiceId : level.getPractices()){
                    Log.d("JOSE", "getPractices practiceId : " + practiceId);
                    getPractice(practiceId, new LSCResponse<Practice>() {
                        @Override
                        public void onResponse(Practice response) {

                        }
                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Level> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void getLevel(String levelId, final LSCResponse<Level> lscResponse) {
        getLSCAppClient().getLevel(levelId).enqueue(new Callback<Level>() {
            @Override
            public void onResponse(Call<Level> call, Response<Level> response) {
                lscResponse.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Level> call, Throwable t) {
                t.printStackTrace();
                lscResponse.onFailure();
            }
        });
    }


    public static void getPractice(String practiceId, final LSCResponse<Practice> lscResponse) {
        getLSCAppClient().getPractice(practiceId).enqueue(new Callback<Practice>() {
            @Override
            public void onResponse(Call<Practice> call, Response<Practice> response) {
                lscResponse.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Practice> call, Throwable t) {
                t.printStackTrace();
                lscResponse.onFailure();
            }
        });

    }

    public interface LSCResponse <T> {
        void onResponse(T response);
        void onFailure();
    }
}

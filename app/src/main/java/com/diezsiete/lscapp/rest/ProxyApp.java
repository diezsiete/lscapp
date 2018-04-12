package com.diezsiete.lscapp.rest;

import android.util.Log;
import android.widget.Toast;

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

    public void uploadPhoto(String answer, File file) {
        RequestBody answerPart = RequestBody.create(MultipartBody.FORM, answer);

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part formData = MultipartBody.Part.createFormData("photo", file.getName(), filePart);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://lscapp.pta.com.co")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

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
}

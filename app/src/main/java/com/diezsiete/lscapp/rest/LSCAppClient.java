package com.diezsiete.lscapp.rest;


import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.practice.Practice;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface LSCAppClient {

    @GET("/dictionary")
    Call<Concept[]> getDictionary();


    @GET("/practices/{levelId}")
    Call<List<Practice>> getPractices(@Path("levelId") String levelId);

    //TODO mirar si RequestBody se puede reemplazar por String y response por Boolean
    @Multipart
    @POST("/practices/upload")
    Call<ResponseBody> uploadPhoto(
        @Part("answer") RequestBody answer,
        @Part MultipartBody.Part photo
    );
}

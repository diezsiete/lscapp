package com.diezsiete.lscapp.data.network;


import com.diezsiete.lscapp.data.db.model.Level;
import com.diezsiete.lscapp.data.db.model.Practice;
import com.diezsiete.lscapp.data.db.model.User;
import com.diezsiete.lscapp.data.db.model.Word;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * TODO: documentation
 */

public interface Api {

    @POST("/user")
    Call<User> createUser(@Body User user);

    @GET("/level")
    Call<Level[]> getLevels();

    @GET("/level/{levelId}")
    Call<Level> getLevel(@Path("levelId") String levelId);

    @GET("/practice/{practiceId}")
    Call<Practice> getPractice(@Path("practiceId") String practiceId);

    @GET("/word")
    Call<Word[]> getWords();

    //TODO mirar si RequestBody se puede reemplazar por String y response por Boolean
    @Multipart
    @POST("/cntk/{tag}")
    Call<ResponseBody> cntk(
            @Path("tag") String tag,
            @Part MultipartBody.Part photo
    );
}

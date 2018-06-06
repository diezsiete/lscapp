package com.diezsiete.lscapp.remote;



import android.arch.lifecycle.LiveData;

import com.diezsiete.lscapp.db.entity.AchievementEntity;
import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.db.entity.LevelEntity;
import com.diezsiete.lscapp.db.entity.UserEntity;
import com.diezsiete.lscapp.vo.Authentication;
import com.diezsiete.lscapp.db.entity.Practice;
import com.diezsiete.lscapp.vo.TakeSignResponse;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {
    @FormUrlEncoded
    @POST("/login")
    LiveData<ApiResponse<Authentication>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/user")
    LiveData<ApiResponse<Authentication>> registerUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword
    );

    @GET("/profile/{profileId}")
    LiveData<ApiResponse<UserEntity>> getProfile(@Path("profileId") String profileId);

    @GET("/level")
    LiveData<ApiResponse<List<LevelEntity>>> getLevels();

    @GET("/lesson/{levelId}")
    LiveData<ApiResponse<List<LessonEntity>>> getLessonsByLevelId(@Path("levelId") String levelId);

    @GET("/practice/{lessonId}")
    LiveData<ApiResponse<List<Practice>>> getPracticesWithDataByLessonId(@Path("lessonId") String lessonId);

    @GET("/word")
    LiveData<ApiResponse<List<WordEntity>>> getWords();

    @GET("/word/{word}")
    LiveData<ApiResponse<WordEntity>> getWord(@Path("word") String word);

    @GET("/achievement")
    LiveData<ApiResponse<List<AchievementEntity>>> getAchievements();


    @Multipart
    @POST("/cntk/{tag}")
    Call<TakeSignResponse> uploadPhoto(
        @Path("tag") String tag,
        @Part MultipartBody.Part photo
    );

    @FormUrlEncoded
    @PUT("/profile/{profileId}")
    LiveData<ApiResponse<UserEntity>> putProfile(
            @Path("profileId") String profileId,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword,
            @Field("completedLesson") String completedLesson);


}

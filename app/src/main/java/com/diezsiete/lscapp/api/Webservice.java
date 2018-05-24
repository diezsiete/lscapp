package com.diezsiete.lscapp.api;



import android.arch.lifecycle.LiveData;

import com.diezsiete.lscapp.db.entity.User;
import com.diezsiete.lscapp.vo.Authentication;
import com.diezsiete.lscapp.vo.CompletedLesson;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.TakeSignResponse;
import com.diezsiete.lscapp.vo.Word;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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

public interface Webservice {
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
    LiveData<ApiResponse<User>> getProfile(@Path("profileId") String profileId);

    @GET("/level")
    LiveData<ApiResponse<List<Level>>> getLevels();

    @GET("/lesson/{levelId}")
    LiveData<ApiResponse<List<Lesson>>> getLessonsByLevelId(@Path("levelId") String levelId);

    @GET("/practice/{lessonId}")
    LiveData<ApiResponse<List<PracticeWithData>>> getPracticesWithDataByLessonId(@Path("lessonId") String lessonId);

    @GET("/word")
    LiveData<ApiResponse<List<Word>>> getWords();

    @GET("/word/{word}")
    LiveData<ApiResponse<Word>> getWord(@Path("word") String word);


    @Multipart
    @POST("/cntk/{tag}")
    Call<TakeSignResponse> uploadPhoto(
        @Path("tag") String tag,
        @Part MultipartBody.Part photo
    );


    @PUT("/profile/{profileId}")
    LiveData<ApiResponse<User>> putCompletedLesson(
            @Path("profileId") String profileId, @Body CompletedLesson completedLesson);

}

package com.diezsiete.lscapp.api;



import android.arch.lifecycle.LiveData;

import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.TakeSignResponse;
import com.diezsiete.lscapp.vo.Word;

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

public interface Webservice {
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

}

package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.db.LSCAppDb;
import com.diezsiete.lscapp.db.dao.PracticeDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosWordDao;
import com.diezsiete.lscapp.db.dao.PracticeWordsDao;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.db.entity.PracticeEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideos;
import com.diezsiete.lscapp.db.entity.PracticeVideosWord;
import com.diezsiete.lscapp.db.entity.Practice;
import com.diezsiete.lscapp.db.entity.PracticeWordsEntity;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.vo.TakeSignResponse;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class PracticeRepository {
    private final Api api;
    private final LSCAppDb db;
    private final PracticeDao practiceDao;
    private final PracticeWordsDao practiceWordsDao;
    private final PracticeVideosDao practiceVideosDao;
    private final PracticeVideosWordDao practiceVideosWordDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    PracticeRepository(
            Api api, LSCAppDb db, PracticeDao practiceDao,
            PracticeWordsDao practiceWordsDao, PracticeVideosDao practiceVideosDao,
            PracticeVideosWordDao practiceVideosWordDao,
            AppExecutors appExecutors) {
        this.api = api;
        this.db = db;
        this.practiceDao = practiceDao;
        this.practiceWordsDao = practiceWordsDao;
        this.practiceVideosDao = practiceVideosDao;
        this.practiceVideosWordDao = practiceVideosWordDao;
        this.appExecutors = appExecutors;
    }

    private void updatePracticesFromCall(List<Practice> practices, String lessonId) {
        db.beginTransaction();
        try {
            for (Practice practice : practices) {
                PracticeEntity practiceEntity = practice.entity;
                practiceEntity.lessonId = lessonId;
                long practiceId =practiceDao.insert(practiceEntity);
                for(PracticeWordsEntity practiceWordsEntity : practice.words) {
                    practiceWordsEntity.practiceId = practiceId;
                    practiceWordsDao.insert(practiceWordsEntity);
                }

                for(PracticeVideos practiceVideos : practice.videos){
                    practiceVideos.entity.practiceId = practiceId;
                    long id = practiceVideosDao.insert(practiceVideos.entity);
                    for(PracticeVideosWord practiceVideosWord : practiceVideos.videosWord){
                        practiceVideosWord.entity.practiceVideosId = id;
                        practiceVideosWordDao.insert(practiceVideosWord.entity);
                    }
                }
            }
            db.setTransactionSuccessful();
        }
        catch(Exception e){
            Log.d("JOSE", e.getMessage());
        }
        finally {
            db.endTransaction();
        }
    }


    public LiveData<Resource<List<Practice>>> loadPracticesWithDataByLessonId(String lessonId) {
        return new NetworkBoundResource<List<Practice>, List<Practice>>(appExecutors) {
            private MediatorLiveData<List<Practice>> practicesMediator;

            @Override
            protected void saveCallResult(@NonNull List<Practice> practices) {
                updatePracticesFromCall(practices, lessonId);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Practice> data) {
                return true;
                //return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("practice");
            }

            @NonNull
            @Override
            protected LiveData<List<Practice>> loadFromDb() {
                return practiceDao.loadPracticesWithDataByLessonId(lessonId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Practice>>> createCall() {
                return api.getPracticesWithDataByLessonId(lessonId);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("practice-"+lessonId);
            }
        }.asLiveData();

    }

    public LiveData<List<String>> getPracticesCodes(List<Integer> ids) {
        return practiceDao.getPracticesCodes(ids);
    }

    public LiveData<Practice> getPracticeWithData(int practiceId) {
        return practiceDao.loadPracticeWithData(practiceId);
    }

    public void updatePractice(PracticeEntity practiceEntity) {
        appExecutors.diskIO().execute(() -> practiceDao.update(practiceEntity));
    }

    public void updatePractice(PracticeEntity practiceEntity, Runnable runnable) {
        appExecutors.diskIO().execute(() -> {
            practiceDao.update(practiceEntity);
            appExecutors.mainThread().execute(runnable);
        });
    }

    public void deletePracticesByLessonId(String lessonId, Runnable runnable) {
        appExecutors.diskIO().execute(() -> {
            practiceDao.deleteByLessonId(lessonId);
            appExecutors.mainThread().execute(runnable);
        });
    }


    public void postCntk(final Practice practice, String tag, File file) {
        practice.setUserAnswer(2);
        updatePractice(practice.entity);

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part formData = MultipartBody.Part.createFormData("file", file.getName(), filePart);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://18.208.61.61:12345")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api client = retrofit.create(Api.class);


        Call<TakeSignResponse> call = client.uploadPhoto(tag, formData);
        call.enqueue(new Callback<TakeSignResponse>() {
            @Override
            public void onResponse(Call<TakeSignResponse> call, Response<TakeSignResponse> response) {
                ApiResponse<TakeSignResponse> apiResponse = new ApiResponse<>(response);
                if(apiResponse.isSuccessful()){
                    TakeSignResponse tsResponse = apiResponse.body;
                    Log.d("CNTK", "["+tag+"]SUCCESS : hit["+tsResponse.hit +"], probability["+tsResponse.probability+"]");
                    practice.setUserAnswer(tsResponse.hit ? 1 : 0);
                    updatePractice(practice.entity);
                }else{
                    Log.e("CNTK", "["+tag+"]ERROR : " + apiResponse.errorMessage);
                    practice.setUserAnswer(1);
                    updatePractice(practice.entity);
                }

            }

            @Override
            public void onFailure(Call<TakeSignResponse> call, Throwable t) {
                Log.e("JOSE Upload error:", t.getMessage());
                practice.setUserAnswer(3);
                updatePractice(practice.entity);
            }
        });
    }
}

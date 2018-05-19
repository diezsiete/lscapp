package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.LSCApp;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.LSCAppDb;
import com.diezsiete.lscapp.db.PracticeDao;
import com.diezsiete.lscapp.db.PracticeVideosDao;
import com.diezsiete.lscapp.db.PracticeVideosWordDao;
import com.diezsiete.lscapp.db.PracticeWordsDao;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeVideosData;
import com.diezsiete.lscapp.vo.PracticeVideosWord;
import com.diezsiete.lscapp.vo.PracticeVideosWordData;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.PracticeWords;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.vo.ShowSign;
import com.diezsiete.lscapp.vo.TakeSignResponse;
import com.diezsiete.lscapp.vo.WhichOneVideo;
import com.diezsiete.lscapp.vo.WhichOneVideos;
import com.diezsiete.lscapp.vo.Word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
public class PracticeRepository {
    private final Webservice webservice;
    private final LSCAppDb db;
    private final PracticeDao practiceDao;
    private final PracticeWordsDao practiceWordsDao;
    private final PracticeVideosDao practiceVideosDao;
    private final PracticeVideosWordDao practiceVideosWordDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    PracticeRepository(
            Webservice webservice, LSCAppDb db, PracticeDao practiceDao,
            PracticeWordsDao practiceWordsDao, PracticeVideosDao practiceVideosDao,
            PracticeVideosWordDao practiceVideosWordDao,
            AppExecutors appExecutors) {
        this.webservice = webservice;
        this.db = db;
        this.practiceDao = practiceDao;
        this.practiceWordsDao = practiceWordsDao;
        this.practiceVideosDao = practiceVideosDao;
        this.practiceVideosWordDao = practiceVideosWordDao;
        this.appExecutors = appExecutors;
    }

    private void updatePracticesFromCall(List<PracticeWithData> practices, String lessonId) {
        db.beginTransaction();
        try {
            for (PracticeWithData practiceWithData : practices) {
                Practice practice = practiceWithData.entity;
                practice.lessonId = lessonId;
                long practiceId =practiceDao.insert(practice);
                for(PracticeWords practiceWords  : practiceWithData.words) {
                    practiceWords.practiceId = practiceId;
                    practiceWordsDao.insert(practiceWords);
                }

                for(PracticeVideosData practiceVideos : practiceWithData.videos){
                    practiceVideos.entity.practiceId = practiceId;
                    long id = practiceVideosDao.insert(practiceVideos.entity);
                    for(PracticeVideosWordData practiceVideosWord : practiceVideos.videosWord){
                        Log.d("JOSE", "PracticeRepository.updatePracticesFromCall Word : " + practiceVideosWord.entity.wordId);
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


    public LiveData<Resource<List<PracticeWithData>>> loadPracticesWithDataByLessonId(String lessonId) {
        return new NetworkBoundResource<List<PracticeWithData>, List<PracticeWithData>>(appExecutors) {
            private MediatorLiveData<List<PracticeWithData>> practicesMediator;

            @Override
            protected void saveCallResult(@NonNull List<PracticeWithData> practices) {
                updatePracticesFromCall(practices, lessonId);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PracticeWithData> data) {
                return true;
                //return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("practice");
            }

            @NonNull
            @Override
            protected LiveData<List<PracticeWithData>> loadFromDb() {
                return practiceDao.loadPracticesWithDataByLessonId(lessonId);
                /*return new BoundPracticeCode() {
                    @NonNull
                    @Override
                    protected LiveData<List<PracticeWithData>> loadFromDb() {
                        return practiceDao.loadPracticesWithDataByLessonId(lessonId);
                    }
                }.asLiveData();
                LiveData<List<PracticeWithData>> dbSource = practiceDao.loadPracticesWithDataByLessonId(lessonId);
                if(practicesMediator == null){
                    practicesMediator = new MediatorLiveData<>();
                    practicesMediator.addSource(dbSource, data -> {
                        List<PracticeWithData> newList = new ArrayList<>();
                        for(PracticeWithData practiceWithData : data)
                            newList.add(instanceByCode(practiceWithData));
                        practicesMediator.setValue(newList);
                    });
                }
                return practicesMediator;*/
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<PracticeWithData>>> createCall() {
                return webservice.getPracticesWithDataByLessonId(lessonId);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("practice-"+lessonId);
            }
        }.asLiveData();

    }

    private PracticeWithData instanceByCode(PracticeWithData oldPracticeWithData){
        switch (oldPracticeWithData.entity.code){
            case "show-sign" :
                return new ShowSign(oldPracticeWithData);
            case "which-one-video" :
                return new WhichOneVideo(oldPracticeWithData);
            case "which-one-videos" :
                return new WhichOneVideos(oldPracticeWithData);
            default:
                return oldPracticeWithData;
        }
    }

    public LiveData<List<String>> getPracticesCodes(List<Integer> ids) {
        return practiceDao.getPracticesCodes(ids);
    }

    public LiveData<PracticeWithData> getPracticeWithData(int practiceId) {
        return practiceDao.loadPracticeWithData(practiceId);
    }

    public void updatePractice(Practice practice) {
        appExecutors.diskIO().execute(() -> practiceDao.update(practice));
    }

    public void updatePractice(Practice practice, Runnable runnable) {
        appExecutors.diskIO().execute(() -> {
            practiceDao.update(practice);
            appExecutors.mainThread().execute(runnable);
        });
    }

    public void deletePracticesByLessonId(String lessonId, Runnable runnable) {
        appExecutors.diskIO().execute(() -> {
            practiceDao.deleteByLessonId(lessonId);
            appExecutors.mainThread().execute(runnable);
        });
    }


    public void postCntk(final PracticeWithData practice, String tag, File file) {
        practice.setUserAnswer(2);
        updatePractice(practice.entity);

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part formData = MultipartBody.Part.createFormData("file", file.getName(), filePart);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://18.208.61.61:12345")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Webservice client = retrofit.create(Webservice.class);


        Call<TakeSignResponse> call = client.uploadPhoto(tag, formData);
        call.enqueue(new Callback<TakeSignResponse>() {
            @Override
            public void onResponse(Call<TakeSignResponse> call, Response<TakeSignResponse> response) {
                ApiResponse<TakeSignResponse> apiResponse = new ApiResponse<>(response);
                if(apiResponse.isSuccessful()){
                    TakeSignResponse tsResponse = apiResponse.body;
                    practice.setUserAnswer(tsResponse.hit ? 1 : 0);
                    updatePractice(practice.entity);
                }else{
                    Log.e("CNTK", "ERROR : " + apiResponse.errorMessage);
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

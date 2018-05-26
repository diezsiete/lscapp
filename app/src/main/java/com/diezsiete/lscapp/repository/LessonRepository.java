package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.db.dao.LessonDao;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LessonRepository {
    private final Api api;
    private final LessonDao lessonDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    LessonRepository(Api api, LessonDao lessonDao, AppExecutors appExecutors) {
        this.api = api;
        this.lessonDao = lessonDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<LessonEntity>>> loadLessonsByLevelId(String levelId) {
        return new NetworkBoundResource<List<LessonEntity>, List<LessonEntity>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<LessonEntity> item) {
                for(LessonEntity lessonEntity : item)
                    lessonEntity.levelId = levelId;
                lessonDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<LessonEntity> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("lessonEntity");
            }

            @NonNull
            @Override
            protected LiveData<List<LessonEntity>> loadFromDb() {
                return lessonDao.loadLessonsByLevelId(levelId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<LessonEntity>>> createCall() {
                return api.getLessonsByLevelId(levelId);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("lessonEntity-"+levelId);
            }
        }.asLiveData();
    }

    public LiveData<LessonEntity> getLesson(String lessonId) {
        return lessonDao.loadLesson(lessonId);
    }

    public void update(LessonEntity lessonEntity) {
        appExecutors.diskIO().execute(() -> lessonDao.update(lessonEntity));
    }

    public void updateLessonProgress(String lessonId, int progress) {
        appExecutors.diskIO().execute(() -> lessonDao.updateLessonProgress(lessonId, progress));
    }

}

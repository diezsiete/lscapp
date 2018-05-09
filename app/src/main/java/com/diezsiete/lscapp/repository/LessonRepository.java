package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.LessonDao;
import com.diezsiete.lscapp.db.LevelDao;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LessonRepository {
    private final Webservice webservice;
    private final LessonDao lessonDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    LessonRepository(Webservice webservice, LessonDao lessonDao, AppExecutors appExecutors) {
        this.webservice = webservice;
        this.lessonDao = lessonDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<Lesson>>> loadLessonsByLevelId(String levelId) {
        return new NetworkBoundResource<List<Lesson>, List<Lesson>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Lesson> item) {
                for(Lesson lesson : item)
                    lesson.levelId = levelId;
                lessonDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Lesson> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("lesson");
            }

            @NonNull
            @Override
            protected LiveData<List<Lesson>> loadFromDb() {
                return lessonDao.loadLessonsByLevelId(levelId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Lesson>>> createCall() {
                return webservice.getLessonsByLevelId(levelId);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("lesson-"+levelId);
            }
        }.asLiveData();
    }

    public LiveData<Lesson> getLesson(String lessonId) {
        return lessonDao.loadLesson(lessonId);
    }

    public void update(Lesson lesson) {
        appExecutors.diskIO().execute(() -> lessonDao.update(lesson));
    }

    public void updateLessonProgress(String lessonId, int progress) {
        appExecutors.diskIO().execute(() -> lessonDao.updateLessonProgress(lessonId, progress));
    }

}

package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.LevelDao;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelRepository {
    private final Webservice webservice;
    private final LevelDao levelDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    LevelRepository(Webservice webservice, LevelDao levelDao, AppExecutors appExecutors) {
        this.webservice = webservice;
        this.levelDao = levelDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<Level>>> loadLevels() {
        return new NetworkBoundResource<List<Level>, List<Level>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Level> item) {
                levelDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Level> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("level");
            }

            @NonNull
            @Override
            protected LiveData<List<Level>> loadFromDb() {
                return levelDao.loadLevels();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Level>>> createCall() {
                return webservice.getLevels();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("level");
            }
        }.asLiveData();
    }

    public LiveData<Resource<Level>> loadLevel(String levelId) {
        return new NetworkBoundResource<Level, Level>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Level item) {
                levelDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Level data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Level> loadFromDb() {
                return levelDao.load(levelId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Level>> createCall() {
                return AbsentLiveData.create();
            }

        }.asLiveData();
    }
}

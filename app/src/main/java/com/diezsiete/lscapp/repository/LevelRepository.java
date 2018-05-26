package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.db.entity.LevelEntity;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.db.dao.LevelDao;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelRepository {
    private final Api api;
    private final LevelDao levelDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    LevelRepository(Api api, LevelDao levelDao, AppExecutors appExecutors) {
        this.api = api;
        this.levelDao = levelDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<LevelEntity>>> loadLevels() {
        return new NetworkBoundResource<List<LevelEntity>, List<LevelEntity>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<LevelEntity> item) {
                levelDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<LevelEntity> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("level");
            }

            @NonNull
            @Override
            protected LiveData<List<LevelEntity>> loadFromDb() {
                return levelDao.loadLevels();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<LevelEntity>>> createCall() {
                return api.getLevels();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("level");
            }
        }.asLiveData();
    }

    public LiveData<Resource<LevelEntity>> loadLevel(String levelId) {
        return new NetworkBoundResource<LevelEntity, LevelEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull LevelEntity item) {
                levelDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable LevelEntity data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<LevelEntity> loadFromDb() {
                return levelDao.load(levelId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<LevelEntity>> createCall() {
                return AbsentLiveData.create();
            }

        }.asLiveData();
    }
}

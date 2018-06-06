package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.db.dao.AchievementDao;
import com.diezsiete.lscapp.db.dao.WordDao;
import com.diezsiete.lscapp.db.entity.AchievementEntity;
import com.diezsiete.lscapp.db.entity.WordEntity;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AchievementRepository {
    private final Api api;
    private final AchievementDao achievementDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    AchievementRepository(Api api, AchievementDao achievementDao, AppExecutors appExecutors) {
        this.api = api;
        this.achievementDao = achievementDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<AchievementEntity>>> loadAll() {
        return new NetworkBoundResource<List<AchievementEntity>, List<AchievementEntity>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<AchievementEntity> item) {
                achievementDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<AchievementEntity> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("achievementEntity");
            }

            @NonNull
            @Override
            protected LiveData<List<AchievementEntity>> loadFromDb() {
                return achievementDao.loadAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<AchievementEntity>>> createCall() {
                return api.getAchievements();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("wordEntities");
            }
        }.asLiveData();
    }
}

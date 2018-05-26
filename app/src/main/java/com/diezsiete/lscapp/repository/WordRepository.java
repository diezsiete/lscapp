package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.db.dao.WordDao;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WordRepository {
    private final Api api;
    private final WordDao wordDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    WordRepository(Api api, WordDao wordDao, AppExecutors appExecutors) {
        this.api = api;
        this.wordDao = wordDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<WordEntity>>> loadWords() {
        return new NetworkBoundResource<List<WordEntity>, List<WordEntity>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<WordEntity> item) {
                wordDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<WordEntity> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("wordEntities");
            }

            @NonNull
            @Override
            protected LiveData<List<WordEntity>> loadFromDb() {
                return wordDao.loadAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<WordEntity>>> createCall() {
                return api.getWords();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("wordEntities");
            }
        }.asLiveData();
    }



    public LiveData<Resource<WordEntity>> loadWord(String word) {
        return new NetworkBoundResource<WordEntity, WordEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WordEntity item) {
                wordDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable WordEntity data) {
                return data == null || repoListRateLimit.shouldFetch("word");
            }

            @NonNull
            @Override
            protected LiveData<WordEntity> loadFromDb() {
                return wordDao.load(word);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WordEntity>> createCall() {
                return api.getWord(word);
            }

        }.asLiveData();
    }
}

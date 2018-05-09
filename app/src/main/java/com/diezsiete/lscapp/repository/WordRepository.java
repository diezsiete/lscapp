package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.WordDao;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.vo.Word;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WordRepository {
    private final Webservice webservice;
    private final WordDao wordDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    WordRepository(Webservice webservice, WordDao wordDao, AppExecutors appExecutors) {
        this.webservice = webservice;
        this.wordDao = wordDao;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<Word>>> loadWords() {
        return new NetworkBoundResource<List<Word>, List<Word>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Word> item) {
                wordDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Word> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("words");
            }

            @NonNull
            @Override
            protected LiveData<List<Word>> loadFromDb() {
                return wordDao.loadAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Word>>> createCall() {
                return webservice.getWords();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset("words");
            }
        }.asLiveData();
    }



    public LiveData<Resource<Word>> loadWord(String word) {
        return new NetworkBoundResource<Word, Word>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Word item) {
                wordDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Word data) {
                return data == null || repoListRateLimit.shouldFetch("word");
            }

            @NonNull
            @Override
            protected LiveData<Word> loadFromDb() {
                return wordDao.load(word);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Word>> createCall() {
                return webservice.getWord(word);
            }

        }.asLiveData();
    }
}

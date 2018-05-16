package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.LevelDao;
import com.diezsiete.lscapp.db.UserDao;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Resource;
import com.diezsiete.lscapp.vo.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {
    private final Webservice webservice;
    private final UserDao userDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    UserRepository(Webservice webservice, UserDao userDao, AppExecutors appExecutors) {
        this.webservice = webservice;
        this.userDao = userDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<User> load() {
        return userDao.load();
    }

    public void create(String email, String password, String passwordConfirm, Runnable runnable) {
        this.appExecutors.diskIO().execute(() -> {
            userDao.insert(new User(email, email));
            appExecutors.mainThread().execute(runnable);
        });
    }
}

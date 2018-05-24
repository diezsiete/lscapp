package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.api.ApiResponse;
import com.diezsiete.lscapp.api.Webservice;
import com.diezsiete.lscapp.db.UserDao;
import com.diezsiete.lscapp.db.entity.User;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Authentication;
import com.diezsiete.lscapp.vo.CompletedLesson;
import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Resource;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Response;
import okhttp3.ResponseBody;

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

    public void delete() {
        this.appExecutors.diskIO().execute(userDao::deleteAll);
    }

    public LiveData<Resource<User>> register(String email, String password, String passwordConfirm) {
        return new NetworkBoundResource<User, Authentication>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Authentication item) {
                if(!item.profileId.isEmpty()){
                    userDao.insert(new User(item.profileId, email));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Authentication>> createCall() {
                return webservice.registerUser(email, password, passwordConfirm);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> login(String email, String password) {
        return new NetworkBoundResource<User, Authentication>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Authentication item) {
                if(!item.profileId.isEmpty()){
                    userDao.insert(new User(item.profileId, email));
                }
            }
            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Authentication>> createCall() {
                return webservice.login(email, password);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> getProfile(String profileId) {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.getProfile(profileId);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> putLessonCompleted(User user, Lesson lesson) {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                Log.d("JOSE", "OKS");
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.putCompletedLesson(user.profileId, new CompletedLesson(lesson.lessonId));
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
}

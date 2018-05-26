package com.diezsiete.lscapp.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.diezsiete.lscapp.AppExecutors;
import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.db.entity.UserEntity;
import com.diezsiete.lscapp.remote.Api;
import com.diezsiete.lscapp.remote.ApiResponse;
import com.diezsiete.lscapp.db.dao.UserDao;
import com.diezsiete.lscapp.util.RateLimiter;
import com.diezsiete.lscapp.vo.Authentication;
import com.diezsiete.lscapp.vo.CompletedLesson;
import com.diezsiete.lscapp.vo.Resource;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {
    private final Api api;
    private final UserDao userDao;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    UserRepository(Api api, UserDao userDao, AppExecutors appExecutors) {
        this.api = api;
        this.userDao = userDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<UserEntity> load() {
        return userDao.load();
    }

    public void delete() {
        this.appExecutors.diskIO().execute(userDao::deleteAll);
    }

    public LiveData<Resource<UserEntity>> register(String email, String password, String passwordConfirm) {
        return new NetworkBoundResource<UserEntity, Authentication>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Authentication item) {
                if(!item.profileId.isEmpty()){
                    userDao.insert(new UserEntity(item.profileId, email));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Authentication>> createCall() {
                return api.registerUser(email, password, passwordConfirm);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<UserEntity>> login(String email, String password) {
        return new NetworkBoundResource<UserEntity, Authentication>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Authentication item) {
                if(!item.profileId.isEmpty()){
                    userDao.insert(new UserEntity(item.profileId, email));
                }
            }
            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Authentication>> createCall() {
                return api.login(email, password);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<UserEntity>> getProfile(String profileId) {
        return new NetworkBoundResource<UserEntity, UserEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull UserEntity item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return true;
            }
            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return userDao.load();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserEntity>> createCall() {
                return api.getProfile(profileId);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<UserEntity>> putLessonCompleted(UserEntity userEntity, LessonEntity lessonEntity) {
        return new NetworkBoundResource<UserEntity, UserEntity>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull UserEntity item) {
                Log.d("JOSE", "OKS");
            }

            @Override
            protected boolean shouldFetch(@Nullable UserEntity data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UserEntity> loadFromDb() {
                return null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserEntity>> createCall() {
                return api.putCompletedLesson(userEntity.profileId, new CompletedLesson(lessonEntity.lessonId));
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
}

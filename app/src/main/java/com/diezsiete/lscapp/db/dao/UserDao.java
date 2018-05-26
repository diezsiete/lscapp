package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.UserEntity;

@Dao
public interface UserDao extends BaseDao<UserEntity> {
    @Query("SELECT * FROM user LIMIT 1")
    LiveData<UserEntity> load();

    @Query("DELETE FROM user")
    void deleteAll();
}

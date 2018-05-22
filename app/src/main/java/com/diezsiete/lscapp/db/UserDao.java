package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.User;

@Dao
public interface UserDao extends BaseDao<User> {
    @Query("SELECT * FROM user LIMIT 1")
    LiveData<User> load();
}

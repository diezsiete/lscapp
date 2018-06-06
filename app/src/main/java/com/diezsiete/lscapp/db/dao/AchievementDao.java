package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.AchievementEntity;
import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.List;

@Dao
public interface AchievementDao extends BaseDao<AchievementEntity> {
    @Query("DELETE FROM achievement")
    void deleteAll();

    @Query("SELECT * FROM achievement")
    LiveData<List<AchievementEntity>> loadAll();

}

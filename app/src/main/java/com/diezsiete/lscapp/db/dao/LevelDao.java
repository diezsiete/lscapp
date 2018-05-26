package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.LevelEntity;

import java.util.List;

@Dao
public interface LevelDao extends BaseDao<LevelEntity> {
    @Query("DELETE FROM level")
    void deleteAll();

    @Query("SELECT * from level ORDER BY `id` ASC")
    LiveData<List<LevelEntity>> loadLevels();

    @Query("SELECT * FROM level WHERE level_id = :levelId")
    LiveData<LevelEntity> load(String levelId);
}

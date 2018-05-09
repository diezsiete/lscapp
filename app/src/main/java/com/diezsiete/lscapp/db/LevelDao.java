package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.vo.Level;

import java.util.List;

@Dao
public interface LevelDao extends BaseDao<Level> {
    @Query("DELETE FROM level")
    void deleteAll();

    @Query("SELECT * from level ORDER BY `id` ASC")
    LiveData<List<Level>> loadLevels();

    @Query("SELECT * FROM level WHERE level_id = :levelId")
    LiveData<Level> load(String levelId);
}

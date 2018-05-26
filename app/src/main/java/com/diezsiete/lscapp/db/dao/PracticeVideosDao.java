package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.PracticeVideosEntity;

@Dao
public interface PracticeVideosDao extends BaseDao<PracticeVideosEntity> {
    @Query("DELETE FROM practice_videos")
    void deleteAll();

    @Query("SELECT * FROM practice_videos WHERE practice_id = :practiceId")
    LiveData<PracticeVideosEntity> load(String practiceId);

    @Query("DELETE FROM practice_videos WHERE practice_id = :practiceId")
    void deleteAllByPracticeId(String practiceId);
}

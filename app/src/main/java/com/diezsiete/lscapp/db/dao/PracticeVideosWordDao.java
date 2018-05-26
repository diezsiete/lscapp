package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.PracticeVideosWordEntity;

@Dao
public interface PracticeVideosWordDao extends BaseDao<PracticeVideosWordEntity> {
    @Query("DELETE FROM practice_videos_word")
    void deleteAll();

    @Query("SELECT * FROM practice_videos_word WHERE practice_videos_id = :practiceVideosId")
    LiveData<PracticeVideosWordEntity> load(String practiceVideosId);

    @Query("DELETE FROM practice_videos_word WHERE practice_videos_id = :practiceVideosId")
    void deleteAllByPracticeVideosId(String practiceVideosId);
}

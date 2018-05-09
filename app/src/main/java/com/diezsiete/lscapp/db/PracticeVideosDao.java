package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeWords;

import java.util.List;

@Dao
public interface PracticeVideosDao extends BaseDao<PracticeVideos> {
    @Query("DELETE FROM practice_videos")
    void deleteAll();

    @Query("SELECT * FROM practice_videos WHERE practice_id = :practiceId")
    LiveData<PracticeVideos> load(String practiceId);

    @Query("DELETE FROM practice_videos WHERE practice_id = :practiceId")
    void deleteAllByPracticeId(String practiceId);
}

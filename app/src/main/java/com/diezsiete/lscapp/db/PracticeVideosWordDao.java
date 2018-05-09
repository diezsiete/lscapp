package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeVideosWord;
import com.diezsiete.lscapp.vo.PracticeWords;

@Dao
public interface PracticeVideosWordDao extends BaseDao<PracticeVideosWord> {
    @Query("DELETE FROM practice_videos_word")
    void deleteAll();

    @Query("SELECT * FROM practice_videos_word WHERE practice_videos_id = :practiceVideosId")
    LiveData<PracticeVideosWord> load(String practiceVideosId);

    @Query("DELETE FROM practice_videos_word WHERE practice_videos_id = :practiceVideosId")
    void deleteAllByPracticeVideosId(String practiceVideosId);
}

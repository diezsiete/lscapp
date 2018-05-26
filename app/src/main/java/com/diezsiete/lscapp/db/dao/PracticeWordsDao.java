package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.PracticeWordsEntity;

@Dao
public interface PracticeWordsDao extends BaseDao<PracticeWordsEntity> {
    @Query("DELETE FROM practice_words")
    void deleteAll();

    @Query("SELECT * FROM practice_words WHERE practice_id = :practiceId")
    LiveData<PracticeWordsEntity> load(int practiceId);

    @Query("DELETE FROM practice_words WHERE practice_id = :practiceId")
    void deleteAllByPracticeId(int practiceId);
}

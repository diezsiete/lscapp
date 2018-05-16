package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.PracticeWords;

import java.util.List;

@Dao
public interface PracticeWordsDao extends BaseDao<PracticeWords> {
    @Query("DELETE FROM practice_words")
    void deleteAll();

    @Query("SELECT * FROM practice_words WHERE practice_id = :practiceId")
    LiveData<PracticeWords> load(int practiceId);

    @Query("DELETE FROM practice_words WHERE practice_id = :practiceId")
    void deleteAllByPracticeId(int practiceId);
}

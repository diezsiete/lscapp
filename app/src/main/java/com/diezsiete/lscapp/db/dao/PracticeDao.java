package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;


import com.diezsiete.lscapp.db.entity.PracticeEntity;
import com.diezsiete.lscapp.db.entity.Practice;

import java.util.List;

@Dao
public interface PracticeDao extends BaseDao<PracticeEntity> {
    @Query("DELETE FROM practice")
    void deleteAll();

    @Query("DELETE FROM practice where lesson_id = :lessonId")
    void deleteByLessonId(String lessonId);

    @Query("SELECT * FROM practice WHERE lesson_id = :lessonId ORDER BY id ASC")
    LiveData<List<PracticeEntity>> loadPracticesByLessonId(String lessonId);

    @Query("SELECT code FROM practice WHERE id IN (:ids)")
    LiveData<List<String>> getPracticesCodes(List<Integer> ids);

    @Transaction
    @Query("SELECT * FROM practice WHERE lesson_id = :lessonId ORDER BY id ASC")
    LiveData<List<Practice>> loadPracticesWithDataByLessonId(String lessonId);

    @Transaction
    @Query("SELECT * FROM practice WHERE id = :practiceId")
    LiveData<Practice> loadPracticeWithData(int practiceId);

}

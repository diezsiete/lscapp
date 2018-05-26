package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.db.entity.LessonEntity;

import java.util.List;

@Dao
public interface LessonDao extends BaseDao<LessonEntity> {
    @Query("DELETE FROM lesson")
    void deleteAll();

    @Query("SELECT * FROM lesson WHERE level_id = :levelId ORDER BY id ASC")
    LiveData<List<LessonEntity>> loadLessonsByLevelId(String levelId);

    @Query("SELECT * FROM lesson WHERE lesson_id = :lessonId")
    LiveData<LessonEntity> loadLesson(String lessonId);

    @Query("UPDATE lesson SET progress = :progress WHERE lesson_id = :lessonId")
    int updateLessonProgress(String lessonId, int progress);
}

package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;

import java.util.List;

@Dao
public interface LessonDao extends BaseDao<Lesson>{
    @Query("DELETE FROM lesson")
    void deleteAll();

    @Query("SELECT * FROM lesson WHERE level_id = :levelId ORDER BY id ASC")
    LiveData<List<Lesson>> loadLessonsByLevelId(String levelId);

    @Query("SELECT * FROM lesson WHERE lesson_id = :lessonId")
    LiveData<Lesson> loadLesson(String lessonId);

    //@Query("SELECT")
    //Lesson getLesson(String lessonId);

    @Query("UPDATE lesson SET progress = :progress WHERE lesson_id = :lessonId")
    int updateLessonProgress(String lessonId, int progress);
}

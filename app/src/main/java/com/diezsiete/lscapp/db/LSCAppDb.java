package com.diezsiete.lscapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.diezsiete.lscapp.db.dao.LessonDao;
import com.diezsiete.lscapp.db.dao.LevelDao;
import com.diezsiete.lscapp.db.dao.PracticeDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosDao;
import com.diezsiete.lscapp.db.dao.PracticeVideosWordDao;
import com.diezsiete.lscapp.db.dao.PracticeWordsDao;
import com.diezsiete.lscapp.db.dao.UserDao;
import com.diezsiete.lscapp.db.dao.WordDao;
import com.diezsiete.lscapp.db.entity.LessonEntity;
import com.diezsiete.lscapp.db.entity.UserEntity;
import com.diezsiete.lscapp.db.entity.LevelEntity;
import com.diezsiete.lscapp.db.entity.PracticeEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosWordEntity;
import com.diezsiete.lscapp.db.entity.PracticeWordsEntity;
import com.diezsiete.lscapp.db.entity.WordEntity;

@Database(
    entities = {
        LevelEntity.class,
        LessonEntity.class,
        PracticeEntity.class,
        PracticeWordsEntity.class,
        PracticeVideosEntity.class,
        PracticeVideosWordEntity.class,
        WordEntity.class,
        UserEntity.class
    },
    version = 43,
    exportSchema = false
)
public abstract class LSCAppDb extends RoomDatabase {
    public abstract LevelDao levelDao();

    public abstract LessonDao lessonDao();

    public abstract PracticeDao practiceDao();

    public abstract PracticeWordsDao practiceWordsDao();

    public abstract PracticeVideosDao practiceVideosDao();

    public abstract PracticeVideosWordDao practiceVideosWordDao();

    public abstract WordDao wordDao();

    public abstract UserDao userDao();
}

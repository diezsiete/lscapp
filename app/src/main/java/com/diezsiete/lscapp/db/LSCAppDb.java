package com.diezsiete.lscapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.diezsiete.lscapp.vo.Lesson;
import com.diezsiete.lscapp.vo.Level;
import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeVideosWord;
import com.diezsiete.lscapp.vo.PracticeWords;
import com.diezsiete.lscapp.vo.User;
import com.diezsiete.lscapp.vo.Word;

@Database(
    entities = {
        Level.class,
        Lesson.class,
        Practice.class,
        PracticeWords.class,
        PracticeVideos.class,
        PracticeVideosWord.class,
        Word.class,
        User.class
    },
    version = 36,
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

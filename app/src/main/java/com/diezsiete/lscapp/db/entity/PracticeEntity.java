package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.diezsiete.lscapp.db.LSCAppTypeConverters;

import java.util.List;

@Entity(tableName = "practice",
        foreignKeys = {
            @ForeignKey(
                    entity = LessonEntity.class,
                    parentColumns = "lesson_id",
                    childColumns = "lesson_id"
            )
        },
        indices = {
            @Index(value="lesson_id", name = "practice_lesson_id")
        }
)
@TypeConverters(LSCAppTypeConverters.class)
public class PracticeEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="lesson_id")
    public String lessonId;
    public String code;
    public List<String> pictures;
    public List<Integer> answer;
    @ColumnInfo(name="answer_user")
    public List<Integer> answerUser;
    @ColumnInfo(name="answer_correct")
    public boolean answerCorrect = false;
    public boolean completed = false;

    @Ignore
    public List<PracticeWordsEntity> words;
    @Ignore
    public List<PracticeVideosEntity> videos;

    public PracticeEntity(String code) {
        this.code = code;
    }
}

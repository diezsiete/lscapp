package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.diezsiete.lscapp.db.LSCAppTypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "practice",
        foreignKeys = {
            @ForeignKey(
                    entity = Lesson.class,
                    parentColumns = "lesson_id",
                    childColumns = "lesson_id"
            )
        },
        indices = {
            @Index(value= "practice_id", unique = true),
            @Index(value="lesson_id", name = "practice_lesson_id")
        }
)
@TypeConverters(LSCAppTypeConverters.class)
public class Practice {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="practice_id")
    public String practiceId;
    @ColumnInfo(name="lesson_id")
    public String lessonId;
    public String code;
    public List<String> images;
    public List<Integer> answer;
    @ColumnInfo(name="answer_user")
    public List<Integer> answerUser;
    @ColumnInfo(name="answer_correct")
    public boolean answerCorrect = false;
    public boolean completed = false;

    @Ignore
    public List<PracticeWords> words;
    @Ignore
    public List<PracticeVideos> videos;

    public Practice(@NonNull String practiceId, String code) {
        this.practiceId = practiceId;
        this.code = code;
    }

}

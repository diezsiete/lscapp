package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "lesson",
        foreignKeys = {
            @ForeignKey(
                    entity = LevelEntity.class,
                    parentColumns = "level_id",
                    childColumns = "level_id"
            )
        },
        indices = {
            @Index(value= "lesson_id", unique = true),
            @Index(value="level_id", name = "lesson_level_id")
        }
)
public class LessonEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="lesson_id")
    public String lessonId;
    @ColumnInfo(name="level_id")
    public String levelId;
    public String name;
    @ColumnInfo(name="image")
    public String image;
    public String color;
    public boolean available = true;
    public int progress = 0;

    public LessonEntity(@NonNull String lessonId, String name, String image, String color) {
        this.lessonId = lessonId;
        this.name = name;
        this.image = image;
        this.color = color;
    }
}

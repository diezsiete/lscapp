package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.diezsiete.lscapp.db.entity.PracticeEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "practice_videos",
        foreignKeys = {
                @ForeignKey(
                        entity = PracticeEntity.class,
                        parentColumns = "id",
                        childColumns = "practice_id",
                        onDelete = CASCADE
                )
        },
        indices = {
            @Index(value="practice_id", name = "practice_video_id")
        }
)
public class PracticeVideosEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="practice_id")
    public long practiceId;
}

package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "practice_videos",
        foreignKeys = {
                @ForeignKey(
                        entity = Practice.class,
                        parentColumns = "practice_id",
                        childColumns = "practice_id",
                        onDelete = CASCADE
                )
        },
        indices = {
            @Index(value="practice_id", name = "practice_video_id")
        }
)
public class PracticeVideos {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="practice_id")
    public String practiceId;
}

package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "practice_videos_word",
        foreignKeys = {
                @ForeignKey(
                        entity = PracticeVideos.class,
                        parentColumns = "id",
                        childColumns = "practice_videos_id",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = Word.class,
                        parentColumns = "word",
                        childColumns = "word_id"
                ),
        },
        indices = {
                @Index(value="practice_videos_id", name = "practice_videosword_video_id"),
                @Index(value="word_id", name = "practice_videosword_word_id")
        }
)
public class PracticeVideosWord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="practice_videos_id")
    public long practiceVideosId;
    @ColumnInfo(name="word_id")
    public String wordId;
}

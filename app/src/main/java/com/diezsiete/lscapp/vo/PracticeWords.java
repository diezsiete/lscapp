package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "practice_words",
        foreignKeys = {
                @ForeignKey(
                        entity = Practice.class,
                        parentColumns = "practice_id",
                        childColumns = "practice_id"
                )
        },
        indices = {
            @Index(value="practice_id", name = "practice_word_id")
        }
)
public class PracticeWords {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="practice_id")
    public String practiceId;
    public String words;

}

package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.diezsiete.lscapp.db.LSCAppTypeConverters;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "practice_words",
        foreignKeys = {
                @ForeignKey(
                        entity = Practice.class,
                        parentColumns = "id",
                        childColumns = "practice_id",
                        onDelete = CASCADE
                )
        },
        indices = {
            @Index(value="practice_id", name = "practice_word_id")
        }
)
@TypeConverters(LSCAppTypeConverters.class)
public class PracticeWords {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="practice_id")
    public long practiceId;

    public List<String> words;

}

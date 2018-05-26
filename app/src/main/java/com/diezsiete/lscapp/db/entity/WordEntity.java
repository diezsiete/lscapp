package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word")
public class WordEntity {
    @PrimaryKey
    @NonNull
    public String word;
    @NonNull
    public String video;

    public WordEntity(@NonNull String word, @NonNull String video) {
        this.word = word;
        this.video = video;
    }

}

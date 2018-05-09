package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word")
public class Word {
    @PrimaryKey
    @NonNull
    public String word;
    @NonNull
    public String video;

    public Word(@NonNull String word, @NonNull String video) {
        this.word = word;
        this.video = video;
    }

}

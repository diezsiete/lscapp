package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.List;

public class PracticeVideosWordData {
    @Embedded
    public PracticeVideosWord entity;

    @Relation(parentColumn = "word_id", entityColumn = "word")
    public List<Word> words;
}

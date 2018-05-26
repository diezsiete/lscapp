package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PracticeVideosWord {
    @Embedded
    public PracticeVideosWordEntity entity;

    @Relation(parentColumn = "word_id", entityColumn = "word")
    public List<WordEntity> wordEntities;
}

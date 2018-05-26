package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PracticeVideos {
    @Embedded
    public PracticeVideosEntity entity;

    @Relation(parentColumn = "id", entityColumn = "practice_videos_id", entity = PracticeVideosWordEntity.class)
    public List<PracticeVideosWord> videosWord;

    public PracticeVideos() {
    }
}

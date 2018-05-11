package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PracticeVideosData {
    @Embedded
    public PracticeVideos entity;

    @Relation(parentColumn = "id", entityColumn = "practice_videos_id", entity = PracticeVideosWord.class)
    public List<PracticeVideosWordData> videosWord;

    public PracticeVideosData() {
    }
}

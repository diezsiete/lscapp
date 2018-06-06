package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "achievement")
public class AchievementEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="achievement_id")
    public String achievementId;
    public String title;
    public String description;
    public String image;
}

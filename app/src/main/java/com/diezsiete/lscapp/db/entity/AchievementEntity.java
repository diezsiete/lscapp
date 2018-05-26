package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "achievement")
public class AchievementEntity {
    @PrimaryKey
    @NonNull
    public String title;
    public String description;
    @ColumnInfo(name="image_url")
    public String imageUrl;
}

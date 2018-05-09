package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "level", indices = {@Index(value = "level_id", unique = true)})
public class Level{
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="level_id")
    public String levelId;
    public String name;
    public String description;
    public String image;
    public String color;
    public boolean available = true;

    public Level(@NonNull String levelId, String name, String description, String image, String color) {
        this.levelId = levelId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.color = color;
    }

}

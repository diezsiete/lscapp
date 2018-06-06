package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.diezsiete.lscapp.db.LSCAppTypeConverters;

import java.util.List;

@Entity(tableName = "user")
@TypeConverters(LSCAppTypeConverters.class)
public class UserEntity {
    @PrimaryKey
    @ColumnInfo(name="profile_id")
    @NonNull
    public String profileId;
    public String name;
    public String email;
    @ColumnInfo(name="profil_image_url")
    public String profileImageUrl;
    @ColumnInfo(name="progress_name")
    public String progressName;
    @ColumnInfo(name="general_progress")
    public String generalProgress;
    public String token;
    @ColumnInfo(name="reached_achievements")
    public List<String> reachedAchievements;
    @ColumnInfo(name="completed_lessons")
    public List<String> completedLessons;
    public String password;
    @ColumnInfo(name="password_confirm")
    public String passwordConfirm;

    public UserEntity(@NonNull String profileId, String email) {
        this.profileId = profileId;
        this.email = email;
    }
}

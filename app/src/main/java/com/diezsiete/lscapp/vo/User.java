package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @ColumnInfo(name="profile_id")
    @NonNull
    public String profileId;
    public String email;
    @ColumnInfo(name="profil_image_url")
    public String profileImageUrl;
    @ColumnInfo(name="general_progress")
    public String generalProgress;
    public String token;

    public User(@NonNull String profileId, String email) {
        this.profileId = profileId;
        this.email = email;
    }
}

package com.diezsiete.lscapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Level implements Parcelable {

    public static final String TAG = "Level";
    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) { return new Level(in); }
        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };

    private final String mId;
    private final String mName;
    public Level(String id, String name) {
        mId = id;
        mName = name;
    }

    public Level(JSONObject json) throws JSONException {
        mId = json.getString("id");
        mName = json.getString("name");
    }

    protected Level(Parcel in) {
        mName = in.readString();
        mId = in.readString();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
    }
}

package com.diezsiete.lscapp.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Concept {

    /*private String mId;
    private String mMeaning;
    private String mVideo;
    private String mThematic;

    public Concept(String id, String meaning, String video, String thematic) {
        mId = id;
        mMeaning = meaning;
        mVideo = video;
        mThematic = thematic;
    }

    public Concept(JSONObject json) throws JSONException {
        mId = json.getString("id");
        mMeaning = json.getString("meaning");
        mVideo = json.getString("video");
        mThematic = json.getString("thematic");
    }

    public String getId() {
        return mId;
    }

    public String getMeaning() {
        return mMeaning;
    }

    public String getVideo() {
        return mVideo;
    }

    public String getThematic() {
        return mThematic;
    }*/

    private String id;
    private String meaning;
    private String video;
    private String thematic;

    public String getId() {
        return id;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getVideo() {
        return video;
    }

    public String getThematic() {
        return thematic;
    }
}

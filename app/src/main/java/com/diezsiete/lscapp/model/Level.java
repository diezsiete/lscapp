package com.diezsiete.lscapp.model;


public class Level {

    public static final String TAG = "Level";


    private String levelId;
    private String title;
    private String description;
    private String image;
    private String[] practices;


    public String getLevelId() {
        return levelId;
    }

    public String getTitle() {
        return title;
    }

    public String[] getPractices() {
        return practices;
    }

    public String getImage() {
        return image;
    }
}

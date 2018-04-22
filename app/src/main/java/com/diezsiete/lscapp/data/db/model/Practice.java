package com.diezsiete.lscapp.data.db.model;



public class Practice {

    private String practiceId;
    private String[] videos;
    private String[] words;
    private String[] pictures;


    public String getPracticeId() {
        return practiceId;
    }
    public String[] getVideos() {
        return videos;
    }
    public String[] getWords() {
        return words;
    }
    public String[] getPictures() {
        return pictures;
    }

    public int getId() {
        return practiceId.hashCode();
    }

    public String getType() {
        String[] explodeId = practiceId.split("-");
        return explodeId[0] + "-" + explodeId[1] + (explodeId.length > 3  ? "-" + explodeId[2] : "");
    }
}
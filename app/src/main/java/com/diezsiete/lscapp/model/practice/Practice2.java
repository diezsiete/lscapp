package com.diezsiete.lscapp.model.practice;



public class Practice2 {
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
        return "show-sign";
    }
}

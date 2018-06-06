package com.diezsiete.lscapp.vo;

public class ToolbarData {
    public String title = "";
    public String color = "";
    public String image = "";

    public boolean show = true;

    public boolean actionBack = false;
    public boolean actionEdit = false;
    public boolean actionSearch = false;

    public ToolbarData(){

    }
    public ToolbarData(boolean show){
        this.show = show;
    }

    public ToolbarData(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public ToolbarData(String title, boolean actionBack){
        this.title = title;
        this.actionBack = actionBack;
    }

    public ToolbarData(String title, String color, boolean actionBack) {
        this.title = title;
        this.color = color;
        this.actionBack = actionBack;
    }

    public ToolbarData(String title, String color, String image) {
        this.title = title;
        this.color = color;
        this.image = image;
    }

    public ToolbarData(String title, String color, String image, boolean actionBack) {
        this.title = title;
        this.color = color;
        this.image = image;
        this.actionBack = actionBack;
    }

    public ToolbarData(String title, boolean actionEdit, boolean actionSearch){
        this.title = title;
        this.actionEdit = actionEdit;
        this.actionSearch = actionSearch;
    }


}

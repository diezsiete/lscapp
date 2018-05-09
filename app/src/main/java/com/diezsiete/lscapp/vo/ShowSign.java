package com.diezsiete.lscapp.vo;

import android.support.annotation.NonNull;

public class ShowSign extends PracticeWithData {

    public ShowSign(PracticeWithData p) {
        super(p);
    }

    public String getQuestion() {
        return "";
    }

    public String getWord() {
        return words.get(0).words;
    }
}

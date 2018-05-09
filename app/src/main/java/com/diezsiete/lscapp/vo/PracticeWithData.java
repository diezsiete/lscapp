package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Transaction;


import java.util.List;

public class PracticeWithData  {
    @Embedded
    public Practice practice;


    @Relation(parentColumn = "practice_id", entityColumn = "practice_id")
    public List<PracticeWords> words;

    @Relation(parentColumn = "practice_id", entityColumn = "practice_id")
    public List<PracticeVideos> videos;

    /**
     * Obligatorio para Room
     */
    public PracticeWithData() {
    }

    @Ignore
    PracticeWithData(PracticeWithData practiceWithData){
        //TODO este copy no creo que este bien
        this.practice = practiceWithData.practice;
        this.words = practiceWithData.words;
    }

    public String getQuestion() {
        return "PracticeWithData Question";
    }

    /*public List<PracticeWords> getWords() {
        return null;
    }*/

    public List<String> getWords() {
        return null;
    }

    public String getWord() {
        return words.get(0).words;
    }

    public List<String> getVideos() {
        return null;
    }

    public boolean inAnswerCorrect(List<String> answer) {
        return false;
    }
}

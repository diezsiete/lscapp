package com.diezsiete.lscapp.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Transaction;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

public class PracticeWithData  {
    @Embedded
    public Practice entity;


    @Relation(parentColumn = "practice_id", entityColumn = "practice_id")
    public List<PracticeWords> words;

    @Relation(parentColumn = "practice_id", entityColumn = "practice_id", entity = PracticeVideos.class)
    public List<PracticeVideosData> videos;

    /**
     * Obligatorio para Room
     */
    public PracticeWithData() {
    }

    @Ignore
    PracticeWithData(PracticeWithData practiceWithData){
        //TODO este copy no creo que este bien
        this.entity = practiceWithData.entity;
        this.words = practiceWithData.words;
    }

    public String getQuestion() {
        String question = "";
        switch (entity.code){
            case "which-one-video" :
            case "translate-video" :
                question = "¿Qué describe la seña?";
                break;
            case "which-one-videos" :
                question = "\""+ TextUtils.join(" ", words.get(0).words) +"\"";
                break;
        }
        return question;
    }

    /*public List<PracticeWords> getWords() {
        return null;
    }*/

    public String getWord() {
        return words.get(0).words.get(0);
    }

    public List<List<String>> getWords() {
        List<List<String>> listWords = new ArrayList<>();
        for(PracticeWords word : words){
            listWords.add(word.words);
        }
        return listWords;
    }

    public List<String> getVideo() {
        List<String> urls = new ArrayList<>();
        PracticeVideosData wordList = this.videos.get(0);
        for(PracticeVideosWordData videosWord : wordList.videosWord)
            for(Word word : videosWord.words)
                urls.add(word.video);

        return urls;
    }

    public List<List<String>> getVideos() {
        List<List<String>> listVideos = new ArrayList<>();
        for(PracticeVideosData practiceVideo : videos){
            List<String> listUrls = new ArrayList<>();
            for(PracticeVideosWordData wordData : practiceVideo.videosWord)
                for(Word word : wordData.words)
                    listUrls.add(word.video);
            listVideos.add(listUrls);
        }
        return listVideos;
    }

    public boolean validateAnswer() {
        boolean ok = true;
        for(int i = 0; i < entity.answer.size(); i++){
            if(entity.answerUser.size() < i || !entity.answerUser.get(i).equals(entity.answer.get(i)))
                ok = false;
        }
        entity.answerCorrect = ok;
        entity.completed = true;
        return ok;
    }

    public String getStringAnswer() {
        String answer = "";
        if(!entity.code.equals("show-sign")){
            answer = "answer";
        }
        return answer;
    }

    public boolean getEnableSave() {
        boolean enable = true;
        if(!entity.code.equals("show-sign"))
            enable = entity.answerUser.size() > 0;
        return enable;
    }

    public List<String> getImages() {
        return entity.images;
    }
}

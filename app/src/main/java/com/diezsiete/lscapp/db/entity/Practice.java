package com.diezsiete.lscapp.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.text.TextUtils;


import com.diezsiete.lscapp.vo.Picture;

import java.util.ArrayList;
import java.util.List;

public class Practice {
    @Embedded
    public PracticeEntity entity;


    @Relation(parentColumn = "id", entityColumn = "practice_id")
    public List<PracticeWordsEntity> words;

    @Relation(parentColumn = "id", entityColumn = "practice_id", entity = PracticeVideosEntity.class)
    public List<PracticeVideos> videos;

    @Relation(parentColumn = "lesson_id", entityColumn = "lesson_id")
    public List<LessonEntity> lessonEntity;

    /**
     * Obligatorio para Room
     */
    public Practice() {
    }

    public String getQuestion() {
        String question = null;
        switch (entity.code){
            case "which-one-video" :
            case "translate-video" :
                question = "¿Qué describe la seña?";
                break;
            case "which-one-videos" :
                question = "\""+ TextUtils.join(" ", words.get(0).words) +"\"";
                break;
            case "take-sign" :
                question = "Realiza la seña \"" + getWord() + "\" con la mano y tomale una foto";
                break;
        }
        return question;
    }

    /*public List<PracticeWordsEntity> getWords() {
        return null;
    }*/

    public String getWord() {
        return words.get(0).words.get(0);
    }

    public List<List<String>> getWords() {
        List<List<String>> listWords = new ArrayList<>();
        for(PracticeWordsEntity word : words){
            listWords.add(word.words);
        }
        return listWords;
    }

    public List<String> getVideo() {
        List<String> urls = new ArrayList<>();
        PracticeVideos wordList = this.videos.get(0);
        for(PracticeVideosWord videosWord : wordList.videosWord)
            for(WordEntity wordEntity : videosWord.wordEntities)
                urls.add(wordEntity.video);

        return urls;
    }

    public List<List<String>> getVideos() {
        List<List<String>> listVideos = new ArrayList<>();
        for(PracticeVideos practiceVideo : videos){
            List<String> listUrls = new ArrayList<>();
            for(PracticeVideosWord wordData : practiceVideo.videosWord)
                for(WordEntity wordEntity : wordData.wordEntities)
                    listUrls.add(wordEntity.video);
            listVideos.add(listUrls);
        }
        return listVideos;
    }

    public boolean validateAnswer() {
        boolean ok = true;
        if(entity.code.equals("take-sign")){
            ok = !(entity.answerUser.size() == 0 || entity.answerUser.get(0) == 0);
        }else
            for(int i = 0; i < entity.answer.size(); i++){
                if(entity.answerUser.size() <= i || !entity.answerUser.get(i).equals(entity.answer.get(i)))
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

        if(entity.code.equals("take-sign") && enable)
            enable = entity.answerUser.get(0) == 0 || entity.answerUser.get(0) == 1;

        return enable;
    }

    public List<Picture> getPictures() {
        List<Picture> pictures = new ArrayList<>();
        for(String url : entity.pictures)
            pictures.add(new Picture(url));
        return pictures;
    }

    public void setUserAnswer(int answer) {
        List<Integer> answerUser = new ArrayList<>();
        answerUser.add(answer);
        entity.answerUser = answerUser;
    }

    public Integer getAnswerUser(){
        if(entity.answerUser.size() > 0)
            return entity.answerUser.get(0);
        return null;
    }

    public Integer getAnswer(){
        return entity.answer.size() > 0 ? entity.answer.get(0) : null;
    }

    public LessonEntity getLessonEntity(){
        return lessonEntity.get(0);
    }

    public int getId(){
        return entity.id;
    }

    public boolean getCompleted(){
        return entity.completed;
    }

    public boolean getAnswerCorrect(){
        return entity.answerCorrect;
    }
}

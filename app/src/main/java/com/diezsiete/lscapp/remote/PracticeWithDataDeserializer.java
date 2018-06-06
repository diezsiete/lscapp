package com.diezsiete.lscapp.remote;

import android.util.Log;

import com.diezsiete.lscapp.db.entity.PracticeEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideos;
import com.diezsiete.lscapp.db.entity.PracticeVideosWordEntity;
import com.diezsiete.lscapp.db.entity.PracticeVideosWord;
import com.diezsiete.lscapp.db.entity.Practice;
import com.diezsiete.lscapp.db.entity.PracticeWordsEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class
PracticeWithDataDeserializer implements JsonDeserializer {

    private List<List<String>> deserializeArrayOfArrays(JsonObject jsonObject, String memberName) {
        JsonArray array = jsonObject.getAsJsonArray(memberName);

        List<List<String>> deserialized = new ArrayList<>();

        for(int i = 0; i < array.size(); i++){
            JsonArray wordsArray = array.get(i).getAsJsonArray();
            List<String> words = new ArrayList<>();
            if(wordsArray.size() > 0) {
                for (int j = 0; j < wordsArray.size(); j++) {
                    words.add(wordsArray.get(j).getAsString());
                }
                deserialized.add(words);
            }
        }

        return deserialized;
    }
    private List<Integer> jsonElementToIntList(JsonElement jsonElement) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(jsonElement, listType);
    }
    private List<String> jsonElementToStringList(JsonElement jsonElement) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(jsonElement, listType);
    }


    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        PracticeEntity practiceEntity = new PracticeEntity(code);
        practiceEntity.answer = jsonElementToIntList(jsonObject.get("answer"));
        practiceEntity.pictures = jsonElementToStringList(jsonObject.get("pictures"));

        List<PracticeWordsEntity> practiceWordEntities = new ArrayList<>();

        List<List<String>> wordsList = deserializeArrayOfArrays(jsonObject, "words");
        for(List<String> words : wordsList) {
            PracticeWordsEntity pw = new PracticeWordsEntity();
            pw.words = words;
            practiceWordEntities.add(pw);
        }

        List<PracticeVideos> practiceVideosList = new ArrayList<>();
        for(List<String> words : deserializeArrayOfArrays(jsonObject, "videos")){
            PracticeVideos practiceVideos = new PracticeVideos();
            practiceVideos.entity = new PracticeVideosEntity();

            practiceVideos.videosWord = new ArrayList<>();
            for(String word : words){
                PracticeVideosWord videoWord = new PracticeVideosWord();
                videoWord.entity = new PracticeVideosWordEntity();
                videoWord.entity.wordId = word;
                practiceVideos.videosWord.add(videoWord);
            }
            practiceVideosList.add(practiceVideos);
        }

        Practice practice = new Practice();
        practice.entity = practiceEntity;
        practice.words = practiceWordEntities;
        practice.videos = practiceVideosList;

        return practice;
    }
}

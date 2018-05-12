package com.diezsiete.lscapp.api;

import android.text.TextUtils;

import com.diezsiete.lscapp.vo.Practice;
import com.diezsiete.lscapp.vo.PracticeVideos;
import com.diezsiete.lscapp.vo.PracticeVideosData;
import com.diezsiete.lscapp.vo.PracticeVideosWord;
import com.diezsiete.lscapp.vo.PracticeVideosWordData;
import com.diezsiete.lscapp.vo.PracticeWithData;
import com.diezsiete.lscapp.vo.PracticeWords;
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
        String practiceId = jsonObject.get("practiceId").getAsString();
        String code = jsonObject.get("code").getAsString();
        Practice practice = new Practice(practiceId, code);
        practice.answer = jsonElementToIntList(jsonObject.get("answer"));
        practice.images = jsonElementToStringList(jsonObject.get("images"));

        List<PracticeWords> practiceWords = new ArrayList<>();

        List<List<String>> wordsList = deserializeArrayOfArrays(jsonObject, "words");
        for(List<String> words : wordsList) {
            PracticeWords pw = new PracticeWords();
            pw.practiceId = practiceId;
            pw.words = words;
            practiceWords.add(pw);
        }

        List<PracticeVideosData> practiceVideosList = new ArrayList<>();
        for(List<String> words : deserializeArrayOfArrays(jsonObject, "videos")){
            PracticeVideosData practiceVideos = new PracticeVideosData();
            practiceVideos.entity = new PracticeVideos();
            practiceVideos.entity.practiceId = practiceId;

            practiceVideos.videosWord = new ArrayList<>();
            for(String word : words){
                PracticeVideosWordData videoWord = new PracticeVideosWordData();
                videoWord.entity = new PracticeVideosWord();
                videoWord.entity.wordId = word;
                practiceVideos.videosWord.add(videoWord);
            }
            practiceVideosList.add(practiceVideos);
        }

        PracticeWithData practiceWithData = new PracticeWithData();
        practiceWithData.entity = practice;
        practiceWithData.words = practiceWords;
        practiceWithData.videos = practiceVideosList;

        return practiceWithData;
    }
}

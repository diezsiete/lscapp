package com.diezsiete.lscapp.utils;

import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.JsonAttributes;
import com.diezsiete.lscapp.model.Level;
import com.diezsiete.lscapp.model.practice.Practice;
import com.diezsiete.lscapp.model.practice.ShowSignPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideoPractice;
import com.diezsiete.lscapp.model.practice.WhichOneVideosPractice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Encargado de retornar la información ya sea del servidor o localmente
 */

public class ProxyApp {

    private static final String URL_DICTIONARY = "/dictionary";
    private static final String URL_LEVELS = "/levels";
    private static final String URL_PRACTICES = "/practices";

    private static String getRestJson(String url) throws IOException, JSONException {
        String jsonString = NetworkUtils.get(url);
        final String MESSAGE_CODE = "code";
        final String CONTENT = "content";

        JSONObject json = new JSONObject(jsonString);

        /* Is there an error? */
        if (json.has(MESSAGE_CODE)) {
            int errorCode = json.getInt(MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                default:
                    /* Server probably down */
                    throw new IOException("Conexión retorna error " + String.valueOf(errorCode));
            }
        }

        return json.getString(CONTENT);
    }


    private static Concept[] getConceptsFromDictionaryJson(String dictionaryJsonString) throws JSONException {
        JSONArray dictionaryArray = new JSONArray(dictionaryJsonString);

        /* String array que tiene las palabras del diccionario */
        Concept[] parsedDictionaryData = new Concept[dictionaryArray.length()];

        for (int i = 0; i < dictionaryArray.length(); i++) {
            JSONObject conceptJson = dictionaryArray.getJSONObject(i);
            parsedDictionaryData[i] = new Concept(conceptJson);
        }

        return parsedDictionaryData;
    }

    private static Level[] getLevelsFromJson(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        Level[] parsedData = new Level[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++)
            parsedData[i] = new Level(jsonArray.getJSONObject(i));

        return parsedData;
    }

    private static List<Practice> getPracticesFromJson(String jsonString) throws JSONException {
        List<Practice> practices = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPractice = jsonArray.getJSONObject(i);
            practices.add(createPracticeDueToType(jsonPractice));
        }
        return practices;
    }

    private static Practice createPracticeDueToType(JSONObject json) throws JSONException {
        String type = json.getString(JsonAttributes.TYPE);
        switch (type){
            case JsonAttributes.PracticeType.SHOW_SIGN:
                return new ShowSignPractice(
                    json.getString(JsonAttributes.MEANING),
                    JsonHelper.jsonArrayToStringArray(json.getString(JsonAttributes.VIDEO))
                );
            case JsonAttributes.PracticeType.WHICH_ONE_VIDEOS:
                return new WhichOneVideosPractice(
                    json.getString(JsonAttributes.QUESTION),
                    json.getInt(JsonAttributes.ANSWER),
                    JsonHelper.jsonMatrixToStringMatrix(json.getString(JsonAttributes.OPTIONS))
                );
            case JsonAttributes.PracticeType.WHICH_ONE_VIDEO:
                return new WhichOneVideoPractice(
                    json.getString(JsonAttributes.QUESTION),
                    JsonHelper.jsonArrayToStringArray(json.getString(JsonAttributes.VIDEO)),
                    JsonHelper.jsonArrayToStringArray(json.getString(JsonAttributes.OPTIONS)),
                    json.getInt(JsonAttributes.ANSWER)
                );
            default: {
                throw new IllegalArgumentException("Practice type " + type + " is not supported");
            }
        }
    }


    public static Concept[] getDictionary() throws IOException, JSONException {
        return getConceptsFromDictionaryJson(getRestJson(URL_DICTIONARY));
    }

    public static Level[] getLevels() throws IOException, JSONException {
        return getLevelsFromJson(getRestJson(URL_LEVELS));
    }

    public static List<Practice> getPractices(String levelId) throws IOException, JSONException {
        return getPracticesFromJson(getRestJson(URL_PRACTICES + "/" + levelId));
    }
}
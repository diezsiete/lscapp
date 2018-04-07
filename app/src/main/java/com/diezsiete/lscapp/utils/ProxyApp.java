package com.diezsiete.lscapp.utils;

import com.diezsiete.lscapp.model.Concept;
import com.diezsiete.lscapp.model.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Encargado de retornar la información ya sea del servidor o localmente
 */

public class ProxyApp {

    private static final String URL_DICTIONARY = "/dictionary";
    private static final String URL_LEVELS = "/levels";

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

    public static Concept[] getDictionary() throws IOException, JSONException {
        return getConceptsFromDictionaryJson(getRestJson(URL_DICTIONARY));
    }

    public static Level[] getLevels() throws IOException, JSONException {
        return getLevelsFromJson(getRestJson(URL_LEVELS));
    }


}

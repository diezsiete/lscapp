package com.diezsiete.lscapp.utils;

import com.diezsiete.lscapp.model.Concept;

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

    public static Concept[] getDictionary() throws IOException, JSONException {
        String dictionaryJsonString = getRestJson(URL_DICTIONARY);
        return getConceptsFromDictionaryJson(dictionaryJsonString);
    }

    public static Concept[] getConceptsFromDictionaryJson(String dictionaryJsonString) throws JSONException {
        JSONArray dictionaryArray = new JSONArray(dictionaryJsonString);

        /* String array que tiene las palabras del diccionario */
        Concept[] parsedDictionaryData = null;

        parsedDictionaryData = new Concept[dictionaryArray.length()];

        for (int i = 0; i < dictionaryArray.length(); i++) {
            JSONObject conceptJson = dictionaryArray.getJSONObject(i);
            parsedDictionaryData[i] = new Concept(conceptJson);
        }

        return parsedDictionaryData;
    }
}

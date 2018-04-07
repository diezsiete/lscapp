package com.diezsiete.lscapp.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class DictioinaryJsonUtils {

    public static String[] getWordsFromDictionaryJson(String dictionaryJsonString) throws JSONException {
        JSONObject dictionaryJson = new JSONObject(dictionaryJsonString);

        final String MESSAGE_CODE = "code";
        final String LIST = "dictionary";
        final String TEXT = "text";
        final String VIDEO = "video";

        /* String array que tiene las palabras del diccionario */
        String[] parsedDictionaryData = null;

        /* Is there an error? */
        if (dictionaryJson.has(MESSAGE_CODE)) {
            int errorCode = dictionaryJson.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        JSONArray dictionaryArray = dictionaryJson.getJSONArray(LIST);

        parsedDictionaryData = new String[dictionaryArray.length()];

        for (int i = 0; i < dictionaryArray.length(); i++) {
            JSONObject wordJSON = dictionaryArray.getJSONObject(i);
            parsedDictionaryData[i] = wordJSON.getString(TEXT);
        }

        return parsedDictionaryData;
    }

}

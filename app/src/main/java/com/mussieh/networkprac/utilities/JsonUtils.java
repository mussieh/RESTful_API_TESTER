package com.mussieh.networkprac.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing JSON data
 * Based on the Android Developer Fundamentals Course
 * https://www.gitbook.com/@google-developer-training
 * author: Mussie Habtemichael
 * Date: 03/04/2018
 */

public class JsonUtils {

    private static final String BOOK_TITLE_KEY = "title";
    private static final String BOOK_AUTHORS_KEY = "authors";
    private static final String BOOK_VOLUME_KEY = "volumeInfo";
    private static final String BOOK_ITEMS_KEY = "items";
    private String jsonData;

    public static List<String> getParsedData(String returnedJsonString) {
        JSONObject jsonObject = null;
        JSONArray itemsArray = null;
        List<String> parsedData = new ArrayList<>();

        try {
            jsonObject = new JSONObject(returnedJsonString);
            itemsArray = jsonObject.getJSONArray(BOOK_ITEMS_KEY);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book =  itemsArray.getJSONObject(i);
                String title = null;
                String authors = null;
                JSONObject volumeInfo = book.getJSONObject(BOOK_VOLUME_KEY);

                try {
                    title = volumeInfo.getString(BOOK_TITLE_KEY);
                    authors = volumeInfo.getString(BOOK_AUTHORS_KEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (title != null && authors != null) {
                    parsedData.add(title);
                    parsedData.add(authors);
                    return parsedData;
                }

                return null;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

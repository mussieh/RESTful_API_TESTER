package com.mussieh.networkprac.utilities;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Helper class to connect to a RESTful API service
 * Based on the Android Developer Fundamentals Course
 * https://www.gitbook.com/@google-developer-training
 * author: Mussie Habtemichael
 * Date: 03/04/2018
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";

    /**
     * Method that returns JSON resources from a RESTful web service (Google Books API in this case)
     * @param queryString the string to search for
     * @param resourceType the resource type to look for (Default: Books)
     * @return the JSON data as a string
     */
    public static String getResourceData(String queryString, String resourceType) {
        URL url = getURL(queryString, resourceType);
        String jsonString = getResponseFromHttpsUrl(url);;
        Log.d(TAG, jsonString);
        return jsonString;
    }

    /**
     * Method to build a URL from a query string
     * @param queryString the string to search for
     * @param resourceType the resource type to look for (Default: Books)
     * @return the URL for the RESTful API
     */
    private static URL getURL(String queryString, String resourceType) {
        Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString)
                .appendQueryParameter(MAX_RESULTS, "10")
                .appendQueryParameter(PRINT_TYPE, "books").build();

        try {
            URL requestURL = new URL(builtURI.toString());
            Log.v(TAG, "URL: " + requestURL);
            return requestURL;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that returns the JSON String from a RESTful web service
     * @param givenUrl the RESTful web service URL
     * @return the JSON data as a String
     */
    private static String getResponseFromHttpsUrl(URL givenUrl) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            urlConnection = (HttpsURLConnection) givenUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n"); // new line added for easier debugging
            }

            if (buffer.length() == 0) {
                return null; // stream was empty
            }
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

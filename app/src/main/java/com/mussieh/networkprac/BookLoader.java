package com.mussieh.networkprac;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.mussieh.networkprac.utilities.NetworkUtils;

/**
 * Helper class that loads resources in a worker thread
 * Based on the Android Developer Fundamentals Course
 * https://www.gitbook.com/@google-developer-training
 * author: Mussie Habtemichael
 * Date: 03/04/2018
 */
public class BookLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    /**
     * Construct the BookLoader
     * @param context the activity context
     * @param argString the query string from a Bundle
     */
    public BookLoader(Context context, String argString) {
        super(context);
        mQueryString = argString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getResourceData(mQueryString, "Book");
    }
}

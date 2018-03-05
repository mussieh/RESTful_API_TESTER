package com.mussieh.networkprac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mussieh.networkprac.utilities.JsonUtils;

import java.util.List;

/**
 * Main activity class for testing a RESTful API service
 * Based on the Android Developer Fundamentals Course
 * https://www.gitbook.com/@google-developer-training
 * author: Mussie Habtemichael
 * Date: 03/04/2018
 */
public class NetworkTestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = NetworkTestActivity.class.getSimpleName();
    private static final String EMPTY_INPUT_ERROR_MESSAGE = "Please enter a search term";
    private static final String NO_INTERNET_ERROR_MESSAGE = "Please check your network connection " +
            "and try again.";
    private static final String NO_RESULTS_MESSAGE = "No Results Found";
    private static final String KEY_QUERY_STRING = "queryString";
    private EditText mBookInput;
    private TextView mAuthorText;
    private TextView mTitleText;
    private ProgressBar loaderProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        mBookInput = (EditText) findViewById(R.id.bookInput);
        mAuthorText = (TextView) findViewById(R.id.authorText);
        mTitleText = (TextView) findViewById(R.id.titleText);
        loaderProgressBar = (ProgressBar) findViewById(R.id.pb_loader);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    /**
     * Method to check if an internet connection is available
     * @return the status of the internet connection
     */
    private boolean isConnectionAvailable() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Method that searches a RESTful API service through a loader
     * doing work in a background thread
     * It is activated by the 'onClick' event of the search button
     * @param view the view this method is activated from
     */
    public void searchBooks(View view) {
        String queryString = mBookInput.getText().toString();
        Log.d(TAG, "searching");

        if (isConnectionAvailable() && queryString.length() != 0) {
            loaderProgressBar.setVisibility(View.VISIBLE);
            mTitleText.setVisibility(View.INVISIBLE);
            mAuthorText.setVisibility(View.INVISIBLE);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(KEY_QUERY_STRING, queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
        else {
            loaderProgressBar.setVisibility(View.INVISIBLE);
            mTitleText.setVisibility(View.VISIBLE);
            mAuthorText.setVisibility(View.VISIBLE);
            if (queryString.length() == 0) {
                mAuthorText.setText("");
                mTitleText.setText(EMPTY_INPUT_ERROR_MESSAGE);
            } else {
                mAuthorText.setText("");
                mTitleText.setText(NO_INTERNET_ERROR_MESSAGE);
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, args.getString(KEY_QUERY_STRING));
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        List<String> parsedData = JsonUtils.getParsedData(data.toString());
        loaderProgressBar.setVisibility(View.INVISIBLE);
        mTitleText.setVisibility(View.VISIBLE);
        mAuthorText.setVisibility(View.VISIBLE);
        if (parsedData != null) {
            mTitleText.setText(parsedData.get(0)); // title text
            mAuthorText.setText(parsedData.get(1)); // author's name text
        } else {
            mTitleText.setText(NO_RESULTS_MESSAGE);
            mAuthorText.setText("");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {} // not needed for now
}

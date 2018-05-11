package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by giorgosnty on 30/4/2018.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private static final String LOG_TAG = ArticleLoader.class.getName();
    private String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i(LOG_TAG, "Calling loadInBackground()");

        if (url == null) {
            return null;
        }

        List<Article> earthquakes = QueryUtils.fetchArticlesData(url);
        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

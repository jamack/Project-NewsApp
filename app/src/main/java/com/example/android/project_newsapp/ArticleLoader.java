package com.example.android.project_newsapp;

import android.content.Context;

import java.util.List;

/**
 * Created by James on 1/10/2018.
 */

public class ArticleLoader extends android.support.v4.content.AsyncTaskLoader<List<Article>> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Article.class.getSimpleName();

    /**
     * Query string for desired articles
     */
    private String mQueryUrl;

    public ArticleLoader(Context context, String url) {
        super(context);

        this.mQueryUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        return QueryUtils.extractArticles(mQueryUrl);
    }
}

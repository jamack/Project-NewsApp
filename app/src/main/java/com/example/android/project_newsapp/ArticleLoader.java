package com.example.android.project_newsapp;

import android.content.Context;

import java.util.List;

/**
 * Loader class - handles creating a list - on a background thread - of {@link Article} objects
 * that are fetched from a server, parsed, and added to a list.
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
        return QueryUtils.extractArticles(getContext(), mQueryUrl);
    }
}

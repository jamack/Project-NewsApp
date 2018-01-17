package com.example.android.project_newsapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Article>>{

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsFeedActivity.class.getSimpleName();

    // CONSTANT queury test string for The Guardian API
    private static final String TEST_QUERY = "https://content.guardianapis.com/us/technology?q=android&order-by=newest&api-key=a512d6e2-1af6-4390-8168-b682383ef0fd";

    /**
     * Reference to the {@link android.widget.ListView}
     */
    private ListView mListView;

    /**
     * Reference to {@link ArticleAdapter}
     */
    private ArrayAdapter<Article> mAdapter;

    /**
     * Reference to LoaderManager
     */
    private LoaderManager mLoaderManager;

    /**
     * Reference to a list of articles
     */
    private List<Article> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        // Store reference to the ListView
        mListView = findViewById(R.id.list_view);

        // Store reference to an instance of the ArticleAdapter class
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set adapter on the list view
        mListView.setAdapter(mAdapter);

        mLoaderManager = getSupportLoaderManager();

        fetchArticles();

    }

    private void fetchArticles() {
        Log.v(LOG_TAG,"In fetchArticles method.");
        mLoaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"In onCreateLoader method.");
        return new ArticleLoader(this, TEST_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        Log.v(LOG_TAG,"In onLoadFinished method; title of first article is: " + articles.get(0).getTitle());

        mAdapter.addAll(articles);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

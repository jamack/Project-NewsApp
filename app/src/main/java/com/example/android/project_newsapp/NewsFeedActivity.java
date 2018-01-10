package com.example.android.project_newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsFeedActivity.class.getSimpleName();

    // CONSTANT queury test string for The Guardian API
    private static final String TEST_QUERY = "https://content.guardianapis.com/us/technology?q=android&order-by=newest&api-key=a512d6e2-1af6-4390-8168-b682383ef0fd";

    private List<Article> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mArticles = QueryUtils.extractArticles(TEST_QUERY);

        // TODO: NEED TO MOVE THE QUERYUTILS CALLS TO A BACKGROUND THREAD VIA A LOADER...

    }
}

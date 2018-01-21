package com.example.android.project_newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
     * Reference to the {@link android.widget.AdapterView.OnItemClickListener}
     */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * Reference to the {@link android.widget.ProgressBar}
     */
    private ProgressBar mProgressBar;

    /**
     * Reference to the empty view
     */
    private TextView mEmptyView;

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

        // Store reference to the empty view
        mEmptyView = findViewById(R.id.empty_view);

        // Assign the ListView's empty view
        mListView.setEmptyView(mEmptyView);

        // Store reference to the ProgressBar
        mProgressBar = findViewById(R.id.progress_bar);

        // Store reference to an instance of the ArticleAdapter class
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set adapter on the list view
        mListView.setAdapter(mAdapter);

        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v(LOG_TAG,"In the onItemClick method.");
                // Call method to display full article for list item in user's web brower
                openWebView(position);
            }
        };

        // Store reference to a LoaderManager instance
        mLoaderManager = getSupportLoaderManager();

        // Call method to handle server query on background thread
        fetchArticles();
    }

    // TODO: ADD/OVERRIDE ONRESUME() METHOD TO HANDLE CONFIGURATION CHANGES & RESTORE STATE

    private void fetchArticles() {
        Log.v(LOG_TAG,"In fetchArticles method.");
        // Display the ProgressBar
        mProgressBar.setVisibility(View.VISIBLE);

        // Call for a Loader to get articles from a server.
        // Check whether instance already exists. If so, use that. If not, initialize an instance.
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

        // Turn off the progress bar
        mProgressBar.setVisibility(View.GONE);

        // Check that returned list of articles is not null or empty
        if (articles != null || articles.size() > 0) {
            // Store list of articles in class field
            mArticles = articles;

            // Add list of articles to the adapter to display
            mAdapter.addAll(mArticles);

            // Set the OnItemClickListener on the ListView. (Now that it is filled with servery query results).
            mListView.setOnItemClickListener(mOnItemClickListener);
        } else {
            // Set the empty view's text
            mEmptyView.setText("No articles to display for current search.");
            // Make the empty view visible
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void openWebView(int position) {
        Log.v(LOG_TAG,"In the openWebView method.");
        // Retrieve the item's web url
        String itemWebUrl = mArticles.get(position).getWebUrl();

        // Declare and initialize a new intent, using the web url
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemWebUrl));

        // Verify that user has an installed app to handle the intent.
        // If so, start the web browser intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // TODO: ADD/OVERRIDE ONSTOP() METHOD AND RELEASE RESOURCES,
    // TODO: INCLUDING SAVING STATE
}

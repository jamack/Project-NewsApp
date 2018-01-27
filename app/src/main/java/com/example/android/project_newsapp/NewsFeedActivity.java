package com.example.android.project_newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
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

    // CONSTANT query test string for The Guardian API
    private static final String INITIAL_QUERY = "https://content.guardianapis.com/us/technology?q=android&order-by=newest&api-key=a512d6e2-1af6-4390-8168-b682383ef0fd";

    /**
     * Key for saving list view state
     */
    private static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";

    /**
     * Key for saving {@link List<Article}
     */
    private static final String ARTICLES_LIST_STATE = "ARTICLES_LIST_STATE";

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
     * Reference to {@link android.net.ConnectivityManager}
     */
    private ConnectivityManager mConnectivityManager;

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

        // Check for prior state
        if(savedInstanceState != null) {
            // Restore prior state of list view
            mListView.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_VIEW_STATE));
        }

        // Store reference to the empty view
        mEmptyView = findViewById(R.id.empty_view);

        // Assign the ListView's empty view
        mListView.setEmptyView(mEmptyView);

        // Store reference to the ProgressBar
        mProgressBar = findViewById(R.id.progress_bar);

        // Check for prior state and whether it contains key for list of articles
        if(savedInstanceState != null && savedInstanceState.containsKey(ARTICLES_LIST_STATE)) {
            // Restore the saved list of articles
            mArticles = savedInstanceState.getParcelableArrayList(ARTICLES_LIST_STATE);

            // Store reference to a new instance of the ArticleAdapter class,
            // using the restored list of articles
            mAdapter = new ArticleAdapter(this, mArticles);
        } else {
            // Store reference to a new instance of the ArticleAdapter class,
            // using a new empty ArrayList
            mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        }

        // Set adapter on the list view
        mListView.setAdapter(mAdapter);

        // Store reference to a LoaderManager instance
        mLoaderManager = getSupportLoaderManager();

        // Check for prior state
        if(savedInstanceState == null) {
            // Call method to handle server query on background thread
            fetchArticles();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check whether onclick listener for list view already exists.
        // If not, create one. (It will be attached in onLoadFinished method, when data for list view is loaded).
        if (mOnItemClickListener == null) {
            mOnItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Call method to display full article for list item in user's web brower
                    openWebView(position);
                }
            };

            // Set the OnItemClickListener on the ListView. (Now that it is filled with servery query results).
            mListView.setOnItemClickListener(mOnItemClickListener);
        }
    }

    /**
     * Perform a server query to return a list of articles.
     * Check for Connectivity; handle error states and progress bar.
     * Create a new {@link ArticleLoader} to perform server operations on a background thread.
     */
    private void fetchArticles() {
        // Get reference to a ConnectivityManager instance
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        // Check whether there is network connectivity
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // If no network connection
        if (!isConnected) {
            // Clear out previous data
            mAdapter.clear();

            // Turn on empty view text
            mEmptyView.setVisibility(View.VISIBLE);

            // Set empty view text to show error message regarding network connectivity
            mEmptyView.setText(R.string.error_message_no_connectivity);
            return;
        } else {
            mEmptyView.setVisibility(View.GONE);
        }

        // Display the ProgressBar
        mProgressBar.setVisibility(View.VISIBLE);

        // Call for a Loader to get articles from a server.
        // Check whether instance already exists. If so, use that. If not, initialize an instance.
        mLoaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, INITIAL_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Turn off the progress bar
        mProgressBar.setVisibility(View.GONE);

        // Check that returned list of articles is not null or empty
        if (articles != null || articles.size() > 0) {
            // Store list of articles in class field
            mArticles = articles;

            // Add list of articles to the adapter to display
            mAdapter.addAll(mArticles);

        } else {
            // Set the empty view's text
            mEmptyView.setText(R.string.error_message_no_articles_found);
            // Make the empty view visible
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // Clear the data
        mAdapter.clear();
    }

    /**
     * Opens user's web browser app (if present) to display a full article
     *
     * @param position in the list view, determining which article to display
     */
    private void openWebView(int position) {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Check whether there is any data (i.e. list of articles, which would also mean list view state is present).
        // (If list of articles is null, then we don't need to worry about saving it or list view state).
        if (mArticles != null) {
            // Save the list of articles
            outState.putParcelableArrayList(ARTICLES_LIST_STATE, (ArrayList<Article>) mArticles);

            // Save state for the list view
            outState.putParcelable(LIST_VIEW_STATE, mListView.onSaveInstanceState());
        }
    }

    /**
     * Called when activity is completely hidden from the user.
     * Release resources that might cause memory leaks.
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Release the list view's onclick listener
        mOnItemClickListener = null;

        // Release the ConnectivityManager
        mConnectivityManager = null;
    }
}

package com.example.android.project_newsapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by James on 1/9/2018.
 */

public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Article> extractArticles(String url) {
        Log.v(LOG_TAG,"Entering extractArticles method.");
        // Declare URL object; initially null;
        URL queryUrl = null;
        // Check that passed String is not null or empty
        if (url != null && !url.isEmpty()) {
            // Call helper method to format string to URL object
            queryUrl = formatURL(url);
        } else {
            Log.e(LOG_TAG, "In extractArticles method; the passed url string is null or empty.");
        }

        // Declare string to hold server's response
        String responseString = null;
        // Check that the URL object is not null
        if (queryUrl != null) {
            // Call helper method to open an HTTP connection to the server
            responseString = makeHttpRequest(queryUrl);
        }

        Log.v(LOG_TAG,"At end of extractArticles method; value of JSON string (to be parsed) is: " + responseString);

        return null;
    }

    private static URL formatURL(String url) {
        // Declare URL object; initially null
        URL formattedUrl = null;
        // Initialize a new URL from the url string
        try {
            formattedUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot create URL object.");
        }

        return formattedUrl;
    }

    private static String makeHttpRequest(URL queryUrl) {
        Log.v(LOG_TAG,"Entering makeHttpRequest method.");
        // Ensure passed URL is valid.
        if (queryUrl == null) {
            return null;
        }

        // String to hold response; initially null.
        String jsonResponse = null;

        // Declare HttpURLConnection object; initially null;
        HttpURLConnection httpConnection;
        // Obtain a new HttpURLConnection by calling URL.openConnection() and casting the result to HttpURLConnection
        try {
            httpConnection = (HttpURLConnection) queryUrl.openConnection();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating HttpURLConnection object.");
            return null;
        }

        // Prepare the request / Set the parameters
        httpConnection.setConnectTimeout(1000 /* milliseconds */);
        httpConnection.setReadTimeout(1500 /* milliseconds */);
        try {
            httpConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "Problem setting up HTTP connection.");
        }

        // Make HTTP connection
        try {
            httpConnection.connect();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem opening HTTP connection.");
            return null;
        }

        // Declare InputStream object
        InputStream inputStream;
        // Check whether the HTTP connection was successful
        try {
            if (httpConnection.getResponseCode() == 200) {
                inputStream = httpConnection.getInputStream();

                // Read data from input stream and save as string
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unsuccessful HTTP connection.");
            return null;
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.v(LOG_TAG,"Entering readFromStream method.");
        // Check that InputStream is not null
        if (inputStream == null) {
            return null;
        }

        // Declare and initialize anInputStreamReader and wrap it in a BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

        // Create a new StringBuilder object
        StringBuilder output = new StringBuilder();

        // Read first line from the buffered reader
        String line = reader.readLine();
        // If initial line is not null, add it to the StringBuilder.
        // Repeat for as many non-null lines as exist
        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }

        // Convert StringBuilder's content and return a final String
        return output.toString();
    }
}


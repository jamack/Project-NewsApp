package com.example.android.project_newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by James on 1/14/2018.
 */

public class ArticleAdapter extends ArrayAdapter{

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();

    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.v(LOG_TAG,"Entering getView method.");
        // Get the Article from the index matching the position in the ListView
        Article currentArticle = (Article) getItem(position);

        // Reference to a ViewHolder object
        ViewHolder viewHolder;

        // Check whether View is being reused or needs to be inflated for the first time
        if (convertView == null) {
            Log.v(LOG_TAG,"In the getView method; inflating a new View.");
            // Inflate a new list_item View
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            Log.v(LOG_TAG,"In the getView method; creating a new ViewHolder object.");
            // Create new ViewHolder object
            viewHolder = new ViewHolder();

            Log.v(LOG_TAG,"In the getView method; fetching TextViews and caching in ViewHolder.");
            // Store references to view TextViews
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.list_item_title);
            viewHolder.pubDateTextView = (TextView) convertView.findViewById(R.id.list_item_date_published);
            viewHolder.sectionTextView = (TextView) convertView.findViewById(R.id.list_item_section);
            viewHolder.authorsTextView = (TextView) convertView.findViewById(R.id.list_item_author);

            // Cache TextView references with View via the ViewHolder
            convertView.setTag(viewHolder);
        } else {
            Log.v(LOG_TAG,"In the getView method; retrieving ViewHolder object from existing View.");
            viewHolder  = (ViewHolder) convertView.getTag();
        }

        // Set text for the title
        viewHolder.titleTextView.setText(currentArticle.getTitle());
        // Set text for the date of publication
        viewHolder.pubDateTextView.setText(currentArticle.getPubDate());
        // Set the text for the section
        viewHolder.sectionTextView.setText(currentArticle.getSection());
        // Set the text for the author(s)
        viewHolder.authorsTextView.setText(currentArticle.getAuthor());

        return convertView;
    }

    private static class ViewHolder {
        // Cache reference to article's title TextView
        private TextView titleTextView;

        // Cache reference to article's publication date TextView
        private TextView pubDateTextView;

        // Cache reference to article's section TextView
        private TextView sectionTextView;

        // Cache reference to article's author(s) TextView
        private TextView authorsTextView;
    }
}

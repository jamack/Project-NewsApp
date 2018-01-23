package com.example.android.project_newsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains data for a news article
 */

// TODO: MAKE CLASS PARCELABLE SO A LIST OF ARTICLES CAN BE SAVED AND RESTORED
public class Article implements Parcelable{

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Article.class.getSimpleName();

    /**
     *  Title of the article
     */
    private String mTitle;

    /**
     * Section that the article is grouped under
     */
    private String mSection;

    /**
     * Author(s) of the article
     */
    private String mAuthor;

    /**
     * Date the article was published
     */
    private String mPubDate;

    /**
     * URL of article on the web
     */
    private String mWebUrl;

    /**
     * Construct a new {@link Article}
     *
     * @param title of the article
     * @param section that the article is grouped under
     * @param author of the article, as many as are known
     * @param pubDate of the article, if known
     * @param webUrl of the article on the web
     */
    public Article(String title, String section, String author, String pubDate, String webUrl) {
        this.mTitle = title;
        this.mSection = section;
        this.mAuthor = author;
        this.mPubDate = pubDate;
        this.mWebUrl = webUrl;
    }

    protected Article(Parcel in) {
        mTitle = in.readString();
        mSection = in.readString();
        mAuthor = in.readString();
        mPubDate = in.readString();
        mWebUrl = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write {@link Article}' data to a Parcelable that can be saved as part of an Activity's state
     *
     * @param parcel to be saved into
     * @param flags with any additional info about how object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mSection);
        parcel.writeString(mAuthor);
        parcel.writeString(mPubDate);
        parcel.writeString(mWebUrl);
    }


}

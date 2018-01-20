package com.example.android.project_newsapp;

/**
 * Contains data for a news article
 */

public class Article {

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
}

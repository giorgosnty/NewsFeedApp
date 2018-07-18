package com.example.android.newsfeedapp;

/**
 * Created by giorgosnty on 26/4/2018.
 */

public class Article {

    private String title;
    private String author;
    private String date;
    private String section;
    private String url;
    private String thumbnail;

    public Article(String title, String author, String date, String section, String url,String thumbnail) {
        this.author = author;
        this.title = title;
        this.date = date;
        this.section = section;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    //getters for retrieving information about an article when we need
    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

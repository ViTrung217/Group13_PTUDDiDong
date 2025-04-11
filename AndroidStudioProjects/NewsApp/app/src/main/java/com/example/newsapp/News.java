package com.example.newsapp;

public class News {
    private String title,imageUrl,url;
    private long timestamp;
    public News(){}

    public News(String title, String imageUrl, String url, long timestamp) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }


    public long getTimestamp() {
        return timestamp;
    }


    public String getUrl() {
        return url;
    }



    public String getImageUrl() {
        return imageUrl;
    }


}

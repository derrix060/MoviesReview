package com.example.mario.moviesreview;

/**
 * Created by mario on 15/05/17.
 */

public class Movie {

    public String imagePath;
    public String title;
    public String publish_date;
    public String review;
    public String link;
    public String headline;
    // rating?

    public Movie(String imagePath,
                 String title,
                 String publish_date,
                 String review,
                 String link,
                 String headline) {
        this.imagePath = imagePath;
        this.title = title;
        this.publish_date = publish_date;
        this.review = review;
        this.link = link;
        this.headline = headline;
    }

    @Override
    public String toString(){
        String rtn = "";

        rtn += "Title: " + this.title;

        return rtn;
    }
}

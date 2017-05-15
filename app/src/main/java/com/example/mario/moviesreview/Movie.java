package com.example.mario.moviesreview;

/**
 * Created by mario on 15/05/17.
 */

public class Movie {

    public String imagePath;
    public String title;
    public String publish_date;
    public String review;

    public Movie(String imagePath, String title, String publish_date, String review) {
        this.imagePath = imagePath;
        this.title = title;
        this.publish_date = publish_date;
        this.review = review;
    }
}

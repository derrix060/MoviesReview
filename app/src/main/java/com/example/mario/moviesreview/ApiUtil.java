package com.example.mario.moviesreview;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by mario on 19/05/17.
 */

public class ApiUtil {

    Activity activity;

    public ApiUtil(Activity activity){
        this.activity = activity;
    }

    public URL createURL(String movie) {

        String apiKey = "a42f7467f8d140f4bc85850b29a5d8c6";
        String baseUrl = "https://api.nytimes.com/svc/movies/v2/reviews/search.json";
        // Example: https://api.nytimes.com/svc/movies/v2/reviews/search.json?api_key=a42f7467f8d140f4bc85850b29a5d8c6&query=terminator+genisys

        /*
            {"status":"OK","copyright":"Copyright (c) 2017 The New York Times Company. All Rights Reserved.","has_more":false,"num_results":1,"results":[{"display_title":"Terminator: Genisys","mpaa_rating":"PG-13","critics_pick":0,"byline":"MANOHLA DARGIS","headline":"Review: In \u2018Terminator Genisys,\u2019 Ageless Cyborgs and a Deathless Franchise","summary_short":"Arnold Schwarzenegger returns, it\u2019s 1984 again, and Sarah Connor is at risk once more.","publication_date":"2015-06-30","opening_date":"2015-07-01","date_updated":"2016-03-30 07:03:53","link":{"type":"article","url":"http:\/\/www.nytimes.com\/2015\/07\/01\/movies\/review-terminator-genisys-shows-that-arnold-schwarzenegger-is-most-assuredly-back.html","suggested_link_text":"Read the New York Times Review of Terminator: Genisys"},"multimedia":{"type":"mediumThreeByTwo210","src":"https:\/\/static01.nyt.com\/images\/2015\/07\/01\/arts\/01TERMINATESUB\/01TERMINATESUB-mediumThreeByTwo210.jpg","width":210,"height":140}}]}

         */

        try {
            String urlString = baseUrl + "?api_key=" + apiKey +
                    "&query=" + URLEncoder.encode(movie, "UTF-8");

            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Movie> convertJSONToArrayList (JSONObject moviesJSON){

        ArrayList<Movie> movieList = new ArrayList<>();

        try{
            JSONArray movies = moviesJSON.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++){
                JSONObject movie = movies.getJSONObject(i);
                String title = movie.getString("display_title");
                String publish_date = movie.getString("publication_date");
                String summary = movie.getString("summary_short");
                String link = movie.getJSONObject("link").getString("url");
                String image = "";
                if (movie.getJSONObject("multimedia") != JSONObject.NULL)
                    image = movie.getJSONObject("multimedia").getString("src");

                movieList.add(new Movie(image, title, publish_date, summary, link));

            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return movieList;
    }

}

package com.example.mario.moviesreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.id.list;
import static com.example.mario.moviesreview.R.id.txtSearch;

/**
 * Created by mario on 19/05/17.
 */

public class ApiUtil {

    ArrayList<Movie> movieList;

    public ApiUtil(){
        movieList = new ArrayList<>();
    }

    public URL createURL(String movie) {

        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.api_endpoint);
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

    private void convertJSONToArrayList (JSONObject forecast){
        movieList.clear();
        try{
            JSONArray movies = forecast.getJSONArray("results");
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
    }

    private class GetMoviesTask extends AsyncTask<URL, Void, JSONObject> {
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(JSONObject weather) {
            convertJSONToArrayList(weather);
            weatherArrayAdapter.notifyDataSetChanged();
            weatherListView.smoothScrollToPosition(0);
        }
    }

    private class LoadImageTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}

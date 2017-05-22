package com.example.mario.moviesreview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mario on 15/05/17.
 */

public class MovieListActivity extends AppCompatActivity {
    private Movie[] moviesList;
    private ApiUtil apiUtil;
    private RecyclerView mRecyclerView;
    private MovieItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateMovies();

        // Inflate view
        setContentView(R.layout.movies_view);


        mRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        // improve performance when you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Custom adapter
        Snackbar.make(findViewById(R.id.moviesLayout), moviesList[0].toString(), Snackbar.LENGTH_SHORT).show();


        adapter = new MovieItemAdapter(moviesList);
        mRecyclerView.setAdapter(adapter);
        /*
        // Get api Util to use NYTimes API
        //apiUtil = new ApiUtil(this);

        // Get list of movies
        //String movieToSeach = getIntent().getStringExtra("movieToSearch");

        //new GetMoviesTask().execute(movieToSeach);
        */

    }


    private class GetMoviesTask extends AsyncTask<String, Void, JSONObject> {

        protected JSONObject doInBackground(String... movies) {

            URL url = apiUtil.createURL(movies[0]);

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.moviesLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.moviesLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(JSONObject moviesJSON) {
            super.onPostExecute(moviesJSON);
            moviesList = apiUtil.convertJSONToArrayList(moviesJSON);
            adapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    private void populateMovies(){
        moviesList = new Movie[1];
        Movie m = new Movie("https:\\/\\/static01.nyt.com\\/images\\/2015\\/07\\/01\\/arts\\/01TERMINATESUB\\/01TERMINATESUB-mediumThreeByTwo210.jpg",
               "Terminator: Genisys",
                "2015-06-30",
                "Arnold Schwarzenegger returns, it\\u2019s 1984 again, and Sarah Connor is at risk once more.",
                "http:\\/\\/www.nytimes.com\\/2015\\/07\\/01\\/movies\\/review-terminator-genisys-shows-that-arnold-schwarzenegger-is-most-assuredly-back.html" );

        moviesList[0] = m;

        // Example: https://api.nytimes.com/svc/movies/v2/reviews/search.json?api_key=a42f7467f8d140f4bc85850b29a5d8c6&query=terminator+genisys


    }
}

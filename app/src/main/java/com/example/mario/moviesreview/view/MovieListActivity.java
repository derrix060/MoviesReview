package com.example.mario.moviesreview.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mario.moviesreview.R;
import com.example.mario.moviesreview.controller.ApiUtil;
import com.example.mario.moviesreview.controller.MovieItemAdapter;
import com.example.mario.moviesreview.model.Movie;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mario on 15/05/17.
 */

public class MovieListActivity extends AppCompatActivity {
    private ArrayList<Movie> moviesList = new ArrayList<>();
    private ApiUtil apiUtil;
    private RecyclerView mRecyclerView;
    private MovieItemAdapter adapter;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate view
        setContentView(R.layout.movies_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = (ProgressBar) findViewById(R.id.progressBar);

        mRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        // improve performance when you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Custom adapter
        adapter = new MovieItemAdapter(moviesList);
        mRecyclerView.setAdapter(adapter);

        // Get api Util to use NYTimes API
        apiUtil = new ApiUtil(this);

        // Get list of movies
        String movieToSearch = getIntent().getStringExtra("movieToSearch");

        new GetMoviesTask(findViewById(android.R.id.content)).execute(movieToSearch);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        View rootView;
        GetMoviesTask(View view){
            this.rootView = view;
        }
        protected ArrayList<Movie> doInBackground(String... movies) {
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
                        Snackbar.make(findViewById(R.id.moviesLayout), getString(R.string.read_error), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return apiUtil.convertJSONToArrayList(new JSONObject(builder.toString()));
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.moviesLayout), getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return new ArrayList<>();
        }

        protected void onPostExecute(ArrayList<Movie> movies) {
            progress.setVisibility(View.INVISIBLE);
            if(movies.size() == 0){
                Snackbar.make(rootView, getString(R.string.dont_find_item), Snackbar.LENGTH_LONG).show();
            }
            else{
                moviesList = movies;
                adapter = new MovieItemAdapter(moviesList);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
    }
}

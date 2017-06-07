package com.example.mario.moviesreview.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mario.moviesreview.R;
import com.example.mario.moviesreview.model.Movie;
import com.example.mario.moviesreview.view.MovieDetailActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mario on 15/05/17.
 */

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ItemViewHolder> {

    private ArrayList<Movie> myItens;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        //CardView cv;
        TextView title;
        TextView publish_date;
        TextView movie_review;
        ImageView img;
        Button btn;
        private final Context context;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.filmImage);
            title = (TextView)itemView.findViewById(R.id.movie_title);
            publish_date = (TextView)itemView.findViewById(R.id.publish_date);
            movie_review = (TextView)itemView.findViewById(R.id.movie_description);
            btn = (Button) itemView.findViewById(R.id.btnSeeMore);

            // Set description when talkback is activated
            img.setContentDescription(title.getText().toString() + " " + R.string.talkback_movie_image);
            context = itemView.getContext();
        }

    }

    public void openMovieDetails(Context context, Movie movie){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("MOVIE_TITLE", movie.title);
        intent.putExtra("MOVIE_SUMMARY", movie.review);
        intent.putExtra("MOVIE_IMAGE", movie.imagePath);
        intent.putExtra("MOVIE_URL", movie.link);
        context.startActivity(intent);
    }

    // Constructor
    public MovieItemAdapter(ArrayList<Movie> myItens){
        this.myItens = myItens;
    }

    @Override
    public MovieItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int position) {
        final Movie movie = myItens.get(position);

        itemViewHolder.title.setText(movie.title);
        itemViewHolder.movie_review.setText(movie.headline);
        itemViewHolder.publish_date.setText(movie.publish_date);

        if (movie.imagePath == ""){
            itemViewHolder.img.setImageResource(R.drawable.ic_do_not_disturb_alt_black_24dp);
        }
        else{
            new LoadImageTask(itemViewHolder.img).execute(movie.imagePath);
        }

        //TODO: Create intent to open MovieDetailActivity
        itemViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMovieDetails(itemViewHolder.context, movie);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myItens.size();
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


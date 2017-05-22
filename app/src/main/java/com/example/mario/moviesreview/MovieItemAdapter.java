package com.example.mario.moviesreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mario on 15/05/17.
 */

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ItemViewHolder> {

    private Movie[] myItens;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        //CardView cv;
        TextView title;
        TextView publish_date;
        TextView movie_review;
        ImageView img;

        public ItemViewHolder(final View itemView) {
            super(itemView);

            //cv = (CardView)itemView.findViewById(R.id.detach_card);
            img = (ImageView)itemView.findViewById(R.id.filmImage);
            title = (TextView)itemView.findViewById(R.id.title_movies);
            publish_date = (TextView)itemView.findViewById(R.id.publish_date);
            movie_review = (TextView)itemView.findViewById(R.id.description);

            // Set description when talkback is activated
            img.setContentDescription(title.getText().toString() + " " + R.string.talkback_movie_image);

        }
    }

    // Constructor
    public MovieItemAdapter(Movie[] myItens){
        this.myItens = myItens;
    }

    @Override
    public MovieItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        Movie movie = myItens[position];

        itemViewHolder.title.setText(movie.title);
        itemViewHolder.movie_review.setText(movie.review);
        itemViewHolder.publish_date.setText(movie.publish_date);

        // Get image from NY Times
        //new LoadImageTask(itemViewHolder.img).execute(movie.imagePath);

    }

    @Override
    public int getItemCount() {
        return myItens.length;
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


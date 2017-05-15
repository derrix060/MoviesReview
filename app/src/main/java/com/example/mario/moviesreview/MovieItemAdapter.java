package com.example.mario.moviesreview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            img = (ImageView)itemView.findViewById(R.id.movie_image);
            title = (TextView)itemView.findViewById(R.id.title_movies);
            publish_date = (TextView)itemView.findViewById(R.id.publish_date);
            movie_review = (TextView)itemView.findViewById(R.id.movie_review);

            // Set description when talkback is activated
            img.setContentDescription(title.getText().toString() + R.string.talkback_movie_image);

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

        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.title.setText(myItens[position].title);
        itemViewHolder.movie_review.setText(myItens[position].review);
        itemViewHolder.publish_date.setText(myItens[position].publish_date);

        // Get image from NY Times
        //itemViewHolder.img.setImageResource(myItens[position].imagePath);
        //itemViewHolder.img.setImageURI(URI.create(myItens[position].imagePath));
    }

    @Override
    public int getItemCount() {
        return myItens.length;
    }
}
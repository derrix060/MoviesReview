package com.example.mario.moviesreview.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mario.moviesreview.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mario on 31/05/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {

    TextView title;
    TextView summary;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(myToolbar);

        title = (TextView) findViewById(R.id.movie_detail_title);
        summary = (TextView) findViewById(R.id.movie_detail_summary);
        image = (ImageView) findViewById(R.id.movie_detail_image);
        Button btnSeeMore = (Button) findViewById(R.id.see_more_button);


        title.setText(getIntent().getStringExtra("MOVIE_TITLE"));
        summary.setText(getIntent().getStringExtra("MOVIE_SUMMARY"));
        String imageURL = getIntent().getStringExtra("MOVIE_IMAGE");

        if (imageURL == ""){
            image.setMaxHeight(0);
            image.setMaxWidth(0);
        }
        else {
            new LoadImageTask(image).execute(imageURL);
        }

        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getIntent().getStringExtra("MOVIE_URL")));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_back) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

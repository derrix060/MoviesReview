package com.example.mario.moviesreview.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.mario.moviesreview.R;

public class MainActivity extends AppCompatActivity {
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }

    public void fabAction(View view){
        Intent myIntent = new Intent(this, MovieListActivity.class);
        myIntent.putExtra("movieToSearch", txtSearch.getText().toString()); //Optional parameters
        startActivity(myIntent);
    }

}


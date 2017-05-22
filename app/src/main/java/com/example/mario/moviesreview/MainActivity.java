package com.example.mario.moviesreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }

    public void fabAction(View view){
        Intent myIntent = new Intent(this, MovieListActivity.class);
        myIntent.putExtra("movieToSearch", txtSearch.getText().toString()); //Optional parameters
        startActivity(myIntent);
    }

}


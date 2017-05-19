package com.example.mario.moviesreview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView hint;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction(view);
            }
        });

        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }

    private void fabAction(View view) {

        result(view, getText(R.string.waiting).toString());

        URL requestUrl = createURL();


        result(view, requestUrl.toString());
    }




    private void result(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }



}


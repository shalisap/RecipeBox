package com.example.shalisa.recipebox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // When clicking browse button
        View browseButton = findViewById(R.id.browseBtn);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
                startActivity(intent);
            }
        });

        // When clicking favorites button
        View favButton = findViewById(R.id.favoritesBtn);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView app_text = (TextView) findViewById(R.id.appNameMenuText);
                app_text.setText("HELLO");
            }
        });
    }
}

package com.example.shalisa.recipebox;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session manager
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        final String username = user.get(SessionManager.KEY_USERNAME);

        // Welcome message
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + username);

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
                TextView app_text = (TextView) findViewById(R.id.welcomeText);
                app_text.setText("HELLO");
            }
        });

        // Add new recipe
        View addButton = findViewById(R.id.addRecipeBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
            }
        });

        // Logout button
        View logoutButton = findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Toast.makeText(getApplicationContext(),
                        username + " logged out", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}

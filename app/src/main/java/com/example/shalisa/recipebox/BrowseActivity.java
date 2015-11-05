package com.example.shalisa.recipebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends Activity {

//    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        RecipeDatabaseHelper databaseHelper = RecipeDatabaseHelper.getInstance(this);
        List<Recipe> recipes = databaseHelper.getAllRecipes();

        // display
        ListAdapter adapter = new ArrayAdapter<Recipe>(BrowseActivity.this,
                R.layout.browse_item, R.id.recipeName, recipes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                final Recipe recipe = getItem(position);

                TextView recipe_name = (TextView) view.findViewById(R.id.recipeName);
                recipe_name.setText(recipe.getName());

                ImageButton imgBtn = (ImageButton) view.findViewById(R.id.recipeBtn);
                Picasso.with(BrowseActivity.this).load(recipe.getImgURL()).into(imgBtn);
                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BrowseActivity.this, RecipeActivity.class);
                        intent.putExtra("recipe_key", recipe);
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        ListView lView = (ListView) findViewById(R.id.browseRecipes);
        lView.setAdapter(adapter);

    }

}

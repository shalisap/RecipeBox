package com.example.shalisa.recipebox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends Activity {

    ArrayAdapter<String> adapter;
    List<String> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addRecipes();
        setContentView(R.layout.activity_browse);

        // display
        adapter = new ArrayAdapter<String>(BrowseActivity.this,
                R.layout.browse_item, R.id.textView, recipes);
        ListView lView = (ListView) findViewById(R.id.browseRecipes);
        lView.setAdapter(adapter);

    }

    public void addRecipes() {
        // TODO: TEMP RECIPES - POSSIBLY GRAB DATA FROM API; Array or list?
        ArrayList<Ingredient> pancakes_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1/8, Ingredient.Unit.TSP, "Baking Powder"));
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.PINCH, "Ground Cinnamon"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> pancakes_dir = new ArrayList<>();
        pancakes_dir.add("Mash banana in a bowl using a fork; add eggs, baking powder " +
                "and cinnamon and mix batter well");
        pancakes_dir.add("Heat butter in a skillet over medium heat. " +
                "Spoon batter into skillet and cook until bubbles form " +
                "and the edges are dry, 2 to 3 minutes. Flip and cook " +
                "until browned on the other side, 2 to 3 minutes. " +
                "Repeat with remaining batter.");
        Recipe pancakes = new Recipe("Pancakes", pancakes_ing, pancakes_dir);
        String name = pancakes.getName();
        recipes = new ArrayList<>();
        recipes.add(name);


        ArrayList<Ingredient> eggs_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> eggs_dir = new ArrayList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        Recipe eggs = new Recipe("Eggs", eggs_ing, eggs_dir);
        String eggs_name = eggs.getName();
        recipes.add(eggs_name);
    }

    // load images from a url?
    public static Drawable loadImage(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable img = Drawable.createFromStream(inputStream, "src name");
            return img;
        } catch (Exception e) {
            return null;
        }
    }
}

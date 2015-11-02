package com.example.shalisa.recipebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends Activity {

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addRecipes();
        setContentView(R.layout.activity_browse);

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


    public void addRecipes() {
        // TODO: TEMP RECIPES - POSSIBLY GRAB DATA FROM API; Array or list?
        ArrayList<Ingredient> pancakes_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1.0/8.0, Ingredient.Unit.TSP, "Baking Powder"));
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
        String pancakes_img = "http://www.onceuponachef.com/images/2013/01/banana-pancakes3.jpg";
        Recipe pancakes = new Recipe("Pancakes", pancakes_ing, pancakes_dir, pancakes_img);
        recipes = new ArrayList<>();
        recipes.add(pancakes);


        ArrayList<Ingredient> eggs_ing = new ArrayList<>();
        eggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        eggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> eggs_dir = new ArrayList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        String eggs_img = "http://toriavey.com/images/2014/06/How-to-Scramble-Eggs.jpg";
        Recipe eggs = new Recipe("Eggs", eggs_ing, eggs_dir, eggs_img);
        recipes.add(eggs);

        ArrayList<Ingredient> ceggs_ing = new ArrayList<>();
        ceggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        ceggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));
        ceggs_ing.add(new Ingredient(1.5, Ingredient.Unit.CUP, "Cheese"));

        ArrayList<String> ceggs_dir = new ArrayList<>();
        ceggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs. Add Cheese");
        String ceggs_img = "http://foodnetwork.sndimg.com/content/dam/images/food/fullset/2011/3/31/0/RE0902H_sunnys-perfect-scrambled-cheesy-eggs_s4x3.jpg";
        Recipe ceggs = new Recipe("Cheesy Eggs", ceggs_ing, ceggs_dir, ceggs_img);
        recipes.add(ceggs);
    }

}

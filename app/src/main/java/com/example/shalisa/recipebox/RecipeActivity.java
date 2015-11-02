package com.example.shalisa.recipebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // grab recipe from intent
        Recipe recipe = getIntent().getExtras().getParcelable("recipe_key");

        // set recipe header?
        TextView recipe_name = (TextView) findViewById(R.id.recipeTitle);
        recipe_name.setText(recipe.getName());

        // set recipe image
        ImageView recipe_img = (ImageView) findViewById(R.id.recipeImg);
        Picasso.with(this).load(recipe.getImgURL()).into(recipe_img);

        // set recipe ingredients
        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            LinearLayout item = (LinearLayout) findViewById(R.id.recipeIngredients);
            View ingView = getLayoutInflater().inflate(R.layout.ingredient_item, null);


            // display the quantity
                TextView quantView = (TextView) ingView.findViewById(R.id.quantityItem);
                String fractionQuantity = ingredient.getQuantityFractionString();
                quantView.setText(Html.fromHtml(fractionQuantity));

                // display the unit ingredient
                TextView ing_View = (TextView) ingView.findViewById(R.id.ingredientItem);
                ing_View.setText(ingredient.unitToString() + " " + ingredient.getIngredient());

            item.addView(ingView);
        }

        // set recipe instructions
        List<String> instructions = recipe.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            LinearLayout item = (LinearLayout) findViewById(R.id.recipeInstructions);
            View instView = getLayoutInflater().inflate(R.layout.instruction_item, null);

            // number the instruction
            TextView num_View = (TextView) instView.findViewById(R.id.recipeNum);
            num_View.setText(Integer.toString(i+1) + ".");

            // display the instruction
            TextView inst_View = (TextView) instView.findViewById(R.id.instructionItem);
            inst_View.setText(instructions.get(i));

            item.addView(instView);

        }

    }
}

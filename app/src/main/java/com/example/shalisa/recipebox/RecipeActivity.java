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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        ListAdapter ing_adapter = new ArrayAdapter<Ingredient>(this,
                R.layout.ingredient_item, R.id.ingredientItem,
                recipe.getIngredients()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Ingredient ingredient = getItem(position);

                // display the quantity
                TextView quantView = (TextView) view.findViewById(R.id.quantityItem);
                String fractionQuantity = ingredient.getQuantityFractionString();
                quantView.setText(Html.fromHtml(fractionQuantity));

                // display the unit ingredient
                TextView ing_View = (TextView) view.findViewById(R.id.ingredientItem);
                ing_View.setText(ingredient.unitToString() + " " + ingredient.getIngredient());
                return view;
            }
        };
        ListView ing_lView = (ListView) findViewById(R.id.recipeIngredients);
        ing_lView.setAdapter(ing_adapter);

        // set recipe instructions
        ListAdapter inst_adapter = new ArrayAdapter<String>(this,
                R.layout.instruction_item, R.id.instructionItem,
                recipe.getInstructions()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String instruction = getItem(position);

                // number the instruction
                TextView num_View = (TextView) view.findViewById(R.id.recipeNum);
                num_View.setText(Integer.toString(position+1));

                // display the instruction
                TextView inst_View = (TextView) view.findViewById(R.id.instructionItem);
                inst_View.setText(instruction);
                return view;
            }
        };
        ListView inst_lView = (ListView) findViewById(R.id.recipeInstructions);
        inst_lView.setAdapter(inst_adapter);

    }
}

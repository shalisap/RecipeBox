package com.example.shalisa.recipebox;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import static com.example.shalisa.recipebox.Ingredient.stringToUnit;

public class AddRecipeActivity extends Activity {

    private int ingredientCount = 0;
    private int instructionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

       // first initial ingredient
        LinearLayout allIngView = (LinearLayout) findViewById(R.id.addIngredient);
        LinearLayout newIngView = (LinearLayout) createAddIngredientView();
        allIngView.addView(newIngView);
        ingredientCount++;

        final Button delIngredientBtn = (Button) findViewById(R.id.deleteAnotherIngredient);
        delIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout allIngView = (LinearLayout) findViewById(R.id.addIngredient);
                allIngView.removeViewAt(allIngView.getChildCount() - 1); // remove last child
                ingredientCount--;
                if (ingredientCount < 2) {
                    delIngredientBtn.setVisibility(View.GONE);
                    delIngredientBtn.setClickable(false);
                    delIngredientBtn.setFocusable(false);
                }
            }
        });

        Button addIngredientBtn = (Button) findViewById(R.id.addAnotherIngredient);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout allIngView = (LinearLayout) findViewById(R.id.addIngredient);
                LinearLayout newIngView = (LinearLayout) createAddIngredientView();
                allIngView.addView(newIngView);
                ingredientCount++;
                if (ingredientCount > 1) {
                    delIngredientBtn.setVisibility(View.VISIBLE);
                    delIngredientBtn.setClickable(true);
                    delIngredientBtn.setFocusable(true);
                }
            }
        });

        final Button delInstructionBtn = (Button) findViewById(R.id.deleteAnotherInstruction);
        delInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout allInstView = (LinearLayout) findViewById(R.id.addInstruction);
                allInstView.removeViewAt(allInstView.getChildCount() - 1); // remove last child
                instructionCount--;
                if (instructionCount < 2) {
                    delInstructionBtn.setVisibility(View.GONE);
                    delInstructionBtn.setClickable(false);
                    delInstructionBtn.setFocusable(false);
                }
            }
        });

        LinearLayout allInstView = (LinearLayout) findViewById(R.id.addInstruction);
        LinearLayout newInstView = (LinearLayout) createAddInstructionView();
        allInstView.addView(newInstView);
        instructionCount++;

        final Button addInstructionBtn = (Button) findViewById(R.id.addAnotherInstruction);
        addInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout allInstView = (LinearLayout) findViewById(R.id.addInstruction);
                LinearLayout newInstView = (LinearLayout) createAddInstructionView();
                allInstView.addView(newInstView);
                instructionCount++;
                if (instructionCount > 1) {
                    delInstructionBtn.setVisibility(View.VISIBLE);
                    delInstructionBtn.setClickable(true);
                    delInstructionBtn.setFocusable(true);
                }
            }
        });

        // When add recipe button clicked, create a recipe object and go to its page
        // Don't come back to add page?
        final Button addRecipeBtn = (Button) findViewById(R.id.addNewRecipeBtn);
        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override // TODO: ADD RECIPE TO DATABASE
            public void onClick(View v) {
                // Set up new recipe object parameters
                String newName;
                LinkedList<String> newInstructions = new LinkedList<>();
                List<Ingredient> newIngredients = new ArrayList<>();
                String newImgURL;

                newName = ((EditText)findViewById(R.id.addRecipeName)).
                        getText().toString();
                newImgURL = ((EditText)findViewById(R.id.addRecipeImgUrl)).
                        getText().toString();

                // Get ingredients from edittexts
                LinearLayout allIngredients = (LinearLayout) findViewById(R.id.addIngredient);
                for (int i = 0; i < allIngredients.getChildCount(); i++) {
                    LinearLayout ingredientItem = (LinearLayout)
                            allIngredients.getChildAt(i);

                    EditText newQuantity = (EditText)
                            ingredientItem.findViewById(R.id.addIngredientQuantityItem);
                    double newQuantityNum =  Double.parseDouble(newQuantity.getText().toString());

                    Spinner newUnit = (Spinner)
                            ingredientItem.findViewById(R.id.addIngredientUnitItem);
                    Ingredient.Unit newUnitEnum = stringToUnit(newUnit.getSelectedItem().toString());

                    EditText newIngName = (EditText)
                            ingredientItem.findViewById(R.id.addIngredientNameItem);
                    String newIngNameStr = newIngName.getText().toString();

                    Ingredient newIngredient = new Ingredient(newQuantityNum,
                            newUnitEnum, newIngNameStr);
                    newIngredients.add(newIngredient);
                }

                // Get instructions from edittexts
                LinearLayout allInstructions = (LinearLayout) findViewById(R.id.addInstruction);
                for (int i = 0; i < allInstructions.getChildCount(); i++) {
                    LinearLayout instructionItem =
                            (LinearLayout) allInstructions.getChildAt(i);
                    EditText newInstruction = (EditText)
                            instructionItem.findViewById(R.id.addInstructionSingleItem);
                    newInstructions.add(newInstruction.getText().toString());
                }

                // Create recipe
                Recipe newRecipe = new Recipe(newName, newIngredients,
                        newInstructions, newImgURL);

                // pass to intent
                Intent intent = new Intent(AddRecipeActivity.this, RecipeActivity.class);
                Log.d("DATABASE", newRecipe.toString());
                intent.putExtra("recipe_key", newRecipe);
                startActivity(intent);
            }
        });

    }

    /**
     * Programatically create view to add another ingredient
     * @return new view
     */
    private View createAddIngredientView() {
        View newIngredientView = getLayoutInflater().
                inflate(R.layout.add_ingredient_item, null);
        Spinner unitSpinner = (Spinner) newIngredientView.
                findViewById(R.id.addIngredientUnitItem);
        unitSpinner.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, Ingredient.Unit.values()
        )); // TODO: set default for spinner?
        return newIngredientView;
    }

    /**
     * Programatically create view to add another instruction
     * @return new view
     */
    private View createAddInstructionView() {
        View newInstructionView = getLayoutInflater().
                inflate(R.layout.add_instruction_item, null);
        TextView numInst = (TextView) newInstructionView.
                findViewById(R.id.addInstructionNumItem);
        numInst.setText(Integer.toString(instructionCount + 1));
        return newInstructionView;
    }
}

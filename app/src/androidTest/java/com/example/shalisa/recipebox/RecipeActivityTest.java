package com.example.shalisa.recipebox;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class RecipeActivityTest extends
        ActivityInstrumentationTestCase2<RecipeActivity> {

    private RecipeActivity mRecipeActivity;
    private TextView recipeName;
    private ImageView recipeImg;
    private LinearLayout recipeIngreds;
    private LinearLayout recipeInstrs;

    Recipe eggs;

    public RecipeActivityTest() {
        super(RecipeActivity.class);
    }

    @Rule
    public ActivityTestRule<RecipeActivity> intentsRule =
            new ActivityTestRule<>(RecipeActivity.class); // touch mode, launch activity

    /**
     * Set up variable declarations.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // prevents the UI control from taking focus when you click programmatically.
        setActivityInitialTouchMode(true);

        // Create eggs recipe
        ArrayList<Ingredient> eggs_ing = new ArrayList<>();

        eggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        eggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> eggs_dir = new ArrayList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        String eggs_img = "http://toriavey.com/images/2014/06/How-to-Scramble-Eggs.jpg";
        eggs = new Recipe("Eggs", eggs_ing, eggs_dir, eggs_img);

        Intent intent = new Intent();
        intent.putExtra("recipe_key", eggs);
        setActivityIntent(intent);

        mRecipeActivity = getActivity();
        recipeName = (TextView) mRecipeActivity.findViewById(R.id.recipeTitle);
        recipeImg = (ImageView) mRecipeActivity.findViewById(R.id.recipeImg);
        recipeIngreds = (LinearLayout) mRecipeActivity.
                findViewById(R.id.recipeIngredients);
        recipeInstrs = (LinearLayout) mRecipeActivity.
                findViewById(R.id.recipeInstructions);

    }

    /**
     * Tests existence of BrowseActivity object to verify
     * that app and test code were configured correctly.
     */
    @Test
    public void testPreconditionsRecipeActivity() {
        assertNotNull("mRecipeActivity is null", mRecipeActivity);
        assertNotNull("recipeName is null", recipeName);
        assertNotNull("recipeImg is null", recipeImg);
        assertNotNull("recipeIngreds is null", recipeIngreds);
        assertNotNull("recipeInstrs is null", recipeInstrs);
    }

}

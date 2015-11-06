package com.example.shalisa.recipebox;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private RecipeActivity mRecipeActivity;
    private TextView recipeName;
    private ImageView recipeImg;
    private LinearLayout recipeIngreds;
    private LinearLayout recipeInstrs;

    Recipe eggs;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule =
            new ActivityTestRule<>(RecipeActivity.class);

    /**
     * Set up variable declarations.
     */
    @Before
    public void setUp() throws Exception {
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

        intending(not(isInternal())).respondWith(new
                Instrumentation.ActivityResult(Activity.RESULT_OK, intent));
        onView(withId(R.id.recipeBtn)).perform(click());

        intended(allOf(
                hasAction(is(equalTo(Intent.ACTION_CALL))),
//                hasData(equalTo(eggs)),
                toPackage("com.example.shalisa.recipebox.RecipeActivity")));

        mRecipeActivity = mActivityRule.getActivity();
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

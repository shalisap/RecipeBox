package com.example.shalisa.recipebox;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AddRecipeActivityTest {

    @Rule
    public IntentsTestRule<AddRecipeActivity> mActivityRule =
            new IntentsTestRule<>(AddRecipeActivity.class);

    private AddRecipeActivity mAddRecipeActivity;
    private LinearLayout ingredientList;
    private int recipeCount;

    /**
     * Set up variable declarations.
     */
    @Before
    public void setUp() {
        mAddRecipeActivity = mActivityRule.getActivity();
        ingredientList = (LinearLayout) mAddRecipeActivity.
                findViewById(R.id.addIngredient);
//        recipeCount = recipeList.getCount();
    }

    /**
     * Tests existence of AddRecipeActivity object to verify
     * that app and test code were configured correctly.
     */
    @Test
    public void testPreconditionsAddRecipeActivity() {
        assertNotNull("mAddRecipeActivity is null", mAddRecipeActivity);
        assertNotNull("ingredientList is null", ingredientList);
//        assertNotNull("recipeCount is null", recipeCount);
    }

    @Test
    public void fillingInFieldsCorrectlyTriggersRecipeActivity() {

        LinkedList<String> newInstructions = new LinkedList<>();
        newInstructions.add("Mix together");
        List<Ingredient> newIngredients = new ArrayList<>();
        Ingredient newIng = new Ingredient(1, Ingredient.Unit.PINCH, "salt");
        newIngredients.add(newIng);
        Recipe newRecipe = new Recipe("Cookies", newIngredients,newInstructions,
                "https://upload.wikimedia.org/wikipedia/commons/b/b9/Chocolate_Chip_Cookies_-_kimberlykv.jpg");

        onView(withId(R.id.addRecipeName)).perform(typeText(newRecipe.getName()));

        // get first
        LinearLayout ingredientView = (LinearLayout)ingredientList.getChildAt(0);
        Ingredient firstIngredient = newRecipe.getIngredients().get(0);
        onView(withHint("Quantity")).perform(typeText(Double.toString(firstIngredient.getQuantity())));

        // spinner
//        onView(withSpinnerText("PINCH")).perform(click());

        onView(withHint("Name")).perform(typeText(firstIngredient.getName()));

        onView(withHint("Instruction")).perform(scrollTo(), typeText(newRecipe.getInstructions().get(0)));
        onView(withId(R.id.addRecipeImgUrl)).perform(scrollTo(),
                typeText(newRecipe.getImgURL()));
        onView(withId(R.id.addNewRecipeBtn)).perform(scrollTo(), click());

        intended(allOf(
                hasComponent(RecipeActivity.class.getName()),
                toPackage("com.example.shalisa.recipebox")));
//                hasExtra("recipe_key", newRecipe)));


    }

}

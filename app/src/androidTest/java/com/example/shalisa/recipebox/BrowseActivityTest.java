package com.example.shalisa.recipebox;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BrowseActivityTest {

    @Rule
    public IntentsTestRule<BrowseActivity> mActivityRule =
            new IntentsTestRule<>(BrowseActivity.class);

    private BrowseActivity mBrowseActivity;
    private ListView recipeList;
    private int recipeCount;

    /**
     * Set up variable declarations.
     */
    @Before
    public void setUp() throws Exception {
        mBrowseActivity = mActivityRule.getActivity();
        recipeList = (ListView) mBrowseActivity.
                findViewById(R.id.browseRecipes);
        recipeCount = recipeList.getCount();
    }

    /**
     * Tests existence of BrowseActivity object to verify
     * that app and test code were configured correctly.
     */
    @Test
    public void testPreconditionsBrowseActivity() {
        assertNotNull("mBrowseActivity is null", mBrowseActivity);
        assertNotNull("recipeList is null", recipeList);
        assertNotNull("recipeCount is null", recipeCount);
    }

    /**
     * Checks that RecipeActivity is launched properly given
     * the first recipe is clicked on and the correct object
     * is passed into the intent.
     */
    @Test
    public void triggerIntentTestFirstToRecipeActivity() {
        int firstPosition = recipeList.getFirstVisiblePosition();
        onData(allOf()).atPosition(firstPosition).onChildView(
                withId(R.id.recipeBtn)).perform(click());

        intended(allOf(
                hasComponent("com.example.shalisa.recipebox.RecipeActivity"),
                toPackage("com.example.shalisa.recipebox"),
                hasExtra("recipe_key", recipeList.getItemAtPosition(firstPosition))
        ));
    }

    /**
     * Checks that RecipeActivity is launched properly given
     * the last recipe is clicked on and the correct object
     * is passed into the intent.
     */
    @Test
    public void triggerIntentTestLastToRecipeActivity() {
        int lastPosition = recipeList.getLastVisiblePosition();
        onData(allOf()).atPosition(lastPosition).onChildView(
                withId(R.id.recipeBtn)).perform(click());

        intended(allOf(
                hasComponent("com.example.shalisa.recipebox.RecipeActivity"),
                toPackage("com.example.shalisa.recipebox"),
                hasExtra("recipe_key", recipeList.getItemAtPosition(lastPosition))
        ));
    }

    /**
     * Checks number of data items is equal to number of visible views
     */
    @Test
    public void testNumberDataItemsEqualsNumberVisible() {
        int count = 0;
        for (int i = recipeList.getFirstVisiblePosition();
             i <= recipeList.getLastVisiblePosition(); i++) {
            if (recipeList.getChildAt(i) != null) {
                count++;
            }
        }
        assertEquals(recipeCount, count);
    }

    /**
     * Verify each item in RecipesList is displayed correctly in the activity.
     * Checks that an image of the recipe is displayed (above the name of the recipe).
     * Checks that text of the recipe name that is set by layout is
     * the same as the expected text defined in strings.xml.
     */
    @UiThreadTest
    public void testBrowseActivityItem_layout() {
        for (int i = 0; i < recipeCount; i++) {
            // get object at that position
            Recipe recipe = (Recipe) recipeList.getItemAtPosition(i);

            LinearLayout recipeItem = (LinearLayout) recipeList.getAdapter().getView(i, null, null);
            ImageButton recipeImg = (ImageButton) recipeItem.getChildAt(0);
            TextView recipeText = (TextView) recipeItem.getChildAt(1);

            assertNotNull("Recipe image null", recipeImg);
            assertNotNull("Recipe text null", recipeText);

            // check that the name of the recipe is correct
            assertEquals(recipe.getName(), recipeText.getText().toString());

            // check layout exist
            final ViewGroup.LayoutParams textLayoutParams = recipeText.getLayoutParams();
            assertNotNull(textLayoutParams);

            final ViewGroup.LayoutParams imgLayoutParams = recipeImg.getLayoutParams();
            assertNotNull(imgLayoutParams);

            // TODO: check gravity of imagebutton?, more tests with layout????
            //onView(withId(recipeImg.getId())).check(isAbove(withId(recipeText.getId())));
        }
    }
}

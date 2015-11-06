package com.example.shalisa.recipebox;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    private MainActivity mMainActivity;
    private TextView appName;
    private Button browseButton;
    private Button favoritesButton;

    /**
     * Set up variable declarations.
     */
    @Before
    public void setUp() {
        mMainActivity = mActivityRule.getActivity();
        appName = (TextView) mMainActivity.
                findViewById(R.id.appNameMenuText);
        browseButton = (Button) mMainActivity.
                findViewById(R.id.browseBtn);
        favoritesButton = (Button) mMainActivity.
                findViewById(R.id.favoritesBtn);
    }

    /**
     * Tests existence of MainActivity object to verify
     * that app and test code were configured correctly.
     */
    @Test
    public void testPreconditionsMainActivity() {
        assertNotNull("mMainActivity is null", mMainActivity);
        assertNotNull("appName is null", appName);
        assertNotNull("browseButton is null", browseButton);
        assertNotNull("favoritesButton is null", favoritesButton);
    }

    /**
     * Checks that text of TextView that is set by layout is
     * the same as the expected text defined in strings.xml.
     */
    @Test
    public void testMainActivityTextView_labelText() {
        final String actualText = getTextFromStringXML(R.string.app_name);
        compareText(R.id.appNameMenuText, actualText);
    }

    /**
     * Compares text of given id to an actual string.
     */
    public ViewInteraction compareText(int expected_id, String actual) {
        return onView(withId(expected_id)).check(matches(withText(actual)));
    }

    /**
     * Given an id, returns the text from the
     * corresponding string.xml.
     */
    public String getTextFromStringXML(int id) {
        return mActivityRule.getActivity().getString(id);
    }

    /**
     * Verify BrowseButton and FavoritesButton is displayed correctly in the activity.
     * Check that text of the buttons that are set by layout is
     * the same as the expected text defined in strings.xml. Also
     * check the browsebutton is above the favoritesbutton.
     */
    @Test
    public void mainActivityButtons_layoutAndLabel() {
        // Browse button text
        final String actualBrowse = getTextFromStringXML(R.string.browse_text);
        compareText(R.id.browseBtn, actualBrowse);

        // Favorites button text
        final String actualFav = getTextFromStringXML(R.string.favorites_text);
        compareText(R.id.favoritesBtn, actualFav);

        // Check that the browse button is above the favorites button
        onView(withId(R.id.browseBtn)).check(isAbove(withId(R.id.favoritesBtn)));
    }

    /**
     * Test new intent when clicking browse button
     */
    @Test
    public void testMainActivityButton_nextActivityLaunchedWithIntent() {
        onView(withId(R.id.favoritesBtn)).perform(click()); // press the browse button
        onView(withId(R.id.appNameMenuText)).check(matches(withText("HELLO")));

//        onView(withId(R.id.browseBtn)).perform(click()); // press the browse button
//        onData(withId(R.id.)).check(matches(withText("Pancakes")));
    }

    /**
     * Test that BrowseActivity is intended when the
     * browse button is clicked.
     */
    @Test
    public void testMainActivityBrowseButton_toBrowseActivityOnClick() {
        onView(withId(R.id.browseBtn)).check(matches(isClickable()));
        onView(withId(R.id.browseBtn)).perform(click());
        intended(allOf(
                hasComponent(BrowseActivity.class.getName()),
                toPackage("com.example.shalisa.recipebox")
        ));
    }

}

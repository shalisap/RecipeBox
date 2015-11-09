package com.example.shalisa.recipebox;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
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
    private TextView welcomeText;
    private Button browseButton;
    private Button favoritesButton;
    private Button addRecipeButton;
    private Button logoutButton;
    private SessionManager session;

    /**
     * Set up variable declarations.
     */
    @Before
    public void setUp() {
        session = new SessionManager(
                mActivityRule.getActivity());
        session.deleteLoginSession();
        session.createLoginSession("Shalisa");

        mMainActivity = mActivityRule.getActivity();
        welcomeText = (TextView) mMainActivity.
                findViewById(R.id.welcomeText);
        browseButton = (Button) mMainActivity.
                findViewById(R.id.browseBtn);
        favoritesButton = (Button) mMainActivity.
                findViewById(R.id.favoritesBtn);
        addRecipeButton = (Button) mMainActivity.
                findViewById(R.id.addRecipeBtn);
        logoutButton = (Button) mMainActivity.
                findViewById(R.id.logoutBtn);
    }

    /**
     * Tests existence of MainActivity object to verify
     * that app and test code were configured correctly.
     */
    @Test
    public void testPreconditionsMainActivity() {
        assertNotNull("mMainActivity is null", mMainActivity);
        assertNotNull("welcomeText is null", welcomeText);
        assertNotNull("browseButton is null", browseButton);
        assertNotNull("favoritesButton is null", favoritesButton);
    }

    /**
     * Checks that text of TextView that is set by layout is
     * the same as the expected text defined in strings.xml.
     */
    @Test
    public void testMainActivityTextView_labelText() {
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);
        String actualWelcome = "Welcome " + username;

        compareText(R.id.welcomeText, actualWelcome);
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
     * Verify BrowseButtonm FavoritesButton, and LogoutButton
     * are displayed correctly in the activity.
     * Check that text of the buttons that are set by layout is
     * the same as the expected text defined in strings.xml. Also
     * check the browsebutton is above the favoritesbutton, which is above
     * the logoutbutton.
     */
    @Test
    public void mainActivityButtons_layoutAndLabel() {
        // Browse button text
        final String actualBrowse = getTextFromStringXML(R.string.browse_text);
        compareText(R.id.browseBtn, actualBrowse);

        // Favorites button text
        final String actualFav = getTextFromStringXML(R.string.favorites_text);
        compareText(R.id.favoritesBtn, actualFav);

        // Add recipes button text
        final String actualAdd = getTextFromStringXML(R.string.add_recipe_text);
        compareText(R.id.addRecipeBtn, actualAdd);

        // Logout button text
        final String actualLogout = getTextFromStringXML(R.string.logout_text);
        compareText(R.id.logoutBtn, actualLogout);

        // Check that the browse button is above the add recipes button
        onView(withId(R.id.browseBtn)).check(isAbove(withId(R.id.addRecipeBtn)));

        // Check that the add recipes button is above the favorites button
        onView(withId(R.id.addRecipeBtn)).check(isAbove(withId(R.id.favoritesBtn)));

        // Check that the favorites button is above the logout button
        onView(withId(R.id.favoritesBtn)).check(isAbove(withId(R.id.logoutBtn)));
    }

    /**
     * Test new intent when clicking browse button
     */
    @Test
    public void testMainActivityButton_nextActivityLaunchedWithIntent() {
        onView(withId(R.id.favoritesBtn)).perform(click()); // press the browse button
        onView(withId(R.id.welcomeText)).check(matches(withText("HELLO")));

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

    /**
     * Test that LoginActivity is intended when the
     * logout button is clicked.
     */
    @Test
    public void testMainActivityLogoutButton_toLoginActivityOnClick() {
        onView(withId(R.id.logoutBtn)).check(matches(isClickable()));
        onView(withId(R.id.logoutBtn)).perform(click());
        intended(allOf(
                hasComponent(LoginActivity.class.getName()),
                toPackage("com.example.shalisa.recipebox")
        ));
    }

}

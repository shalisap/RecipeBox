package com.example.shalisa.recipebox;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private TextView appName;
    private Button browseButton;
    private Button favoritesButton;


    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Set up variable declarations.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // prevents the UI control from taking focus when you click programmatically.
        setActivityInitialTouchMode(true);

        mMainActivity = getActivity();
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
        final String expected =
                mMainActivity.getString(R.string.app_name);
        final String actual = appName.getText().toString();
        assertEquals(expected, actual);
    }

    /**
     * Verify BrowseButton is displayed correctly in the activity.
     * Also checks that text of the Browse Button that is set by layout is
     * the same as the expected text defined in strings.xml.
     */
    @Test
    public void testMainActivityBrowseButton_layout() {
        final View decorView = mMainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, browseButton);
        final ViewGroup.LayoutParams layoutParams =
                browseButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width,
                WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height,
                WindowManager.LayoutParams.WRAP_CONTENT);

        final String expected =
                mMainActivity.getString(R.string.browse_text);
        final String actual = browseButton.getText().toString();
        assertEquals(expected, actual);
    }

    /**
     * Verify FavoritesButton is displayed correctly in the activity.
     * Also checks that text of the Favorites Button that is set by layout is
     * the same as the expected text defined in strings.xml.
     */
    @Test
    public void testMainActivityFavoritesButton_layout() {
        final View decorView = mMainActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, favoritesButton);
        final ViewGroup.LayoutParams layoutParams =
                favoritesButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width,
                WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height,
                WindowManager.LayoutParams.WRAP_CONTENT);

        final String expected =
                mMainActivity.getString(R.string.favorites_text);
        final String actual = favoritesButton.getText().toString();
        assertEquals(expected, actual);
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
//
    @Rule
    public IntentsTestRule<MainActivity> intentsRule =
            new IntentsTestRule<>(MainActivity.class, true, false); // touch mode, launch activity

    @Test
    public void triggerIntentTestOnBrowseButton() {
        onView(withId(R.id.browseBtn)).perform(click());
        intended(toPackage("com.example.shalisa.recipebox.BrowseActivity"));
    }

}

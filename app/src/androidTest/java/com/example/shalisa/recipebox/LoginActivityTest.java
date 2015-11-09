package com.example.shalisa.recipebox;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        SessionManager session = new SessionManager(mActivityRule.getActivity());
        session.logoutUser(); // make sure no one is logged in before running test
    }

    @Test
    public void signInCorrect() {
        onView(withId(R.id.username)).perform(typeText("Shalisa"));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.sign_in_button)).perform(click());

        intended(allOf(
                hasComponent(MainActivity.class.getName()),
                toPackage("com.example.shalisa.recipebox")
        ));
    }

    @Test
    public void signInNoPasswordCorrect() {
        onView(withId(R.id.username)).perform(typeText("Shalisa"));
        onView(withId(R.id.sign_in_button)).perform(click());

        intended(allOf(
                hasComponent(MainActivity.class.getName()),
                toPackage("com.example.shalisa.recipebox")
        ));
    }

    @Test
    public void signInInvalidUsername() {
        onView(withId(R.id.username)).perform(typeText(""));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.sign_in_button)).perform(click());

        intended(allOf(
                hasComponent(LoginActivity.class.getName())
        ));
    }

    @Test
    public void signInNoUsername() {
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.sign_in_button)).perform(click());

        intended(allOf(
                hasComponent(LoginActivity.class.getName())
        ));
    }

    @Test
    public void signInInvalidPassword() {
        onView(withId(R.id.username)).perform(typeText("Shalisa"));
        onView(withId(R.id.password)).perform(typeText("1"));
        onView(withId(R.id.sign_in_button)).perform(click());

        intended(allOf(
                hasComponent(LoginActivity.class.getName())
        ));
    }

}

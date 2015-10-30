package com.example.shalisa.recipebox;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Tests existence of MainActivity object to verify
     * that app and test code were configured correctly.
     */
    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }
}

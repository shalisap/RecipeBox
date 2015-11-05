package com.example.shalisa.recipebox;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module( injects =
        {BrowseActivity.class, RecipeDatabase.class,
                MockRecipeDatabaseHelper.class,
                RecipeDatabaseHelper.class} )
public class RecipeDatabaseModule {

    public static boolean mockMode;

    @Provides
    @Singleton
    public RecipeDatabase provideDatabase() {
        if (mockMode) {
//            return MockRecipeDatabaseHelper.getInstance();
            return new MockRecipeDatabaseHelper();
        } return new RecipeDatabaseHelper();
        //return RecipeDatabaseHelper.getInstance();
    }

}

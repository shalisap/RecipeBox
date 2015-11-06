package com.example.shalisa.recipebox;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module( injects = {BrowseActivity.class} )
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

package com.example.shalisa.recipebox;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module( injects = {BrowseActivity.class} )
public class DatabaseModule {

    public static boolean mockMode;

    @Provides
    @Singleton
    public Database provideDatabase() {
        if (mockMode) {
            return MockRecipeDatabase.getInstance();
        } return RecipeDatabase.getInstance();
    }

}

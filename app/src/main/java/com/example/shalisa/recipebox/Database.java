package com.example.shalisa.recipebox;

import java.util.List;

public interface Database {

    /**
     * Given a recipe object, addRecipe adds the recipe
     * to the database.
     */
    public void addRecipe(Recipe recipe);

    /**
     * Returns a list of all of the recipes in the
     * database.
     */
    public List<Recipe> getAllRecipes();

    /**
     * Returns the number of recipes in the database.
     */
    public int getRecipeCount();

    /**
     * Deletes all recipes from the database.
     */
    public void deleteAllRecipes();
}

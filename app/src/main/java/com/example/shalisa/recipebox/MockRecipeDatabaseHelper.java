package com.example.shalisa.recipebox;

import java.util.ArrayList;
import java.util.List;

public class MockRecipeDatabaseHelper implements RecipeDatabase {

    // Make singleton
    private static MockRecipeDatabaseHelper sInstance;

    // List of recipes
    private List<Recipe> recipes;

    MockRecipeDatabaseHelper() {
        recipes = new ArrayList<>();
        prepopulateDatabase(this);
    }

    // Adding new recipe
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    // Getting all recipes
    public List<Recipe> getAllRecipes() {
        return recipes;
    }

    // Getting recipe count
    public int getRecipeCount() {
        return recipes.size();
    }

    // Deleting all recipes
    public void deleteAllRecipes() {
        recipes = new ArrayList<>();
    }

    // add sample recipes to database
    private static void prepopulateDatabase(MockRecipeDatabaseHelper helper) {
        // TODO: TEMP RECIPES - POSSIBLY GRAB DATA FROM API; Array or list?
        ArrayList<Ingredient> pancakes_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1.0/8.0, Ingredient.Unit.TSP, "Baking Powder"));
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.PINCH, "Ground Cinnamon"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> pancakes_dir = new ArrayList<>();
        pancakes_dir.add("Mash banana in a bowl using a fork; add eggs, baking powder " +
                "and cinnamon and mix batter well");
        pancakes_dir.add("Heat butter in a skillet over medium heat. " +
                "Spoon batter into skillet and cook until bubbles form " +
                "and the edges are dry, 2 to 3 minutes. Flip and cook " +
                "until browned on the other side, 2 to 3 minutes. " +
                "Repeat with remaining batter.");
        String pancakes_img = "http://www.onceuponachef.com/images/2013/01/banana-pancakes3.jpg";
        Recipe pancakes = new Recipe("Pancakes", pancakes_ing, pancakes_dir, pancakes_img);

        ArrayList<Ingredient> eggs_ing = new ArrayList<>();
        eggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        eggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> eggs_dir = new ArrayList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        String eggs_img = "http://toriavey.com/images/2014/06/How-to-Scramble-Eggs.jpg";
        Recipe eggs = new Recipe("Eggs", eggs_ing, eggs_dir, eggs_img);

        // Get singleton instance of database
        helper.addRecipe(pancakes);
        helper.addRecipe(eggs);
    }
}

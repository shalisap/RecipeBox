package com.example.shalisa.recipebox;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeTest {

    private String name;
    private List<Ingredient> ingredients;
    private LinkedList<String> instructions;
    private String img_url;
    private Recipe recipe;

    @Before
    public void setUp() {
        name = "pancakes";

        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        ingredients.add(new Ingredient(3, Ingredient.Unit.NONE, "Eggs"));
        ingredients.add(new Ingredient(1.0/8.0, Ingredient.Unit.TSP, "Baking Powder"));
        ingredients.add(new Ingredient(1.5, Ingredient.Unit.PINCH, "Ground Cinnamon"));
        ingredients.add(new Ingredient(2, Ingredient.Unit.TSP, "Butter"));

        instructions = new LinkedList<>();
        instructions.add("Mash banana in a bowl using a fork; add eggs, baking powder " +
                "and cinnamon and mix batter well");
        instructions.add("Heat butter in a skillet over medium heat. " +
                "Spoon batter into skillet and cook until bubbles form " +
                "and the edges are dry, 2 to 3 minutes. Flip and cook " +
                "until browned on the other side, 2 to 3 minutes. " +
                "Repeat with remaining batter.");
        img_url = "http://www.onceuponachef.com/images/2013/01/banana-pancakes3.jpg";
        recipe = new Recipe(name, ingredients, instructions, img_url);
    }

    /**
     * Given correct parameters, the constructor should
     * create a non-null object.
     */
    @Test
    public void recipeConstructorCorrect() {
        assertNotNull(recipe);
    }

    /**
     * Tests that getName returns the name
     * of the recipe.
     */
    @Test
    public void getRecipeNameCorrect() {
        assertEquals(name, recipe.getName());
    }

    /**
     * Tests that getIngredients returns the
     * ingredients list of the recipe.
     */
    @Test
    public void getIngredientsCorrect() {
        assertEquals(ingredients, recipe.getIngredients());
    }

    /**
     * Tests that getInstructions returns the
     * instructions list of the recipe.
     */
    @Test
    public void getInstructionsCorrect() {
        assertEquals(instructions, recipe.getInstructions());
    }

    /**
     * Tests that getImgURL returns the
     * single image url of the recipe.
     */
    @Test
    public void getImgURLCorrect() {
        assertEquals(img_url, recipe.getImgURL());
    }

    /**
     *  Tests that toString returns a description of the recipe
     *  in the format:
     *
     *      name:
     *      ingredient list
     *      instruction list
     *      img_url
     */
    @Test
    public void toStringCorrect() {
        String recipeString =
                name + ":\n\t" +
                "Ingredients: " + ingredients.toString() + "\n\t" +
                "Instructions: " + instructions.toString() + "\n\t" +
                "Image: " + img_url;
        assertEquals(recipeString, recipe.toString());
    }
}

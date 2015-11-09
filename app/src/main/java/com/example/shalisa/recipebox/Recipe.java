package com.example.shalisa.recipebox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent a single recipe
 * including name, ingredients, instructions, and an image url.
 *
 * An Recipe object consists of:
 *      * name - name of the recipe
 *      * ingredients - list of ingredient objects
 *      * instructions - list of instructions
 *      * img_url - image url
 */
public class Recipe implements Parcelable{

    private String name;
    private List<Ingredient> ingredients;
    private LinkedList<String> instructions;
    private String img_url;
    private boolean isFavorite;

    /**
     * Constructor for Recipe.
     * @param name The name of the recipe.
     * @param ingredients List of ingredient objects
     * @param instructions List of instructions as strings
     * @param img_url The image url of the recipe as a string
     */
    public Recipe(String name, List<Ingredient> ingredients,
                  LinkedList<String> instructions, String img_url) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.img_url = img_url;
    }

    /**
     * Getter for name
     * @return name of the recipe
     */
    public String getName() { return name; }

    /**
     * Getter for ingredients
     * @return ingredient list
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Getter for instructions
     * @return instruction list
     */
    public LinkedList<String> getInstructions() {
        return instructions;
    }

    /**
     * Getter for img_url
     * @return image url
     */
    public String getImgURL() { return img_url; }

    /**
     * @return A description of the recipe in the format:
     *      name:
     *      ingredient list
     *      instruction list
     *      img_url
     */
    public String toString() {
        return String.format("%s:\n\tIngredients: %s\n\tInstructions: %s\n\tImage: %s",
                name, ingredients.toString(),
                instructions.toString(), img_url);
    }

    /**
     * Set a recipe to favorite or not.
     * @param fav Boolean true if favorite, false if not.
     */
    public void setFavorite(boolean fav) {
        isFavorite = fav;
    }

    /**
     * Returns true if the recipe has been favorited,
     * false otherwise.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    /**
     * For implementing Parcelable
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten Recipe object into a parcel.
     * @param out The Parcel the Recipe will be written to.
     * @param flags Additional flags about how the Recipe
     *              object should be written.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeTypedList(ingredients);
        out.writeStringList(instructions);
        out.writeString(img_url);
    }

    /**
     * A public CREATOR field that generates instances
     * of Recipe's Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Constructor to create an instance of
     * Recipe from a given parcel.
     * @param in The Parcel the Recipe will be created from.
     */
    public Recipe(Parcel in) {
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients,Ingredient.CREATOR);
        instructions = new LinkedList<>();
        in.readStringList(instructions);
        img_url = in.readString();
    }
}


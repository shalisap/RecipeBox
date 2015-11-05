package com.example.shalisa.recipebox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable{

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<String> instructions;
    private String img_url;

    public Recipe(String name, List<Ingredient> ingredients,
                  List<String> instructions, String img_url) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.img_url = img_url;
    }

    public String getName() { return name; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public List<String> getInstructions() { return instructions; }

    public String getImgURL() { return img_url; }

    /**
     * For implementing Parcelable
     */
    public int describeContents() {
        return 0;
    }

    // write Recipe data to passed in parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeTypedList(ingredients);
        out.writeStringList(instructions);
        out.writeString(img_url);
    }

    // Used to regenerate data
    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(Parcel in) {
        readFromParcel(in);
    }

    // parcelling part
    private void readFromParcel(Parcel in) {
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients,Ingredient.CREATOR);
        instructions = new ArrayList<>();
        in.readStringList(instructions);
        img_url = in.readString();
    }
}


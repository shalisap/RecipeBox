package com.example.shalisa.recipebox;

import java.util.List;

public class Recipe {

    private String name;
    private List<Ingredient> ingredientList;
    private List<String> instructions;

    public Recipe(String name, List<Ingredient> ingredientList, List<String> instructions) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.instructions = instructions;
    }

    public String getName() { return name; }

}

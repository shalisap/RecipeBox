package com.example.shalisa.recipebox;

import android.text.style.TtsSpan;

public class Ingredient {
    public enum Unit {
        PINCH, TSP, TBSP, CUP, OUNCE, PINT, QUART, GALLON, NONE // TODO:CLASS TO CONVERT BETWEEN??
    }
    private double quantity; // TODO:FRACTION instead?
    private String ingredient;
    private Unit unit;

    public Ingredient(double quantity, Unit unit, String ingredient) {
        this.quantity = quantity;
        this.unit = unit;
        this.ingredient = ingredient;

    }

}

package com.example.shalisa.recipebox;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

import static org.junit.Assert.*;

public class IngredientTest {

    @Test (expected = IllegalArgumentException.class)
    public void negativeQuantityThrowsException() {
        Ingredient ing = new Ingredient(-0.5, Ingredient.Unit.CUP, "Banana");
    }

    @Test
    public void setQuantityCorrect() {
        Ingredient ing = new Ingredient(0.5, Ingredient.Unit.CUP, "Banana");
        assertEquals(ing.getQuantity(), 0.5, 0.000000001);
        ing.setQuantity(0.75);
        assertEquals(ing.getQuantity(), 0.75, 0.00000001);
    }

    @Test
    public void doublesToFractionsCorrect() {
        final double half = 0.5;
        final double third = 1.0/3.0;
        final double truncated_third = 0.3333;
        final double fourth = 0.25;
        final double one = 1;
        final double five = 5;
        final double one_half = 1.5;
        final double ten_threeFourths = 10.75;

        Ingredient ing = new Ingredient(half, Ingredient.Unit.CUP, "Banana");
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"", "1", "2"}));

        ing.setQuantity(third);
        assertThat(ing.getQuantityFraction(), equalTo(new String[]{"", "1", "3"}));

        ing.setQuantity(truncated_third);
        assertThat(ing.getQuantityFraction(), equalTo(new String[]{"", "1", "3"}));

        ing.setQuantity(fourth);
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"", "1", "4"}));

        ing.setQuantity(one);
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"1", "", ""}));

        ing.setQuantity(five);
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"5", "", ""}));

        ing.setQuantity(one_half);
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"1", "1", "2"}));

        ing.setQuantity(ten_threeFourths);
        assertThat(ing.getQuantityFraction(), equalTo(new String[] {"10", "3", "4"}));
    }

    @Test
    public void doublesToFractionStringsCorrect() {
        final double half = 0.5;
        final double one = 1;
        final double one_half = 1.5;

        Ingredient ing = new Ingredient(half, Ingredient.Unit.CUP, "Banana");
        assertEquals(ing.getQuantityFractionString(), "<sup>1</sup>/<sub>2</sub>");

        ing.setQuantity(one);
        assertEquals(ing.getQuantityFractionString(), "1");

        ing.setQuantity(one_half);
        assertEquals(ing.getQuantityFractionString(), "1<sup>1</sup>/<sub>2</sub>");

    }


}
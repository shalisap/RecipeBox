package com.example.shalisa.recipebox;

import android.os.Parcel;

import org.junit.Ignore;
import org.junit.Test;

import static com.example.shalisa.recipebox.Ingredient.doubleToFractionString;
import static com.example.shalisa.recipebox.Ingredient.quantityToFractionString;
import static com.example.shalisa.recipebox.Ingredient.stringToUnit;
import static com.example.shalisa.recipebox.Ingredient.unitToString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class IngredientTest {

    private static final double EPSILON = 10E-6;

    /**
     * Given a negative quantity, the constructor should
     * throw an illegal argument exception.
     */
    @Test (expected = IllegalArgumentException.class)
    public void negativeQuantityThrowsException() {
        Ingredient i = new Ingredient(-0.5, Ingredient.Unit.CUP, "Banana");
    }

    /**
     * Given a quantity of 0, the constructor should
     * throw an illegal argument exception.
     */
    @Test (expected = IllegalArgumentException.class)
    public void zeroQuantityThrowsException() {
        Ingredient i = new Ingredient(0.0, Ingredient.Unit.CUP, "Banana");
    }

    /**
     * Tests that getQuantity returns the quantity
     */
    @Test
    public void getQuantityCorrect() {
        final double quantity = 9.75;
        Ingredient i = new Ingredient(quantity, Ingredient.Unit.CUP, "Banana");
        assertEquals(quantity, i.getQuantity(), EPSILON);
    }

    /**
     * Tests that getName returns the name.
     */
    @Test
    public void getNameCorrect() {
        final String name = "banana";
        Ingredient i = new Ingredient(1.0, Ingredient.Unit.CUP, name);
        assertEquals(name, i.getName());
    }

    /**
     * Tests that getQuantityString returns the correct fraction
     * formatted quantity string.
     * quantityToFractionStringCorrect tests correctness
     * of quantity to string.
     */
    @Test
    public void getQuantityStringCorrect() {
        final double one_half = 1.50;
        final String fOne_half = quantityToFractionString(one_half);
        Ingredient i = new Ingredient(one_half, Ingredient.Unit.CUP, "Banana");
        assertEquals(fOne_half, i.getQuantityString());
    }

    /**
     * Tests that getUnitString returns the correct unit string.
     * unitToStringCorrect tests correctness
     * of unit to string.
     */
    @Test
    public void getUnitStringCorrect() {
        final Ingredient.Unit unit = Ingredient.Unit.CUP;
        final String unitString = "cup";
        Ingredient i = new Ingredient(0.5, unit, "Banana");
        assertEquals(unitString, i.getUnitString());
    }

    /**
     * Tests that toString returns the correct string.
     */
    @Test
    public void toStringCorrect() {
        final String ingStr = "1/2 cup banana";
        Ingredient i = new Ingredient(0.5, Ingredient.Unit.CUP, "Banana");
        assertEquals(ingStr, i.toString());
    }

    /**
     * Tests when quantity set, getQuantityFraction returns
     * the correct fraction formatted string.
     */
    @Test
    public void doublesToFractionStringCorrect() {
        // set up various fraction values
        final double half = 0.5;
        final double third = 1.0/3.0;
        final double truncated_third = 0.3333;
        final double fourth = 0.25;
        final double one = 1;
        final double five = 5;
        final double one_half = 1.5;
        final double ten_threeFourths = 10.75;

        assertThat("Did not get 1/2 from 0.5.",
                doubleToFractionString(half),
                equalTo(new String[] {"", "1", "2"}));

        assertThat("Did not get 1/3 from 1.0/3.0",
                doubleToFractionString(third),
                equalTo(new String[]{"", "1", "3"}));

        assertThat("Did not get 1/3 from 0.3333",
                doubleToFractionString(truncated_third),
                equalTo(new String[]{"", "1", "3"}));

        assertThat("Did not get 1/4 from 0.25",
                doubleToFractionString(fourth),
                equalTo(new String[]{"", "1", "4"}));

        assertThat("Did not get 1 from 1",
                doubleToFractionString(one),
                equalTo(new String[]{"1", "", ""}));

        assertThat("Did not get 5 from 5",
                doubleToFractionString(five),
                equalTo(new String[]{"5", "", ""}));

        assertThat("Did not get 1 1/2 from 1.5",
                doubleToFractionString(one_half),
                equalTo(new String[]{"1", "1", "2"}));

        assertThat("Did not get 10 3/4 from 10.75",
                doubleToFractionString(ten_threeFourths),
                equalTo(new String[]{"10", "3", "4"}));
    }

    /**
     * Tests that given a quantity, quantityToFractionString
     * returns the correct fraction formatted string.
     */
    @Test
    public void quantityToFractionStringCorrect() {
        final double half = 0.5;
        final double one = 1;
        final double one_half = 1.5;

        assertEquals("1/2", quantityToFractionString(half));
        assertEquals("1", quantityToFractionString(one));
        assertEquals("1 1/2",quantityToFractionString(one_half));
    }

    /**
     * Tests that given a unit and quantity, unitToString returns the
     * correct string representation given different quantity values.
     */
    @Test
    public void unitToStringCorrect_variousQuantities() {
        assertEquals("pinches", unitToString(Ingredient.Unit.PINCH, 2));
        assertEquals("pinch", unitToString(Ingredient.Unit.PINCH, 0.2));
        assertEquals("cups", unitToString(Ingredient.Unit.CUP, 5.35));
        assertEquals("cup", unitToString(Ingredient.Unit.CUP, 1));
        assertEquals("", unitToString(Ingredient.Unit.NONE, 0.5));
        assertEquals("", unitToString(Ingredient.Unit.NONE, 9));
    }

    /**
     * Tests that given a unit and quantity, unitToString returns the
     * correct string representation given different unit values.
     */
    @Test
    public void unitToStringCorrect_variousUnits() {
        assertEquals("pinches", unitToString(Ingredient.Unit.PINCH, 2));
        assertEquals("pinch", unitToString(Ingredient.Unit.PINCH, 0.5));
        assertEquals("teaspoons", unitToString(Ingredient.Unit.TSP, 2));
        assertEquals("teaspoon", unitToString(Ingredient.Unit.TSP, 0.5));
        assertEquals("tablespoons", unitToString(Ingredient.Unit.TBSP, 2));
        assertEquals("tablespoon", unitToString(Ingredient.Unit.TBSP, 0.5));
        assertEquals("cups", unitToString(Ingredient.Unit.CUP, 2));
        assertEquals("cup", unitToString(Ingredient.Unit.CUP, 0.5));
        assertEquals("ounces", unitToString(Ingredient.Unit.OUNCE, 2));
        assertEquals("ounce", unitToString(Ingredient.Unit.OUNCE, 0.5));
        assertEquals("pints", unitToString(Ingredient.Unit.PINT, 2));
        assertEquals("pint", unitToString(Ingredient.Unit.PINT, 0.5));
        assertEquals("quarts", unitToString(Ingredient.Unit.QUART, 2));
        assertEquals("quart", unitToString(Ingredient.Unit.QUART, 0.5));
        assertEquals("gallons", unitToString(Ingredient.Unit.GALLON, 2));
        assertEquals("gallon", unitToString(Ingredient.Unit.GALLON, 0.5));
        assertEquals("", unitToString(Ingredient.Unit.NONE, 2));
        assertEquals("", unitToString(Ingredient.Unit.NONE, 0.5));
    }

    /**
     * Tests that given a string, stringToUnit returns the
     * correct unit representation.
     */
    @Test
    public void stringToUnitCorrect() {
        assertEquals(Ingredient.Unit.PINCH, stringToUnit("pinches"));
        assertEquals(Ingredient.Unit.PINCH, stringToUnit("pinch"));
        assertEquals(Ingredient.Unit.TSP, stringToUnit("teaspoons"));
        assertEquals(Ingredient.Unit.TSP, stringToUnit("teaspoon"));
        assertEquals(Ingredient.Unit.TBSP, stringToUnit("tablespoons"));
        assertEquals(Ingredient.Unit.TBSP, stringToUnit("tablespoon"));
        assertEquals(Ingredient.Unit.CUP, stringToUnit("cups"));
        assertEquals(Ingredient.Unit.CUP, stringToUnit("cup"));
        assertEquals(Ingredient.Unit.OUNCE, stringToUnit("ounces"));
        assertEquals(Ingredient.Unit.OUNCE, stringToUnit("ounce"));
        assertEquals(Ingredient.Unit.PINT, stringToUnit("pints"));
        assertEquals(Ingredient.Unit.PINT, stringToUnit("pint"));
        assertEquals(Ingredient.Unit.QUART, stringToUnit("quarts"));
        assertEquals(Ingredient.Unit.QUART, stringToUnit("quart"));
        assertEquals(Ingredient.Unit.GALLON, stringToUnit("gallons"));
        assertEquals(Ingredient.Unit.NONE, stringToUnit(""));
    }

    // TODO: FIGURE OUT WHERE TO TEST PARCELABLE
    /**
     * To test custom parcelable
     */
    @Ignore
    public void testParcelable() {
        Ingredient i = new Ingredient(0.5, Ingredient.Unit.CUP, "Banana");
        Parcel parcel = Parcel.obtain();
        i.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Ingredient parceledI = (Ingredient) Ingredient.CREATOR.createFromParcel(parcel);
        assertEquals(i, parceledI);
    }
}
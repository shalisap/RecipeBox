package com.example.shalisa.recipebox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to represent a single ingredient (in a recipe),
 * including quantity and unit.
 *
 * An Ingredient object consists of:
 *      * quantity - amount of the ingredient as a double
 *      * unit - unit of measurement
 *      * name - name of ingredient as a string
 *
 */
public class Ingredient implements Parcelable {

    public enum Unit {
        PINCH, TSP, TBSP, CUP, OUNCE, PINT, QUART, GALLON, NONE // TODO:CLASS TO CONVERT BETWEEN??
    }

    private double quantity; // quantity of the ingredient
    private Unit unit; // unit of the ingredient
    private String name; // name of the ingredient
    // ex. 0.5 CUP flour

    /**
     * Constructor for Ingredient.
     * @param quantity The amount (double) of the ingredient.
     * @param unit The unit of the ingredient.
     * @param name The name of the ingredient.
     */
    public Ingredient(double quantity, Unit unit, String name) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
        this.unit = unit;
        this.name = name.toLowerCase();
    }

    /**
     * Returns the quantity (double) field of Ingredient.
     * @return Double quantity of the ingredient.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Returns the name of the ingredient.
     * @return String name of the ingredient.
     */
    public String getName() {
        return name;
    }

    /**
     * Given a double, converts to a fraction string[]
     * consisting of [integer, numerator, denominator]
     * where any value of 0 is represented by the empty string.
     *
     * @return A string[] representation of the quantity
     *      where [integer, numerator, denominator].
     */
    public String[] doubleToFractionString(double quantity) {
        String[] fraction = new String[3];

        double q = quantity;
        long integer = (long) q; // Calculates integer value and add it to fraction[0]
        fraction[0] = integer != 0 ? Long.toString(integer) : "";  // If 0, add ""

        q -= integer; // Subtract integer from rest - q is now a decimal value
        double error = q;
        int bestDenominator = 1;
        for (int i = 2; i <= 16; i++) { //largest denominator is 16 - recipes don't typically exceed that
            // compute denominator
            double error2 = Math.abs(q - (double) Math.round(q * i) / i);
            if (error2 < error) {
                error = error2;
                bestDenominator = i;
            }
        }
        if (bestDenominator > 1) {
            fraction[1] = Long.toString(Math.round(q * bestDenominator));
            fraction[2] = Integer.toString(bestDenominator);
        } else {
            fraction[1] = "";
            fraction[2] = "";
        } return fraction;
    }

    /**
     * Converts a double to a fraction string and returns
     * the formatted fraction as a string.
     * @return A string fraction representation of the quantity.
     */
    public String getQuantityString() {
        String[] fractionArr = doubleToFractionString(quantity);
        StringBuilder fractionStr = new StringBuilder();
        if (fractionArr[0] != "") {
            fractionStr.append(fractionArr[0]);
            fractionStr.append(" ");
        }
        if (fractionArr[1] != "" && fractionArr[2] != "") {
            fractionStr.append(fractionArr[1]);
            fractionStr.append("/");
            fractionStr.append(fractionArr[2]);
        } return fractionStr.toString();
    }

    /**
     * @return String representation of the
     *      unite of measurement.
     */
    public String getUnitString() {
        String unitStr = "";
        switch(unit) {
            case PINCH:
                unitStr += "pinch";
                break;
            case TSP:
                unitStr += "teaspoon";
                break;
            case TBSP:
                unitStr += "tablespoon";
                break;
            case CUP:
                unitStr += "cup";
                break;
            case OUNCE:
                unitStr += "ounce";
                break;
            case PINT:
                unitStr += "pint";
                break;
            case QUART:
                unitStr += "quart";
                break;
            case GALLON:
                unitStr += "gallon";
                break;
            default:
                break;
        }
        if (quantity > 1 && unit != Unit.PINCH && unitStr != "") {
            return unitStr + "s";
        } else if (quantity > 1 && unit == Unit.PINCH) {
            return unitStr + "es";
        } else { return unitStr; }
    }

    /**
     * Returns the string representation of
     * the ingredient as:
     *      "quantity_as_a_fraction unit_as_a_string name"
     * @return Ingredient as a string
     */
    public String toString() {
        return String.format("%s %s %s",
                getQuantityString(), getUnitString(), getName());
    }

    /**
     * @param str Unit represented as a String.
     * @return Unit
     */
    public static Unit stringToUnit(String str) {
        switch(str.toLowerCase()) {
            case "pinch":
            case "pinches":
                return Unit.PINCH;
            case "teaspoon":
            case "teaspoons":
                return Unit.TSP;
            case "tablespoon":
            case "tablespoons":
                return Unit.TBSP;
            case "cup":
            case "cups":
                return Unit.CUP;
            case "ounce":
                return Unit.OUNCE;
            case "pint":
            case "ounces":
                return Unit.PINT;
            case "quart":
            case "quarts":
                return Unit.QUART;
            case "gallon":
            case "gallons":
                return Unit.GALLON;
            default:
                return Unit.NONE;
        }
    }

    /**
     * For implementing Parcelable
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten Ingredient object into a parcel.
     * @param out The Parcel the Ingredient will be written to.
     * @param flags Additional flags about how the Ingredient
     *              object should be written.
     */
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(quantity);
        out.writeSerializable(unit);
        out.writeString(name);
    }

    /**
     * A public CREATOR field that generates instances
     * of Ingredient's Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Ingredient createFromParcel(Parcel in) {
                    return new Ingredient(in);
                }

                public Ingredient[] newArray(int size) {
                    return new Ingredient[size];
                }
            };

    /**
     * Constructor to create an instance of
     * Ingredient from a given parcel.
     * @param in The Parcel the Ingredient will be created from.
     */
    public Ingredient(Parcel in) {
        quantity = in.readDouble();
        unit = (Unit) in.readSerializable();
        name = in.readString();
    }
}

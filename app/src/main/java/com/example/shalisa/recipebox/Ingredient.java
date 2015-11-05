package com.example.shalisa.recipebox;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    public enum Unit {
        PINCH, TSP, TBSP, CUP, OUNCE, PINT, QUART, GALLON, NONE // TODO:CLASS TO CONVERT BETWEEN??
    }

    private int id;
    private double quantity; // TODO:FRACTION instead?
    private String ingredient;
    private Unit unit;

    public Ingredient(double quantity, Unit unit, String ingredient) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
        this.unit = unit;
        this.ingredient = ingredient.toLowerCase();

    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    // TODO: Plural?
    public String unitToString() {
        switch(unit) {
            case PINCH:
                return "pinch";
            case TSP:
                return "teaspoon";
            case TBSP:
                return "tablespoon";
            case CUP:
                return "cup";
            case OUNCE:
                return "ounce";
            case PINT:
                return "pint";
            case QUART:
                return "quart";
            case GALLON:
                return "gallon";
            default:
                return "";
        }
    }

    public static Unit stringToUnit(String str) {
        switch(str.toLowerCase()) {
            case "pinch":
                return Unit.PINCH;
            case "teaspoon":
                return Unit.TSP;
            case "tablespoon":
                return Unit.TBSP;
            case "cup":
                return Unit.CUP;
            case "ounce":
                return Unit.OUNCE;
            case "pint":
                return Unit.PINT;
            case "quart":
                return Unit.QUART;
            case "gallon":
                return Unit.GALLON;
            default:
                return Unit.NONE;
        }
    }

    /**
     * Converts a double to a fraction string.
     * @return A string[] representation of the quantity
     *      where [integer, numerator, denominator].
     */
    public String[] getQuantityFraction() {
        String[] fraction = new String[3];
        double q = quantity;
        long integer = (long) q;
        fraction[0] = integer != 0 ? Long.toString(integer) : "";
        q -= integer;
        double error = Math.abs(q);
        int bestDenominator = 1;
        for (int i = 2; i <= 16; i++) { //largest denominator is 16 - recipes don't typically exceed that
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
     * the formatted fraction as a string..
     * @return A string fraction representation of the quantity.
     */
    public String getQuantityFractionString() {
        String[] fractionArr = getQuantityFraction();
        StringBuilder fractionStr = new StringBuilder();
        if (fractionArr[0] != "") {
            fractionStr.append(fractionArr[0]);
            fractionStr.append(" ");
        }
        if (fractionArr[1] != "" && fractionArr[2] != "") {
//            fractionStr.append("<sup>");
//            fractionStr.append(fractionArr[1]);
//            fractionStr.append("</sup>/<sub>");
//            fractionStr.append(fractionArr[2]);
//            fractionStr.append("</sub>");
            fractionStr.append(fractionArr[1]);
            fractionStr.append("/");
            fractionStr.append(fractionArr[2]);
        } return fractionStr.toString();
    }

    /**
     * For implementing Parcelable
     */
    public int describeContents() {
        return 0;
    }

    // write Recipe data to passed in parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(quantity);
        out.writeSerializable(unit);
        out.writeString(ingredient);
    }

    // Used to regenerate data
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Ingredient createFromParcel(Parcel in) {
                    return new Ingredient(in);
                }

                public Ingredient[] newArray(int size) {
                    return new Ingredient[size];
                }
            };

    public Ingredient(Parcel in) {
        readFromParcel(in);
    }

    // parcelling part
    private void readFromParcel(Parcel in) {
        quantity = in.readDouble();
        unit = (Unit) in.readSerializable();
        ingredient = in.readString();
    }

}

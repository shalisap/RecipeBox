package com.example.shalisa.recipebox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    // Make singleton
    private static RecipeDatabaseHelper sInstance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipesDatabase";
    private static final String TAG = "DATABASE";

    // table names
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_INGREDIENTS = "ingredients";

    // recipe table columns
    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_INSTRUCTIONS = "instructions";
    private static final String KEY_RECIPE_IMGURL = "img_url";

    // ingredients table columns
    private static final String KEY_RECIPE_ING_ID_FK = "recipeId";
    private static final String KEY_INGREDIENTS_ID = "id";
    private static final String KEY_INGREDIENTS_QUANTITY = "quantity";
    private static final String KEY_INGREDIENTS_ING = "ingredient";
    private static final String KEY_INGREDIENTS_UNIT = "unit";


    private RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // In an activity pass context, use singleton method -
    // RecipeDatabaseHelper helper = RecipeDatabaseHelper.getInstance(this);
    public static synchronized RecipeDatabaseHelper
        getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RecipeDatabaseHelper(context);
        } return sInstance;
    }

    // Called when database connection is being configured
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + " ("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY," + KEY_RECIPE_NAME + " TEXT,"
                + KEY_RECIPE_INSTRUCTIONS + " TEXT,"
                + KEY_RECIPE_IMGURL + " TEXT" + ")";

        String CREATE_INGRED_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                KEY_INGREDIENTS_ID + " INTEGER PRIMARY KEY," +
                KEY_RECIPE_ING_ID_FK + " INTEGER REFERENCES " + TABLE_RECIPES + "," +
                KEY_INGREDIENTS_QUANTITY + " TEXT," +
                KEY_INGREDIENTS_UNIT + " TEXT," +
                KEY_INGREDIENTS_ING + " TEXT" + ")";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_INGRED_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older table if existed
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);

            // create tables again
            onCreate(db);
        }
    }

    // Adding new recipe
    public void addRecipe(Recipe recipe) {
        // open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        long recipeId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RECIPE_NAME, recipe.getName()); // recipes name
            values.put(KEY_RECIPE_IMGURL, recipe.getImgURL()); // recipe image url

            // insert arraylist of instructions using JSON
            JSONObject json = new JSONObject();
            json.put("instArr", new JSONArray(recipe.getInstructions()));
            String stringInsts = json.toString();
            values.put(KEY_RECIPE_INSTRUCTIONS, stringInsts);

            // inserting row
            recipeId = db.insertOrThrow(TABLE_RECIPES, null, values);

            addIngredients(recipe.getIngredients(), recipeId);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add recipe to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or add ingredients into the database with the foreign key
    public void addIngredients(List<Ingredient> ingredients, long recipeId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            for (Ingredient ingredient : ingredients) {
                ContentValues values = new ContentValues();
                values.put(KEY_RECIPE_ING_ID_FK, recipeId); // Add foreign key
                values.put(KEY_INGREDIENTS_QUANTITY, ingredient.getQuantity());
                values.put(KEY_INGREDIENTS_UNIT, ingredient.unitToString());
                values.put(KEY_INGREDIENTS_ING, ingredient.getIngredient());

                // try add ingredient
                db.insertOrThrow(TABLE_INGREDIENTS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add ingredient");
        } finally {
            db.endTransaction();
        }
    }

    // Getting all recipes
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String RECIPES_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_RECIPES);

        SQLiteDatabase db = getReadableDatabase();
        Cursor recipeCursor = db.rawQuery(RECIPES_SELECT_QUERY, null);
        try {
            if (recipeCursor.moveToFirst()) {
                do {
                    String recipe_name = recipeCursor.getString(
                            recipeCursor.getColumnIndex(KEY_RECIPE_NAME));

                    String recipeId = recipeCursor.getString(recipeCursor.getColumnIndex(KEY_RECIPE_ID));
                    List<Ingredient> ingredients = getIngredientsList(recipeId);

                    String recipe_img_url = recipeCursor.getString(
                            recipeCursor.getColumnIndex(KEY_RECIPE_IMGURL));

                    String recipeInstrStr = recipeCursor.getString(
                            recipeCursor.getColumnIndex(KEY_RECIPE_INSTRUCTIONS));

                    JSONArray recipeInstrJson = (new JSONObject(recipeInstrStr)).optJSONArray("instArr");
                    List<String> recipeInstrArr = new ArrayList<>();
                    for (int i = 0; i < recipeInstrJson.length(); i++) {
                        recipeInstrArr.add(recipeInstrJson.getString(i));
                    }

                    Recipe recipe = new Recipe(
                            recipe_name, ingredients,
                            recipeInstrArr,
                            recipe_img_url);
                    recipes.add(recipe);
                } while (recipeCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get recipe from database");
        } finally {
            if (recipeCursor != null && !recipeCursor.isClosed()) {
                recipeCursor.close();
            }
        } return recipes;
    }

    public List<Ingredient> getIngredientsList(String recipeId) {
        List<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String INGRED_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = %s",
                        TABLE_INGREDIENTS,
                        KEY_RECIPE_ING_ID_FK, recipeId);

        Cursor ingredCursor = db.rawQuery(INGRED_SELECT_QUERY, null);
        try {
            if (ingredCursor.moveToFirst()) {
                do {
                    String quantity = ingredCursor.getString(
                            ingredCursor.getColumnIndex(KEY_INGREDIENTS_QUANTITY));
                    String unit = ingredCursor.getString(
                            ingredCursor.getColumnIndex(KEY_INGREDIENTS_UNIT));
                    String ingred = ingredCursor.getString(
                            ingredCursor.getColumnIndex(KEY_INGREDIENTS_ING));

                    Ingredient ingredient = new Ingredient(
                            Double.parseDouble(quantity),
                            Ingredient.stringToUnit(unit),
                            ingred);
                    ingredients.add(ingredient);
                } while(ingredCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get ingredients from database");
        } finally {
            if (ingredCursor != null && !ingredCursor.isClosed()) {
                ingredCursor.close();
            }
        } return ingredients;
    }

    // Getting recipe count
    public int getRecipeCount() {
        String countQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    // TODO: single recipe database CRUD
    // Updating single recipe
    //public int updateRecipe(Recipe recipe) {}

    // Delete single recipe
//    public void deleteRecipe(Recipe recipe) {
//    }

    // Deleting all recipes
    public void deleteAllRecipes() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_INGREDIENTS, null, null);
            db.delete(TABLE_RECIPES, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all recipes");
        } finally {
            db.endTransaction();
        }
    }
}

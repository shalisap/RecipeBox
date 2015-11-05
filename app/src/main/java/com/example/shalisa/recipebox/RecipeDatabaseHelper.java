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
import java.util.List;

import javax.inject.Inject;

public class RecipeDatabaseHelper extends SQLiteOpenHelper
        implements RecipeDatabase {

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



    @Inject
    RecipeDatabaseHelper() {
        super(BrowseActivity.browseContext, DATABASE_NAME, null, DATABASE_VERSION);
//        prepopulateDatabase(this);
    }
//
//    private RecipeDatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    // In an activity pass context, use singleton method -
    // RecipeDatabaseHelper helper = RecipeDatabaseHelper.getInstance(this);
//    public static synchronized RecipeDatabaseHelper
//        getInstance() {
//        if (sInstance == null) {
//            sInstance = new RecipeDatabaseHelper();
//            prepopulateDatabase(sInstance);
//        } return sInstance;
//    }

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

    // add sample recipes to database
    private static void prepopulateDatabase(RecipeDatabaseHelper helper) {
        // TODO: TEMP RECIPES - POSSIBLY GRAB DATA FROM API; Array or list?
        ArrayList<Ingredient> pancakes_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1.0/8.0, Ingredient.Unit.TSP, "Baking Powder"));
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.PINCH, "Ground Cinnamon"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> pancakes_dir = new ArrayList<>();
        pancakes_dir.add("Mash banana in a bowl using a fork; add eggs, baking powder " +
                "and cinnamon and mix batter well");
        pancakes_dir.add("Heat butter in a skillet over medium heat. " +
                "Spoon batter into skillet and cook until bubbles form " +
                "and the edges are dry, 2 to 3 minutes. Flip and cook " +
                "until browned on the other side, 2 to 3 minutes. " +
                "Repeat with remaining batter.");
        String pancakes_img = "http://www.onceuponachef.com/images/2013/01/banana-pancakes3.jpg";
        Recipe pancakes = new Recipe("Pancakes", pancakes_ing, pancakes_dir, pancakes_img);

        ArrayList<Ingredient> eggs_ing = new ArrayList<>();
        eggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        eggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));

        ArrayList<String> eggs_dir = new ArrayList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        String eggs_img = "http://toriavey.com/images/2014/06/How-to-Scramble-Eggs.jpg";
        Recipe eggs = new Recipe("Eggs", eggs_ing, eggs_dir, eggs_img);

        ArrayList<Ingredient> ceggs_ing = new ArrayList<>();
        ceggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        ceggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));
        ceggs_ing.add(new Ingredient(1.5, Ingredient.Unit.CUP, "Cheese"));

        ArrayList<String> ceggs_dir = new ArrayList<>();
        ceggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs. Add Cheese");
        String ceggs_img = "http://foodnetwork.sndimg.com/content/dam/images/food/fullset/2011/3/31/0/RE0902H_sunnys-perfect-scrambled-cheesy-eggs_s4x3.jpg";
        Recipe ceggs = new Recipe("Cheesy Eggs", ceggs_ing, ceggs_dir, ceggs_img);

        // Get singleton instance of database
        helper.addRecipe(pancakes);
        helper.addRecipe(eggs);
        helper.addRecipe(ceggs);
    }
}

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
import java.util.LinkedList;
import java.util.List;

/**
 * Database to store recipes.
 *
 * Each database consists of:
 *
 * Example:
 * Recipe Table
 * +---------+---------+--------------+---------+-----+
 * | PRI_KEY |  NAME   | INSTRUCTIONS | IMG_URL | FAV |
 * +---------+---------+--------------+---------+-----+
 * |    1    |Pancakes |pancakesinst* | pan.jpg |true |
 * |    2    |  Eggs   | eggsinst*    | egg.jpg |false|
 * +---------+---------+--------------+---------+-----+
 * where pancakesinst, eggsinst are json formatted string arrays.
 *
 * and Ingredients Table
 * +---------+-----------+----------+------+-------+
 * | PRI_KEY |RECIPE_KEY | QUANTITY | UNIT | NAME  |
 * +---------+-----------+----------+------+-------+
 * |    1    |     1     |    1     |PINCH |  salt |
 * |    2    |     1     |    0.5   | CUP  | flour |
 * |    3    |     2     |    3     | NONE | eggs  |
 * +---------+----------------------+------+-------+
 */
public class RecipeDatabase extends SQLiteOpenHelper
        implements Database {

    private boolean initialCreate = true;
    private static RecipeDatabase sInstance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipeDatabase";
    private static final String TAG = "DATABASE";

    // table names
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_INGREDIENTS = "ingredients";

    // recipe table columns
    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_INSTRUCTIONS = "instructions";
    private static final String KEY_RECIPE_IMGURL = "img_url";
    private static final String KEY_RECIPE_FAV = "favorite";

    // ingredients table columns
    private static final String KEY_RECIPE_ING_ID_FK = "recipeId";
    private static final String KEY_INGREDIENTS_ID = "id";
    private static final String KEY_INGREDIENTS_QUANTITY = "quantity";
    private static final String KEY_INGREDIENTS_NAME = "name";
    private static final String KEY_INGREDIENTS_UNIT = "unit";

    public static synchronized RecipeDatabase getInstance() {
        if (sInstance == null) {
            sInstance = new RecipeDatabase();
            sInstance.prepopulateDatabase();
        } return sInstance;
    }

    private RecipeDatabase() {
        super(BrowseActivity.browseContext, DATABASE_NAME, null, DATABASE_VERSION); // assuming the context is always from browseActivity
    }

    RecipeDatabase(Context context) {
        super(BrowseActivity.browseContext, DATABASE_NAME, null, DATABASE_VERSION);
        if (initialCreate) { // TODO: FIGURE OUT HOW TO PREPOPULATE ONLY ONCE - key for each recipe object?
            prepopulateDatabase();
            initialCreate = false;
        }
    }

    /**
     * When the database connection is being configured,
     * enable foreign key.
     * @param db A SQLiteDatabase
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * Creates the recipe and ingredients tables
     * and prepopulates the database.
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + " ("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY," + KEY_RECIPE_NAME + " TEXT,"
                + KEY_RECIPE_INSTRUCTIONS + " TEXT,"
                + KEY_RECIPE_IMGURL + " TEXT,"
                + KEY_RECIPE_FAV + " TEXT" + ")";

        String CREATE_INGRED_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                KEY_INGREDIENTS_ID + " INTEGER PRIMARY KEY," +
                KEY_RECIPE_ING_ID_FK + " INTEGER REFERENCES " + TABLE_RECIPES + "," +
                KEY_INGREDIENTS_QUANTITY + " TEXT," +
                KEY_INGREDIENTS_UNIT + " TEXT," +
                KEY_INGREDIENTS_NAME + " TEXT" + ")";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_INGRED_TABLE);
    }

    /**
     * Upgrade the database.
     * @param db SQLiteDatabase
     * @param oldVersion Old version number of the database
     * @param newVersion New version number of the database
     */
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

    /**
     * Given a new recipe, adds it to the database.
     * @param recipe Recipe object
     */
    public void addRecipe(Recipe recipe) {
        // open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        long recipeId;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RECIPE_NAME, recipe.getName()); // recipes name
            values.put(KEY_RECIPE_IMGURL, recipe.getImgURL()); // recipe image url
            values.put(KEY_RECIPE_FAV, Boolean.toString(recipe.isFavorite())); // favorited?

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

    /**
     * Given a list of ingredients and foreign id to a recipe in
     * the recipe database, the ingredients are added to the
     * ingredients database with the foreign key of a recipe.
     * @param ingredients List of ingredient objects.
     * @param recipeId Foreign id of the recipe database
     */
    private void addIngredients(List<Ingredient> ingredients, long recipeId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            for (Ingredient ingredient : ingredients) {
                ContentValues values = new ContentValues();
                values.put(KEY_RECIPE_ING_ID_FK, recipeId); // Add foreign key
                values.put(KEY_INGREDIENTS_QUANTITY, ingredient.getQuantity());
                values.put(KEY_INGREDIENTS_UNIT, ingredient.getUnitString());
                values.put(KEY_INGREDIENTS_NAME, ingredient.getName());

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

    /**
     * Returns the list of recipe objects in
     * the database.
     * @return List of recipe objects.
     */
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
                    LinkedList<String> recipeInstrArr = new LinkedList<>();
                    for (int i = 0; i < recipeInstrJson.length(); i++) {
                        recipeInstrArr.add(recipeInstrJson.getString(i));
                    }

                    String isFavStr = recipeCursor.getString(
                            recipeCursor.getColumnIndex(KEY_RECIPE_FAV));
                    boolean isFav = Boolean.parseBoolean(isFavStr);

                    Recipe recipe = new Recipe(
                            recipe_name, ingredients,
                            recipeInstrArr,
                            recipe_img_url);
                    recipes.add(recipe);
                    recipe.setFavorite(isFav);
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

    /**
     * Given a foreign key of a recipe, returns the
     * ingredients with that foreign key.
     * @param recipeId Foreign key of a recipe
     * @return List of ingredient objects of a recipe
     */
    private List<Ingredient> getIngredientsList(String recipeId) {
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
                            ingredCursor.getColumnIndex(KEY_INGREDIENTS_NAME));

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

    /**
     * @return Number of recipes in the database.
     */
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

    /**
     * Deletes all of the recipes (and ingredients) in
     * the database.
     */
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

    /**
     * Prepopulate the database with sample recipes.
     */
    private void prepopulateDatabase() {
        // TODO: TEMP RECIPES - POSSIBLY GRAB DATA FROM API; Array or list?
        ArrayList<Ingredient> pancakes_ing = new ArrayList<>();
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.NONE, "Banana"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        pancakes_ing.add(new Ingredient(1.0/8.0, Ingredient.Unit.TSP, "Baking Powder"));
        pancakes_ing.add(new Ingredient(1, Ingredient.Unit.PINCH, "Ground Cinnamon"));
        pancakes_ing.add(new Ingredient(2, Ingredient.Unit.TSP, "Butter"));

        LinkedList<String> pancakes_dir = new LinkedList<>();
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

        LinkedList<String> eggs_dir = new LinkedList<>();
        eggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs");
        String eggs_img = "http://toriavey.com/images/2014/06/How-to-Scramble-Eggs.jpg";
        Recipe eggs = new Recipe("Eggs", eggs_ing, eggs_dir, eggs_img);

        ArrayList<Ingredient> ceggs_ing = new ArrayList<>();
        ceggs_ing.add(new Ingredient(2, Ingredient.Unit.NONE, "Eggs"));
        ceggs_ing.add(new Ingredient(1, Ingredient.Unit.TSP, "Butter"));
        ceggs_ing.add(new Ingredient(1.5, Ingredient.Unit.CUP, "Cheese"));

        LinkedList<String> ceggs_dir = new LinkedList<>();
        ceggs_dir.add("Heat butter in a skillet over medium heat. " +
                "Scramble eggs. Add Cheese");
        String ceggs_img = "http://foodnetwork.sndimg.com/content/dam/images/food/fullset/2011/3/31/0/RE0902H_sunnys-perfect-scrambled-cheesy-eggs_s4x3.jpg";
        Recipe ceggs = new Recipe("Cheesy Eggs", ceggs_ing, ceggs_dir, ceggs_img);

        // Get singleton instance of database
        addRecipe(pancakes);
        addRecipe(eggs);
        addRecipe(ceggs);
    }
}

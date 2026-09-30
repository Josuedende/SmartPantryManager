package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "recipe_id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_RECIPE_STEPS = "steps";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "ri_id";
    public static final String COL_RI_RECIPE_ID = "ri_recipe_id";
    public static final String COL_RI_NAME = "ri_name";
    public static final String COL_RI_QUANTITY = "ri_quantity";
    public static final String COL_RI_UNIT = "ri_unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createPantry);

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)";
        db.execSQL(createRecipes);

        String createRecipeIngredients = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT)";
        db.execSQL(createRecipeIngredients);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

    // ============ PANTRY CRUD ============

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        values.put(COL_EXPIRY, item.getExpiryDate());
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COL_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                PantryItem item = new PantryItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)));
                item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)));
                item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY)));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        values.put(COL_EXPIRY, item.getExpiryDate());
        int rows = db.update(TABLE_PANTRY, values, COL_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ============ RECIPE QUERIES ============

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Recipe r = new Recipe();
                r.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)));
                r.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)));
                r.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS)));
                list.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COL_RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_RI_ID)));
                ri.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_RI_RECIPE_ID)));
                ri.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_NAME)));
                ri.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_RI_QUANTITY)));
                ri.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_RI_UNIT)));
                list.add(ri);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // ============ SEED DATA ============

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Scrambled Eggs", "1. Crack eggs into a bowl and whisk.\n2. Melt butter in a pan.\n3. Pour eggs in and stir until cooked.\n4. Season with salt and serve.",
                new String[]{"eggs|3|pieces", "butter|1|tbsp", "salt|1|pinch"});

        addRecipe(db, "Tomato Pasta", "1. Boil pasta until soft.\n2. Fry garlic in olive oil.\n3. Add chopped tomatoes and simmer.\n4. Mix with pasta and serve.",
                new String[]{"pasta|200|g", "tomatoes|3|pieces", "olive oil|1|tbsp", "garlic|2|cloves"});

        addRecipe(db, "Cheese Toastie", "1. Butter the bread.\n2. Place cheese between slices.\n3. Toast in a pan until golden.",
                new String[]{"bread|2|slices", "cheese|50|g", "butter|1|tbsp"});

        addRecipe(db, "Pancakes", "1. Mix flour, milk, egg and sugar.\n2. Pour batter into a hot pan.\n3. Flip when bubbles appear.\n4. Serve with syrup.",
                new String[]{"flour|1|cup", "milk|1|cup", "eggs|1|pieces", "sugar|1|tbsp"});

        addRecipe(db, "Fried Rice", "1. Cook rice and set aside.\n2. Scramble eggs in a pan.\n3. Add rice, soy sauce and chopped carrot.\n4. Stir fry for 5 minutes.",
                new String[]{"rice|1|cup", "eggs|2|pieces", "soy sauce|2|tbsp", "carrot|1|pieces"});

        addRecipe(db, "Cheese Omelette", "1. Whisk eggs with a splash of milk.\n2. Pour into a hot pan.\n3. Add cheese and fold.\n4. Season and serve.",
                new String[]{"eggs|3|pieces", "cheese|30|g", "milk|2|tbsp", "salt|1|pinch"});

        addRecipe(db, "Garlic Butter Pasta", "1. Boil pasta.\n2. Melt butter and fry garlic.\n3. Toss pasta in the garlic butter.\n4. Top with parmesan.",
                new String[]{"pasta|200|g", "butter|2|tbsp", "garlic|3|cloves", "parmesan|30|g"});

        addRecipe(db, "Grilled Cheese", "1. Butter bread.\n2. Add cheese.\n3. Grill until cheese melts.",
                new String[]{"bread|2|slices", "cheese|50|g", "butter|1|tbsp"});

        addRecipe(db, "Tomato Soup", "1. Fry onion and garlic.\n2. Add tomatoes and stock.\n3. Simmer for 20 minutes.\n4. Blend until smooth.",
                new String[]{"tomatoes|4|pieces", "onion|1|pieces", "garlic|2|cloves", "vegetable stock|1|cup"});

        addRecipe(db, "Avocado Toast", "1. Toast the bread.\n2. Mash avocado on top.\n3. Season with salt and pepper.",
                new String[]{"bread|1|slices", "avocado|1|pieces", "salt|1|pinch", "pepper|1|pinch"});

        addRecipe(db, "Chicken Stir-fry", "1. Cook rice.\n2. Fry chicken pieces.\n3. Add broccoli and soy sauce.\n4. Serve over rice.",
                new String[]{"chicken|200|g", "soy sauce|2|tbsp", "broccoli|1|cup", "rice|1|cup"});

        addRecipe(db, "Tuna Sandwich", "1. Mix tuna with mayonnaise and chopped onion.\n2. Spread on bread.\n3. Top with another slice.",
                new String[]{"bread|2|slices", "tuna|1|can", "mayonnaise|2|tbsp", "onion|1|pieces"});

        addRecipe(db, "Mac and Cheese", "1. Boil macaroni.\n2. Melt butter and add milk and cheese.\n3. Stir until sauce thickens.\n4. Mix with macaroni.",
                new String[]{"macaroni|200|g", "cheese|100|g", "milk|1|cup", "butter|2|tbsp"});

        addRecipe(db, "Fruit Salad", "1. Chop all fruit.\n2. Mix in a bowl.\n3. Drizzle with honey.",
                new String[]{"apple|1|pieces", "banana|1|pieces", "orange|1|pieces", "honey|1|tbsp"});

        addRecipe(db, "Veggie Wrap", "1. Warm the tortilla.\n2. Add lettuce, tomato and cucumber.\n3. Roll up and serve.",
                new String[]{"tortilla|1|pieces", "lettuce|1|cup", "tomatoes|1|pieces", "cucumber|1|pieces"});

        addRecipe(db, "Quesadilla", "1. Place cheese and chicken on a tortilla.\n2. Fold and grill until golden.\n3. Serve with salsa.",
                new String[]{"tortilla|1|pieces", "cheese|50|g", "chicken|100|g", "salsa|2|tbsp"});

        addRecipe(db, "Oatmeal", "1. Boil milk.\n2. Add oats and stir.\n3. Top with banana and honey.",
                new String[]{"oats|1|cup", "milk|1|cup", "honey|1|tbsp", "banana|1|pieces"});

        addRecipe(db, "Banana Smoothie", "1. Add all ingredients to a blender.\n2. Blend until smooth.\n3. Serve cold.",
                new String[]{"banana|1|pieces", "milk|1|cup", "yogurt|1|cup", "honey|1|tbsp"});
    }

    private void addRecipe(SQLiteDatabase db, String name, String steps, String[] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (String ingredient : ingredients) {
            String[] parts = ingredient.split("\\|");
            ContentValues riValues = new ContentValues();
            riValues.put(COL_RI_RECIPE_ID, recipeId);
            riValues.put(COL_RI_NAME, parts[0]);
            riValues.put(COL_RI_QUANTITY, Double.parseDouble(parts[1]));
            riValues.put(COL_RI_UNIT, parts[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, riValues);
        }
    }
}
package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {

    /**
     * Returns ONLY the recipes where every ingredient is present
     * in the pantry, in at least the required quantity.
     * This is the strict-matching rule.
     */
    public static List<Recipe> getStrictMatches(DatabaseHelper dbHelper) {
        List<Recipe> result = new ArrayList<>();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<PantryItem> pantry = dbHelper.getAllPantryItems();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> needed =
                    dbHelper.getIngredientsForRecipe(recipe.getId());

            boolean canMake = true;
            for (RecipeIngredient req : needed) {
                if (!pantryHasIngredient(pantry, req)) {
                    canMake = false;
                    break;
                }
            }

            if (canMake) {
                result.add(recipe);
            }
        }
        return result;
    }

    /**
     * Returns recipes that are missing exactly ONE ingredient.
     * Used for the optional "Almost There" bonus section.
     */
    public static List<Recipe> getAlmostThere(DatabaseHelper dbHelper) {
        List<Recipe> result = new ArrayList<>();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<PantryItem> pantry = dbHelper.getAllPantryItems();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> needed =
                    dbHelper.getIngredientsForRecipe(recipe.getId());

            int missingCount = 0;
            for (RecipeIngredient req : needed) {
                if (!pantryHasIngredient(pantry, req)) {
                    missingCount++;
                }
            }

            if (missingCount == 1) {
                result.add(recipe);
            }
        }
        return result;
    }

    /**
     * Checks whether the pantry contains the required ingredient
     * with at least the required quantity.
     * Handles case differences, plural/singular, and unit differences.
     */
    private static boolean pantryHasIngredient(List<PantryItem> pantry,
                                               RecipeIngredient req) {
        String wantedName = normalise(req.getName());
        String wantedUnit = normaliseUnit(req.getUnit());
        double wantedQty = req.getQuantity();

        for (PantryItem item : pantry) {
            String haveName = normalise(item.getName());
            if (!haveName.equals(wantedName)) continue;

            // If units match (or either is empty), compare quantities directly.
            String haveUnit = normaliseUnit(item.getUnit());

            if (haveUnit.equals(wantedUnit) || haveUnit.isEmpty() || wantedUnit.isEmpty()) {
                if (item.getQuantity() >= wantedQty) return true;
            } else {
                // Units differ — do a simple conversion for common cases.
                double converted = convertUnit(item.getQuantity(), haveUnit, wantedUnit);
                if (converted >= wantedQty) return true;
            }
        }
        return false;
    }

    // ---------- Normalisation ----------

    private static String normalise(String s) {
        if (s == null) return "";
        String lower = s.toLowerCase().trim();
        // Singularise simple plurals: tomatoes -> tomato, eggs -> egg
        if (lower.endsWith("oes")) lower = lower.substring(0, lower.length() - 2);
        else if (lower.endsWith("ies")) lower = lower.substring(0, lower.length() - 3) + "y";
        else if (lower.endsWith("es") && lower.length() > 2) lower = lower.substring(0, lower.length() - 2);
        else if (lower.endsWith("s") && !lower.endsWith("ss") && lower.length() > 1)
            lower = lower.substring(0, lower.length() - 1);
        return lower;
    }

    private static String normaliseUnit(String unit) {
        if (unit == null) return "";
        String u = unit.toLowerCase().trim();
        // Treat these as equivalent
        if (u.equals("piece") || u.equals("pieces") || u.equals("pc") || u.equals("pcs")) return "piece";
        if (u.equals("gram") || u.equals("grams") || u.equals("g")) return "g";
        if (u.equals("kilogram") || u.equals("kilograms") || u.equals("kg")) return "kg";
        if (u.equals("millilitre") || u.equals("millilitres") || u.equals("ml")) return "ml";
        if (u.equals("litre") || u.equals("litres") || u.equals("l")) return "l";
        if (u.equals("tablespoon") || u.equals("tablespoons") || u.equals("tbsp")) return "tbsp";
        if (u.equals("teaspoon") || u.equals("teaspoons") || u.equals("tsp")) return "tsp";
        if (u.equals("cup") || u.equals("cups")) return "cup";
        if (u.equals("slice") || u.equals("slices")) return "slice";
        if (u.equals("clove") || u.equals("cloves")) return "clove";
        return u;
    }

    /**
     * Very simple conversion for common units.
     * If we can't convert, we just return the raw quantity.
     */
    private static double convertUnit(double qty, String from, String to) {
        // Weight: kg <-> g
        if (from.equals("kg") && to.equals("g")) return qty * 1000;
        if (from.equals("g") && to.equals("kg")) return qty / 1000;

        // Volume: l <-> ml
        if (from.equals("l") && to.equals("ml")) return qty * 1000;
        if (from.equals("ml") && to.equals("l")) return qty / 1000;

        // Volume: cup <-> ml (approx)
        if (from.equals("cup") && to.equals("ml")) return qty * 240;
        if (from.equals("ml") && to.equals("cup")) return qty / 240;

        // Spoons
        if (from.equals("tbsp") && to.equals("tsp")) return qty * 3;
        if (from.equals("tsp") && to.equals("tbsp")) return qty / 3;

        // Can't convert meaningfully — return as-is
        return qty;
    }
}
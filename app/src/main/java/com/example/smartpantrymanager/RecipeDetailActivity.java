package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId == -1) { finish(); return; }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Recipe selected = null;
        for (Recipe r : dbHelper.getAllRecipes()) {
            if (r.getId() == recipeId) { selected = r; break; }
        }
        if (selected == null) { finish(); return; }

        TextView textName = findViewById(R.id.textDetailRecipeName);
        LinearLayout containerIngredients = findViewById(R.id.containerIngredients);
        TextView textSteps = findViewById(R.id.textDetailSteps);

        textName.setText(selected.getName());
        textSteps.setText(selected.getSteps());

        List<RecipeIngredient> ingredients =
                dbHelper.getIngredientsForRecipe(recipeId);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (RecipeIngredient ri : ingredients) {
            View row = inflater.inflate(R.layout.item_recipe_ingredient,
                    containerIngredients, false);
            TextView line = row.findViewById(R.id.textIngredientLine);
            String qty = (ri.getQuantity() == Math.floor(ri.getQuantity()))
                    ? String.valueOf((int) ri.getQuantity())
                    : String.valueOf(ri.getQuantity());
            line.setText("• " + ri.getName() + " — " + qty + " " + ri.getUnit());
            containerIngredients.addView(row);
        }
    }
}
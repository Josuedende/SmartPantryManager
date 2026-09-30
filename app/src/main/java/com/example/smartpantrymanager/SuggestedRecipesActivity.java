package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerRecipes;
    private TextView textNoMatches;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        textNoMatches = findViewById(R.id.textNoMatches);
        dbHelper = new DatabaseHelper(this);

        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<Recipe> matches = RecipeMatcher.getStrictMatches(dbHelper);

        RecipeAdapter adapter = new RecipeAdapter(matches, recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this,
                    RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
        recyclerRecipes.setAdapter(adapter);

        if (matches.isEmpty()) {
            textNoMatches.setVisibility(View.VISIBLE);
            recyclerRecipes.setVisibility(View.GONE);
        } else {
            textNoMatches.setVisibility(View.GONE);
            recyclerRecipes.setVisibility(View.VISIBLE);
        }
    }
}
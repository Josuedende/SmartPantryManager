package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private TextView textEmpty;
    private FloatingActionButton fabAdd;
    private Button buttonViewSuggestions;
    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        textEmpty = findViewById(R.id.textEmpty);
        fabAdd = findViewById(R.id.fabAdd);
        buttonViewSuggestions = findViewById(R.id.buttonViewSuggestions);

        dbHelper = new DatabaseHelper(this);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> startActivity(
                new Intent(PantryListActivity.this, AddEditIngredientActivity.class)));

        buttonViewSuggestions.setOnClickListener(v -> startActivity(
                new Intent(PantryListActivity.this, SuggestedRecipesActivity.class)));

        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_pantry);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                return true;
            } else if (id == R.id.nav_recipes) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }

    private void loadPantry() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter = new PantryAdapter(items, item -> {
            Intent intent = new Intent(PantryListActivity.this,
                    AddEditIngredientActivity.class);
            intent.putExtra(AddEditIngredientActivity.EXTRA_ID, item.getId());
            intent.putExtra(AddEditIngredientActivity.EXTRA_NAME, item.getName());
            intent.putExtra(AddEditIngredientActivity.EXTRA_QUANTITY, item.getQuantity());
            intent.putExtra(AddEditIngredientActivity.EXTRA_UNIT, item.getUnit());
            intent.putExtra(AddEditIngredientActivity.EXTRA_EXPIRY, item.getExpiryDate());
            startActivity(intent);
        });
        recyclerPantry.setAdapter(adapter);

        if (items.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }
}
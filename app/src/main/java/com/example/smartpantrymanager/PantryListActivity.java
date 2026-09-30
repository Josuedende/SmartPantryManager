package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    private RecyclerView recyclerPantry;
    private TextView textEmpty;
    private FloatingActionButton fabAdd;
    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        textEmpty = findViewById(R.id.textEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        dbHelper = new DatabaseHelper(this);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this,
                    AddEditIngredientActivity.class);
            startActivity(intent);
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
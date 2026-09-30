package com.example.smartpantrymanager;

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
            // We'll wire this to the Add/Edit screen next
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
            // Tapping a row opens edit — wired up next step
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
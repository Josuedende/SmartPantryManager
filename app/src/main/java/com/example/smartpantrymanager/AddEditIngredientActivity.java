package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_QUANTITY = "extra_quantity";
    public static final String EXTRA_UNIT = "extra_unit";
    public static final String EXTRA_EXPIRY = "extra_expiry";

    private EditText editName, editQuantity, editUnit, editExpiry;
    private Button buttonSave, buttonDelete;
    private TextView textScreenTitle;
    private DatabaseHelper dbHelper;
    private long editingId = -1; // -1 = add mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        textScreenTitle = findViewById(R.id.textScreenTitle);

        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra(EXTRA_ID)) {
            editingId = getIntent().getLongExtra(EXTRA_ID, -1);
            String name = getIntent().getStringExtra(EXTRA_NAME);
            double quantity = getIntent().getDoubleExtra(EXTRA_QUANTITY, 0);
            String unit = getIntent().getStringExtra(EXTRA_UNIT);
            String expiry = getIntent().getStringExtra(EXTRA_EXPIRY);

            textScreenTitle.setText("Edit Ingredient");
            editName.setText(name);
            editQuantity.setText(String.valueOf(quantity));
            editUnit.setText(unit);
            editExpiry.setText(expiry == null ? "" : expiry);

            buttonDelete.setVisibility(Button.VISIBLE);
            buttonDelete.setOnClickListener(v -> confirmDelete());
        }

        buttonSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // ---- VALIDATION ----
        if (TextUtils.isEmpty(name)) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(quantityStr)) {
            editQuantity.setError("Quantity is required");
            editQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            editQuantity.setError("Must be a number");
            editQuantity.requestFocus();
            return;
        }
        if (quantity <= 0) {
            editQuantity.setError("Must be greater than 0");
            editQuantity.requestFocus();
            return;
        }
        if (!TextUtils.isEmpty(expiry) && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            editExpiry.setError("Use format yyyy-MM-dd");
            editExpiry.requestFocus();
            return;
        }

        PantryItem item = new PantryItem(name, quantity, unit,
                TextUtils.isEmpty(expiry) ? null : expiry);

        if (editingId == -1) {
            dbHelper.addPantryItem(item);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            item.setId(editingId);
            dbHelper.updatePantryItem(item);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Are you sure you want to remove this ingredient from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePantryItem(editingId);
                    Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
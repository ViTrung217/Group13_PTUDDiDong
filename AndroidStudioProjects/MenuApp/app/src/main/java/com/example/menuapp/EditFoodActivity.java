package com.example.menuapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menuapp.database.DatabaseHelper;

public class EditFoodActivity extends AppCompatActivity {
    EditText etEditFoodName, etEditFoodPrice, etEditFoodDescription;
    Button btnUpdateFood;
    DatabaseHelper dbHelper;
    int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        etEditFoodName = findViewById(R.id.etEditFoodName);
        etEditFoodPrice = findViewById(R.id.etEditFoodPrice);
        etEditFoodDescription = findViewById(R.id.etEditFoodDescription);
        btnUpdateFood = findViewById(R.id.btnUpdateFood);
        dbHelper = new DatabaseHelper(this);

        menuId = getIntent().getIntExtra("MENU_ID", -1);
        loadFoodData();

        btnUpdateFood.setOnClickListener(v -> {
            String name = etEditFoodName.getText().toString().trim();
            int price = Integer.parseInt(etEditFoodPrice.getText().toString().trim());
            String description = etEditFoodDescription.getText().toString().trim();

            if (dbHelper.updateMenu(menuId, name, price, description)) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditFoodActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFoodData() {
        Cursor cursor = dbHelper.getMenuById(menuId);
        if (cursor.moveToFirst()) {
            etEditFoodName.setText(cursor.getString(1));
            etEditFoodPrice.setText(String.valueOf(cursor.getInt(2)));
            etEditFoodDescription.setText(cursor.getString(3));
        }
    }
}

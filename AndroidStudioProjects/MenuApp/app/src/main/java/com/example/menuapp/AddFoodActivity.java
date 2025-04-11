package com.example.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menuapp.database.DatabaseHelper;

public class AddFoodActivity extends AppCompatActivity {
    EditText etFoodName, etFoodPrice, etFoodDescription;
    Button btnSaveFood;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        etFoodName = findViewById(R.id.etFoodName);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        etFoodDescription = findViewById(R.id.etFoodDescription);
        btnSaveFood = findViewById(R.id.btnSaveFood);
        dbHelper = new DatabaseHelper(this);

        btnSaveFood.setOnClickListener(v -> {
            String name = etFoodName.getText().toString().trim();
            int price = Integer.parseInt(etFoodPrice.getText().toString().trim());
            String description = etFoodDescription.getText().toString().trim();

            if (dbHelper.insertMenu(name, price, description)) {
                Toast.makeText(this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddFoodActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "Lỗi khi thêm món ăn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

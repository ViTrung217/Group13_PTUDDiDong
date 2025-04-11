package com.example.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menuapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    EditText etNewUsername, etNewPassword;
    Button btnRegisterSubmit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        dbHelper = new DatabaseHelper(this);

        btnRegisterSubmit.setOnClickListener(v -> {
            String username = etNewUsername.getText().toString().trim();
            String password = etNewPassword.getText().toString().trim();

            if (dbHelper.insertUser(username, password)) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            } else {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
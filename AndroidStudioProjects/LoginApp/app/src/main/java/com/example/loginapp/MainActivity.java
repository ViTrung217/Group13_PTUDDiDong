package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.equals("admin") && password.equals("123456")) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện chuyển sang màn hình đăng ký
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

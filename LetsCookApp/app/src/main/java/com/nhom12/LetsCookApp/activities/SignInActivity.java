package com.nhom12.LetsCookApp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.database.DatabaseHelper;

public class SignInActivity extends AppCompatActivity {
    TextView txt_signup;
    EditText edt_emaillogin, edt_passwordlogin;
    Button btn_login;

    DatabaseHelper dbHelper = new DatabaseHelper(SignInActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ giao diện
        txt_signup = findViewById(R.id.txt_signup);
        edt_emaillogin = findViewById(R.id.edt_emaillogin);
        edt_passwordlogin = findViewById(R.id.edt_passwordlogin);
        btn_login = findViewById(R.id.btn_login);

        // Chuyển sang màn hình đăng ký khi nhấn "Sign Up"
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(myintent);
                finish();
            }
        });

        // Xử lý sự kiện đăng nhập
        btn_login.setOnClickListener(v -> {
            String email = edt_emaillogin.getText().toString();
            String password = edt_passwordlogin.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignInActivity.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignInActivity.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra thông tin đăng nhập trong cơ sở dữ liệu
            Cursor c = dbHelper.getUserByEmailAndPassword(email, password);
            Intent myintent;
            if (c != null && c.moveToFirst()) {
                int userId = c.getInt(c.getColumnIndexOrThrow("id"));
                String role = c.getString(c.getColumnIndexOrThrow("role"));
                String username = c.getString(c.getColumnIndexOrThrow("username"));
                // Lưu user_id vào SharedPreferences
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", userId);
                editor.apply();
                // Điều hướng theo vai trò
                if ("Khách hàng".equals(role)) {
                    myintent = new Intent(SignInActivity.this, GuestActivity.class);
                    myintent.putExtra("username", username);
                    myintent.putExtra("email", email);
                    startActivity(myintent);
                } else if ("Quản trị viên".equals(role)) {
                    myintent = new Intent(SignInActivity.this, AdminActivity.class);
                    myintent.putExtra("username", username);
                    myintent.putExtra("email", email);
                    startActivity(myintent);
                }
                finish();
            } else {
                Toast.makeText(SignInActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
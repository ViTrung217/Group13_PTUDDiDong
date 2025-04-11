package com.nhom12.letscook;

import android.content.Intent;
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
        txt_signup = findViewById(R.id.txt_signup);
        edt_emaillogin = findViewById(R.id.edt_emaillogin);
        edt_passwordlogin = findViewById(R.id.edt_passwordlogin);
        btn_login = findViewById(R.id.btn_login);

        //Chuyển sang màn hình đăng ký
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(myintent);
                finish();
            }
        });

        //Click đăng nhập
        // Xử lý đăng nhập
        btn_login.setOnClickListener(v -> {
            String email = edt_emaillogin.getText().toString().trim();
            String password = edt_passwordlogin.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignInActivity.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignInActivity.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra thông tin đăng nhập trong SQLite
            Cursor cursor = dbHelper.getUserByEmailAndPassword(email, password);
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE));

                Intent intent;
                if ("Khách hàng".equals(role)) {
                    intent = new Intent(SignInActivity.this, MainActivity.class);

                } else if ("Quản trị viên".equals(role)) {
                    intent = new Intent(SignInActivity.this, AdminActivity.class);
                    // Truyền thông tin tài khoản sang AdminActivity
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                } else {
                    Toast.makeText(SignInActivity.this, "Vai trò không hợp lệ!", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignInActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) {
                cursor.close();
            }
        });

    }

}
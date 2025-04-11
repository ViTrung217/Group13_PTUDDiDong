package com.nhom12.LetsCookApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class SignUpActivity extends AppCompatActivity {
    AutoCompleteTextView auto_role;
    TextView txt_return;
    Button btn_signup;
    EditText edt_username, edt_email, edt_password;

    DatabaseHelper dbHelper = new DatabaseHelper(SignUpActivity.this);

    // Danh sách vai trò cho AutoCompleteTextView
    String roles[] = new String[] {"Quản trị viên", "Khách hàng"};
    ArrayAdapter<String> roleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ giao diện
        auto_role = findViewById(R.id.auto_role);
        txt_return = findViewById(R.id.txt_return);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_username = findViewById(R.id.edt_username);
        btn_signup = findViewById(R.id.btn_signup);

        // Thiết lập adapter cho vai trò
        roleAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_dropdown_item_1line, roles);
        auto_role.setAdapter(roleAdapter);

        // Tắt bàn phím và hiển thị danh sách khi nhấn vào AutoCompleteTextView
        auto_role.setInputType(0);
        auto_role.setFocusable(false);
        auto_role.setOnClickListener(v -> auto_role.showDropDown());

        // Quay lại màn hình đăng nhập
        txt_return.setOnClickListener(v -> {
            Intent myintent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(myintent);
            finish();
        });

        // Xử lý sự kiện đăng ký
        btn_signup.setOnClickListener(v -> {
            String email = edt_email.getText().toString();
            String password = edt_password.getText().toString();
            String role = auto_role.getText().toString();
            String username = edt_username.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(role) || TextUtils.isEmpty(username)) {
                Toast.makeText(SignUpActivity.this, "Hãy điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                boolean isRegistered = dbHelper.registerUser(username, email, password, role);
                if (isRegistered) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("SignUpActivity", "Error registering user", e);
                Toast.makeText(SignUpActivity.this, "Lỗi khi đăng ký. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
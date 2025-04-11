package com.nhom12.letscook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class SignUpActivity extends AppCompatActivity {
    AutoCompleteTextView auto_role;
    TextView txt_return;
    Button btn_signup;
    EditText edt_username, edt_email, edt_password;

    DatabaseHelper dbHelper = new DatabaseHelper(SignUpActivity.this);

    //Tạo danh sách các vai trò và gọi adapter để làm trung gian
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
        //Ánh xạ id
        auto_role = findViewById(R.id.auto_role);
        txt_return = findViewById(R.id.txt_return);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_username = findViewById(R.id.edt_username);
        btn_signup = findViewById(R.id.btn_signup);

        //Tạo ArrayAdapter với danh sách vai trò và áp dụng nó vào autocompletetextview
        roleAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_dropdown_item_1line, roles);
        auto_role.setAdapter(roleAdapter);

        //Lấy giá trị chọn từ autocompletextview
        String selectRole = auto_role.getText().toString();
        auto_role.setInputType(0); // Tắt bàn phím
        auto_role.setFocusable(false); // Không cho gõ, nhưng vẫn cho chọn
        auto_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_role.showDropDown(); // Mở danh sách khi click
            }
        });


        //Quay tro lại màn hình đăng nhập
        txt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(myintent);
                finish();
            }
        });

        //Click đăng ký tài khoản
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                String role = auto_role.getText().toString();
                String username = edt_username.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(role) || TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Hãy điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isRegistered = dbHelper.registerUser(username, email, password, role);
                if (isRegistered) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
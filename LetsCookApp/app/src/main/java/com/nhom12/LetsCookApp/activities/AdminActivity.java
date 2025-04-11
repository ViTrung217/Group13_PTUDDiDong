package com.nhom12.LetsCookApp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.adapters.UserAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseHelper dbHelper;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Thiết lập Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Thiết lập RecyclerView để hiển thị danh sách người dùng
        setupRecyclerView();

        // Thiết lập sự kiện cho các nút thêm, sửa, xóa
        setupButtonListeners();

        // Thiết lập Navigation Drawer
        setupNavigationDrawer();
    }

    // Thiết lập RecyclerView
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        loadUsers(); // Tải dữ liệu người dùng
    }

    // Tải danh sách người dùng từ cơ sở dữ liệu
    private void loadUsers() {
        userList.clear();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE));
                userList.add(new User(id, username, email, password, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        userAdapter = new UserAdapter(this, userList, this::showUserDialog);
        recyclerView.setAdapter(userAdapter);
    }

    // Hiển thị dialog chi tiết người dùng
    private void showUserDialog(User user) {
        selectedUser = user;
        new AlertDialog.Builder(this)
                .setTitle("Chi tiết người dùng")
                .setMessage("Tên người dùng: " + user.getUsername() + "\nEmail: " + user.getEmail() + "\nVai trò: " + user.getRole())
                .setPositiveButton("OK", null)
                .show();
    }

    // Thiết lập sự kiện cho các nút
    private void setupButtonListeners() {
        findViewById(R.id.btn_add).setOnClickListener(v -> showAddUserDialog());
        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            if (selectedUser != null) showEditUserDialog(selectedUser);
            else Toast.makeText(this, "Vui lòng chọn người dùng để sửa", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_delete).setOnClickListener(v -> {
            if (selectedUser != null) deleteUser(selectedUser);
            else Toast.makeText(this, "Vui lòng chọn người dùng để xóa", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_delete_all).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa tất cả")
                    .setMessage("Bạn có chắc chắn muốn xóa tất cả người dùng không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        dbHelper.deleteAllUsers();
                        loadUsers();
                        Toast.makeText(this, "Đã xóa tất cả người dùng", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    // Dialog thêm người dùng
    private void showAddUserDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_user_input, null);
        EditText etUsername = dialogView.findViewById(R.id.et_username);
        EditText etEmail = dialogView.findViewById(R.id.et_email);
        EditText etPassword = dialogView.findViewById(R.id.et_password);
        EditText etRole = dialogView.findViewById(R.id.et_role);

        new AlertDialog.Builder(this)
                .setTitle("Thêm người dùng mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String role = etRole.getText().toString();
                    if (dbHelper.registerUser(username, email, password, role)) {
                        loadUsers();
                        Toast.makeText(this, "Đã thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Dialog sửa thông tin người dùng
    private void showEditUserDialog(User user) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_user_input, null);
        EditText etUsername = dialogView.findViewById(R.id.et_username);
        EditText etEmail = dialogView.findViewById(R.id.et_email);
        EditText etPassword = dialogView.findViewById(R.id.et_password);
        EditText etRole = dialogView.findViewById(R.id.et_role);

        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
        etPassword.setText(user.getPassword());
        etRole.setText(user.getRole());

        new AlertDialog.Builder(this)
                .setTitle("Sửa thông tin người dùng")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String role = etRole.getText().toString();
                    dbHelper.getWritableDatabase().execSQL(
                            "UPDATE " + DatabaseHelper.TABLE_USERS + " SET " +
                                    DatabaseHelper.COL_USERNAME + "=?, " +
                                    DatabaseHelper.COL_EMAIL + "=?, " +
                                    DatabaseHelper.COL_PASSWORD + "=?, " +
                                    DatabaseHelper.COL_ROLE + "=? WHERE " +
                                    DatabaseHelper.COL_ID + "=?",
                            new Object[]{username, email, password, role, user.getId()}
                    );
                    loadUsers();
                    Toast.makeText(this, "Đã cập nhật người dùng thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xóa một người dùng
    private void deleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa " + user.getUsername() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    dbHelper.getWritableDatabase().delete(
                            DatabaseHelper.TABLE_USERS,
                            DatabaseHelper.COL_ID + "=?",
                            new String[]{String.valueOf(user.getId())}
                    );
                    loadUsers();
                    selectedUser = null;
                    Toast.makeText(this, "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    // Thiết lập Navigation Drawer
    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.txt_nav_username);
        TextView tvEmail = headerView.findViewById(R.id.txt_nav_email);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");

        if (username != null && email != null) {
            tvUsername.setText(username);
            tvEmail.setText(email);
        } else {
            tvUsername.setText("Admin");
            tvEmail.setText("admin@example.com");
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent newIntent;
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                newIntent = new Intent(this, SignInActivity.class);
                startActivity(newIntent);
                finish();
            } else if (id == R.id.nav_user_info) {
                Toast.makeText(this, "Đã làm mới danh sách người dùng", Toast.LENGTH_SHORT).show();
                loadUsers();
            } else if (id == R.id.nav_food_info) {
                newIntent = new Intent(this, AdminFoodActivity.class);
                newIntent.putExtra("username", tvUsername.getText().toString());
                newIntent.putExtra("email", tvEmail.getText().toString());
                startActivity(newIntent);
            } else if (id == R.id.nav_favorite_foods) {
                newIntent = new Intent(this, FavoriteFoodActivity.class);
                newIntent.putExtra("username", tvUsername.getText().toString());
                newIntent.putExtra("email", tvEmail.getText().toString());
                startActivity(newIntent);
            } else if (id == R.id.nav_feedback) {
                newIntent = new Intent(this, FeedbackManagementActivity.class);
                newIntent.putExtra("username", tvUsername.getText().toString());
                newIntent.putExtra("email", tvEmail.getText().toString());
                startActivity(newIntent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Mở Navigation Drawer khi nhấn nút Home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
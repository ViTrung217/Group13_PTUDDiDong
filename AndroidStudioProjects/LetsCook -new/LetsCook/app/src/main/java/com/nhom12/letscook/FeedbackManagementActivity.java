package com.nhom12.letscook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.nhom12.letscook.Model.Feedback;
import java.util.ArrayList;
import java.util.List;

public class FeedbackManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<Feedback> feedbackList;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_management);

        // Thiết lập Toolbar làm ActionBar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Thiết lập ActionBarDrawerToggle để liên kết Toolbar với Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thiết lập Navigation Drawer
        setupNavigationDrawer();

        // Thiết lập RecyclerView để hiển thị danh sách phản hồi
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_feedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(this, feedbackList);
        recyclerView.setAdapter(adapter);
        loadFeedback(); // Tải dữ liệu phản hồi từ database
    }

    // Thiết lập Navigation Drawer và xử lý sự kiện chọn menu
    private void setupNavigationDrawer() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tv_username);
        TextView tvEmail = headerView.findViewById(R.id.tv_email);

        // Lấy thông tin username và email từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");

        // Hiển thị thông tin người dùng lên header của Navigation Drawer
        if (username != null && email != null) {
            tvUsername.setText(username);
            tvEmail.setText(email);
        } else {
            tvUsername.setText("Admin");
            tvEmail.setText("admin@example.com");
        }

        // Xử lý sự kiện khi chọn các mục trong menu Navigation Drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent newIntent;
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                newIntent = new Intent(this, SignInActivity.class);
                startActivity(newIntent);
                finish(); // Đóng Activity hiện tại sau khi đăng xuất
            } else if (id == R.id.nav_user_info) {
                newIntent = new Intent(this, AdminActivity.class);
                newIntent.putExtra("username", tvUsername.getText().toString());
                newIntent.putExtra("email", tvEmail.getText().toString());
                startActivity(newIntent);
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
                Toast.makeText(this, "Đã làm mới danh sách feedback", Toast.LENGTH_SHORT).show();
                loadFeedback(); // Tải lại danh sách phản hồi
                adapter.notifyDataSetChanged(); // Cập nhật giao diện
            }
            drawerLayout.closeDrawer(GravityCompat.START); // Đóng Navigation Drawer
            return true;
        });
    }

    // Tải danh sách phản hồi từ database và cập nhật vào feedbackList
    private void loadFeedback() {
        feedbackList.clear(); // Xóa danh sách cũ để tránh trùng lặp
        Cursor cursor = dbHelper.getAllFeedback();
        if (cursor.moveToFirst()) {
            do {
                int feedbackId = cursor.getInt(cursor.getColumnIndexOrThrow("feedback_id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow("food_name"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                feedbackList.add(new Feedback(feedbackId, username, foodName, message, timestamp, status));
            } while (cursor.moveToNext());
        }
        cursor.close(); // Đóng Cursor để giải phóng tài nguyên
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView với dữ liệu mới
    }

    // Mở Navigation Drawer khi nhấn nút Home trên Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    // Xử lý sự kiện nhấn nút Back
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); // Đóng Drawer nếu đang mở
        } else {
            super.onBackPressed(); // Thoát Activity nếu Drawer đã đóng
        }
    }
}
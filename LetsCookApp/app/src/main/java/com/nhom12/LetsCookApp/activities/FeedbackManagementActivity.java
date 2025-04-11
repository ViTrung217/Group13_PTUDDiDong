package com.nhom12.LetsCookApp.activities;

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

import com.nhom12.LetsCookApp.adapters.FeedbackAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.Feedback;
import java.util.ArrayList;
import java.util.List;

import com.nhom12.LetsCookApp.R;

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

        // Thiết lập Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thiết lập Navigation Drawer
        setupNavigationDrawer();

        // Thiết lập RecyclerView
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_feedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(this, feedbackList);
        recyclerView.setAdapter(adapter);
        loadFeedback();
    }

    // Thiết lập Navigation Drawer
    private void setupNavigationDrawer() {
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
                loadFeedback();
                adapter.notifyDataSetChanged();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFeedback(); // Tải lại danh sách khi quay lại Activity
    }

    // Tải danh sách phản hồi từ cơ sở dữ liệu
    private void loadFeedback() {
        feedbackList.clear();
        Cursor cursor = dbHelper.getAllFeedback();
        if (cursor.moveToFirst()) {
            do {
                int feedbackId = cursor.getInt(cursor.getColumnIndexOrThrow("feedback_id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                int foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow("food_name"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                feedbackList.add(new Feedback(feedbackId, username, foodId, foodName, message, timestamp, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    // Mở Navigation Drawer khi nhấn nút Home
    @Override
    public boolean onSupportNavigateUp() {
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    // Xử lý nút Back
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
package com.nhom12.letscook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.nhom12.letscook.Model.FavoriteFood;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteFoodAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DatabaseHelper dbHelper;
    private List<FavoriteFood> favoriteFoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_food);

        // Thiết lập Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thiết lập Navigation Drawer
        setupNavigationDrawer();

        // Thiết lập RecyclerView để hiển thị danh sách món ăn yêu thích
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_favorite_foods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteFoodList = new ArrayList<>();
        adapter = new FavoriteFoodAdapter(this, favoriteFoodList);
        recyclerView.setAdapter(adapter);
        loadFavoriteFoods();
    }

    // Thiết lập Navigation Drawer
    private void setupNavigationDrawer() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tv_username);
        TextView tvEmail = headerView.findViewById(R.id.tv_email);

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
                Toast.makeText(this, "Đã làm mới danh sách yêu thích", Toast.LENGTH_SHORT).show();
                loadFavoriteFoods();
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

    // Tải danh sách món ăn yêu thích từ database
    private void loadFavoriteFoods() {
        favoriteFoodList.clear();
        Cursor cursor = null;
        try {
            cursor = dbHelper.getFavoriteFoodsRanking();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int foodId = cursor.getInt(cursor.getColumnIndexOrThrow("food_id"));
                    String foodName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    int favoriteCount = cursor.getInt(cursor.getColumnIndexOrThrow("favorite_count"));
                    favoriteFoodList.add(new FavoriteFood(foodId, foodName, favoriteCount));
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có món ăn nào trong danh sách yêu thích!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("FavoriteFoodActivity", "Lỗi khi tải danh sách món ăn yêu thích: " + e.getMessage());
            Toast.makeText(this, "Có lỗi xảy ra khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            adapter.notifyDataSetChanged();
        }
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
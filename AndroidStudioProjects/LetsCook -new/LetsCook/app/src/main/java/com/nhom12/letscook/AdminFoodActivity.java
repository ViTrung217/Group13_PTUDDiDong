package com.nhom12.letscook;

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
import com.nhom12.letscook.Model.Food;
import java.util.ArrayList;
import java.util.List;

public class AdminFoodActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private DatabaseHelper dbHelper;
    private Food selectedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food);

        // Thiết lập Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        dbHelper = new DatabaseHelper(this);

        // Thiết lập RecyclerView để hiển thị danh sách món ăn
        setupRecyclerView();

        // Thiết lập sự kiện cho các nút thêm, sửa, xóa
        setupButtonListeners();

        // Thiết lập Navigation Drawer
        setupNavigationDrawer();
    }

    // Thiết lập RecyclerView
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_foods);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodList = new ArrayList<>();
        loadFoods();
    }

    // Tải danh sách món ăn từ database
    private void loadFoods() {
        foodList.clear();
        Cursor cursor = dbHelper.getAllFoods();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
                int views = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIEWS));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENTS));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INSTRUCTIONS));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT));
                String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT));
                foodList.add(new Food(id, name, description, image, views, categoryId, ingredients, instructions, createdAt, updatedAt));
            } while (cursor.moveToNext());
        }
        cursor.close();
        foodAdapter = new FoodAdapter(this, foodList, this::showFoodDialog);
        recyclerView.setAdapter(foodAdapter);
    }

    // Hiển thị dialog chi tiết món ăn
    private void showFoodDialog(Food food) {
        selectedFood = food;
        String message = "Tên: " + food.getName() + "\n" +
                "Mô tả: " + food.getDescription() + "\n" +
                "Nguyên liệu: " + food.getIngredients() + "\n" +
                "Hướng dẫn: " + food.getInstructions() + "\n" +
                "Lượt xem: " + food.getViews() + "\n" +
                "Danh mục ID: " + food.getCategoryId();
        new AlertDialog.Builder(this)
                .setTitle("Chi tiết công thức")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Thiết lập sự kiện cho các nút
    private void setupButtonListeners() {
        findViewById(R.id.btn_add).setOnClickListener(v -> showAddFoodDialog());
        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            if (selectedFood != null) showEditFoodDialog(selectedFood);
            else Toast.makeText(this, "Vui lòng chọn công thức để sửa", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_delete).setOnClickListener(v -> {
            if (selectedFood != null) deleteFood(selectedFood);
            else Toast.makeText(this, "Vui lòng chọn công thức để xóa", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btn_delete_all).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa tất cả")
                    .setMessage("Bạn có chắc chắn muốn xóa tất cả công thức không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        dbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_FOODS, null, null);
                        loadFoods();
                        Toast.makeText(this, "Đã xóa tất cả công thức", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    // Dialog thêm món ăn
    private void showAddFoodDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_food_input, null);
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etIngredients = dialogView.findViewById(R.id.et_ingredients);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);

        new AlertDialog.Builder(this)
                .setTitle("Thêm công thức mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    String ingredients = etIngredients.getText().toString();
                    String instructions = etInstructions.getText().toString();
                    if (dbHelper.addFood(name, description, "", 1, ingredients, instructions)) {
                        loadFoods();
                        Toast.makeText(this, "Đã thêm công thức thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm công thức thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Dialog sửa món ăn
    private void showEditFoodDialog(Food food) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_food_input, null);
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etIngredients = dialogView.findViewById(R.id.et_ingredients);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);

        etName.setText(food.getName());
        etDescription.setText(food.getDescription());
        etIngredients.setText(food.getIngredients());
        etInstructions.setText(food.getInstructions());

        new AlertDialog.Builder(this)
                .setTitle("Sửa công thức")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    String ingredients = etIngredients.getText().toString();
                    String instructions = etInstructions.getText().toString();
                    dbHelper.getWritableDatabase().execSQL(
                            "UPDATE " + DatabaseHelper.TABLE_FOODS + " SET " +
                                    DatabaseHelper.COL_NAME + "=?, " +
                                    DatabaseHelper.COL_DESCRIPTION + "=?, " +
                                    DatabaseHelper.COL_INGREDIENTS + "=?, " +
                                    DatabaseHelper.COL_INSTRUCTIONS + "=?, " +
                                    DatabaseHelper.COL_UPDATED_AT + "=? WHERE " +
                                    DatabaseHelper.COL_ID + "=?",
                            new Object[]{name, description, ingredients, instructions, String.valueOf(System.currentTimeMillis()), food.getId()}
                    );
                    loadFoods();
                    Toast.makeText(this, "Đã cập nhật công thức", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xóa món ăn
    private void deleteFood(Food food) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa " + food.getName() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    dbHelper.getWritableDatabase().delete(
                            DatabaseHelper.TABLE_FOODS,
                            DatabaseHelper.COL_ID + "=?",
                            new String[]{String.valueOf(food.getId())}
                    );
                    loadFoods();
                    selectedFood = null;
                    Toast.makeText(this, "Đã xóa công thức", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    // Thiết lập Navigation Drawer
    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(this, "Đã làm mới danh sách công thức", Toast.LENGTH_SHORT).show();
                loadFoods();
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
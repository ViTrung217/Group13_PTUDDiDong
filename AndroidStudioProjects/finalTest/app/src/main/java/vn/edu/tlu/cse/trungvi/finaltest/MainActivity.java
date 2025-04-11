package vn.edu.tlu.cse.trungvi.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import vn.edu.tlu.cse.trungvi.finaltest.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DatabaseHelper dbHelper;
    private int currentUserId = 1; // Giả sử ID người dùng hiện tại là 1 (có thể lấy từ login)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Khởi tạo DrawerLayout và NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        dbHelper = new DatabaseHelper(this);

        // Thiết lập Toggle cho Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Cập nhật thông tin trong header
        updateNavHeader();

        // Xử lý sự kiện chọn item trong Navigation
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Mặc định hiển thị Fragment chính (nếu có)
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_profile);
            // Có thể thay bằng Fragment Profile nếu cần
        }
    }

    private void updateNavHeader() {
        User user = dbHelper.getUser(currentUserId);
        if (user != null) {
            TextView navUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
            TextView navEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_email);
            navUsername.setText(user.getUsername());
            navEmail.setText(user.getEmail());
        }
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Thông tin cá nhân", Toast.LENGTH_SHORT).show();
            // Chuyển sang Fragment Profile nếu cần
        } else if (id == R.id.nav_change_password) {
            Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
            // Chuyển sang Fragment ChangePassword hoặc mở dialog
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class); // Giả sử có LoginActivity
            startActivity(intent);
            finish(); // Đóng MainActivity
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
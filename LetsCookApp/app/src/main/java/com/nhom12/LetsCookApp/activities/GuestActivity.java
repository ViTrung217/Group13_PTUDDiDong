package com.nhom12.LetsCookApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.fragments.ChangePasswordFragment;
import com.nhom12.LetsCookApp.fragments.FavoritesFragment;
import com.nhom12.LetsCookApp.fragments.HistoryFragment;
import com.nhom12.LetsCookApp.fragments.HomeFragment;
import com.nhom12.LetsCookApp.fragments.MyFeedbackFragment;
import com.nhom12.LetsCookApp.fragments.ProfileFragment;
import com.nhom12.LetsCookApp.fragments.FeedbackFragment;

public class GuestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guest_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DrawerLayout drawerLayout = findViewById(R.id.guest_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Hiển thị thông tin người dùng trong header của Navigation Drawer
        View headerView = navigationView.getHeaderView(0);
        TextView txt_nav_username = headerView.findViewById(R.id.txt_nav_username);
        TextView txt_nav_email = headerView.findViewById(R.id.txt_nav_email);

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        txt_nav_username.setText(username);
        txt_nav_email.setText(email);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new HomeFragment()).commit();
            } else if (id == R.id.nav_personal_info) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new ProfileFragment()).commit();
            } else if (id == R.id.nav_history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new HistoryFragment()).commit();
            } else if (id == R.id.nav_favorite) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new FavoritesFragment()).commit();
            } else if (id == R.id.nav_my_feedback) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new MyFeedbackFragment()).commit();
            } else if (id == R.id.nav_feedback) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new FeedbackFragment()).commit();
            } else if (id == R.id.nav_change_password) {
                getSupportFragmentManager().beginTransaction().replace(R.id.guest_frame_layout,
                        new ChangePasswordFragment()).commit();
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(GuestActivity.this, SignInActivity.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Mặc định hiển thị HomeFragment khi khởi động
        navigationView.setCheckedItem(R.id.nav_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.guest_frame_layout, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
package com.example.menuapp;

import static java.lang.reflect.Array.get;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import com.example.menuapp.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    ListView listViewMenu;
    Button btnAddFood;
    DatabaseHelper dbHelper;
    ArrayList<String> foodList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMenu = findViewById(R.id.listViewMenu);
        btnAddFood = findViewById(R.id.btnAddFood);
        dbHelper = new DatabaseHelper(this);

        registerForContextMenu(listViewMenu); // ĐĂNG KÝ CONTEXT MENU

        loadMenu(); // Hiển thị thực đơn

        // Nếu nhân viên đăng nhập -> Hiện nút thêm món ăn
        boolean isEmployee = getIntent().getBooleanExtra("isEmployee", false);
        if (isEmployee) {
            btnAddFood.setVisibility(View.VISIBLE);
        }

        btnAddFood.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
        });
    }


    private void loadMenu() {
        Cursor cursor = dbHelper.getAllMenu();
        foodList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            int price = cursor.getInt(2);
            foodList.add(name + " - " + price + " VND");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodList);
        listViewMenu.setAdapter(adapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) return false; // Kiểm tra tránh lỗi NullPointerException

        int menuId = info.position; // Lấy vị trí món ăn trong ListView

        if (item.getItemId() == R.id.edit) { // Dùng if thay vì switch nếu lỗi vẫn còn
            Intent intent = new Intent(this, EditFoodActivity.class);
            intent.putExtra("MENU_ID", menuId);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.delete) {
            dbHelper.deleteMenu(menuId);
            loadMenu();
            Toast.makeText(this, "Đã xóa món ăn!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }




}

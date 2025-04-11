package com.nhom12.LetsCookApp.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.adapters.GuestFoodAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.Food;

import java.util.ArrayList;
import java.util.List;

public class DishListFragment extends Fragment {
    // Cập nhật tiêu đề ActionBar khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Danh sách món ăn");
    }

    RecyclerView recyclerView;
    GuestFoodAdapter adapter;
    List<Food> foodList;
    int categoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_dish_list
        View view = inflater.inflate(R.layout.fragment_dish_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_dish_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy categoryId từ arguments, nếu không có thì mặc định là -1 (hiển thị tất cả)
        categoryId = getArguments() != null ? getArguments().getInt("category_id", -1) : -1;

        // Khởi tạo danh sách món ăn
        foodList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (categoryId != -1) {
            // Lấy món ăn theo danh mục
            cursor = db.rawQuery(
                    "SELECT f.id, f.name, f.description, f.image, f.views, f.category_id, f.ingredients, " +
                            "f.instructions, c.name AS category_name " +
                            "FROM foods f " +
                            "JOIN categories c ON f.category_id = c.id " +
                            "WHERE f.category_id = ?",
                    new String[]{String.valueOf(categoryId)});
        } else {
            // Lấy tất cả món ăn
            cursor = db.rawQuery(
                    "SELECT f.id, f.name, f.description, f.image, f.views, f.category_id, f.ingredients, " +
                            "f.instructions, c.name AS category_name " +
                            "FROM foods f " +
                            "JOIN categories c ON f.category_id = c.id", null);
        }

        // Đọc dữ liệu từ Cursor và thêm vào danh sách
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String image = cursor.getString(3);
                int views = cursor.getInt(4);
                int cId = cursor.getInt(5);
                String ingredients = cursor.getString(6);
                String instructions = cursor.getString(7);
                String categoryName = cursor.getString(8);
                Food food = new Food(cId, categoryName, description, id, image, ingredients, instructions, name, views);
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Thiết lập Adapter cho RecyclerView
        adapter = new GuestFoodAdapter(getContext(), foodList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
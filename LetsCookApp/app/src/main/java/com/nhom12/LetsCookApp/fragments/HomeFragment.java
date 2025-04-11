package com.nhom12.LetsCookApp.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.adapters.GuestCategoryAdapter;
import com.nhom12.LetsCookApp.adapters.GuestFoodAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.Category;
import com.nhom12.LetsCookApp.models.Food;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseHelper dbHelper;

    // Cập nhật tiêu đề ActionBar khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Trang chủ thực đơn món ăn");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo RecyclerView cho danh mục
        RecyclerView recycler_category = view.findViewById(R.id.recycler_category);
        recycler_category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Danh sách hình ảnh mẫu cho danh mục
        int[] images = {R.drawable.main_dish, R.drawable.dessert, R.drawable.appetizer, R.drawable.snack, R.drawable.drink};

        // Lấy danh sách danh mục từ cơ sở dữ liệu
        List<Category> categoryList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, description FROM categories", null);

        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                int image = images[i % images.length];
                categoryList.add(new Category(description, id, image, name));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Thiết lập Adapter cho danh mục, xử lý sự kiện nhấn để chuyển sang DishListFragment
        GuestCategoryAdapter adapter = new GuestCategoryAdapter(getContext(), categoryList, category -> {
            DishListFragment fragment = new DishListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("category_id", category.getId());
            fragment.setArguments(bundle);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.guest_frame_layout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        recycler_category.setAdapter(adapter);

        // Khởi tạo RecyclerView cho danh sách món ăn phổ biến
        RecyclerView recycler_food = view.findViewById(R.id.recycler_food);
        recycler_food.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Lấy 5 món ăn có lượt xem cao nhất từ cơ sở dữ liệu
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase dbFood = new DatabaseHelper(getContext()).getReadableDatabase();
        Cursor cursorFood = dbFood.rawQuery(
                "SELECT f.id, f.name, f.description, f.image, f.views, f.category_id, f.ingredients, " +
                        "f.instructions, f.created_at, f.updated_at, c.name AS category_name " +
                        "FROM foods f " +
                        "JOIN categories c ON f.category_id = c.id " +
                        "ORDER BY f.views DESC LIMIT 5", null);

        if (cursorFood.moveToFirst()) {
            do {
                int id = cursorFood.getInt(0);
                String name = cursorFood.getString(1);
                String description = cursorFood.getString(2);
                String image = cursorFood.getString(3);
                int views = cursorFood.getInt(4);
                int categoryId = cursorFood.getInt(5);
                String ingredients = cursorFood.getString(6);
                String instructions = cursorFood.getString(7);
                String categoryName = cursorFood.getString(10);
                int likes = dbHelper.getLikesCount(id);

                Food food = new Food(categoryId, categoryName, description, id, image, ingredients, instructions, name, views);
                foodList.add(food);
            } while (cursorFood.moveToNext());
        }
        cursorFood.close();
        dbFood.close();

        // Thiết lập Adapter cho danh sách món ăn
        GuestFoodAdapter foodAdapter = new GuestFoodAdapter(getContext(), foodList);
        recycler_food.setAdapter(foodAdapter);

        // Xử lý sự kiện nhấn "Xem tất cả" để chuyển sang DishListFragment với toàn bộ món ăn
        TextView txt_food_seeall = view.findViewById(R.id.txt_food_seeall);
        txt_food_seeall.setOnClickListener(v -> {
            DishListFragment fragment = new DishListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", -1); // -1 để hiển thị tất cả món ăn
            fragment.setArguments(bundle);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.guest_frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
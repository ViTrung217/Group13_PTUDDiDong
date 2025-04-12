package com.nhom12.LetsCookApp.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.adapters.GuestFoodAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.Food;

public class FavoritesFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private GuestFoodAdapter guestFoodAdapter;

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Danh sách món ăn yêu thích");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_favourite
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tải danh sách món ăn yêu thích
        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        // Lấy user_id từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            List<Food> favoritesList = new ArrayList<>();
            Cursor cursor = dbHelper.getFavoritesByUserId(userId);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int foodId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FAV_FOOD_ID));
                    String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FAV_TIMESTAMP));
                    Food food = getFoodDetails(foodId);
                    if (food != null) {
                        food.setTimestamp(timestamp);
                        food.setFavourite(true); // Đánh dấu là yêu thích
                        favoritesList.add(food);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }

            // Thiết lập Adapter cho RecyclerView
            guestFoodAdapter = new GuestFoodAdapter(getContext(), favoritesList);
            recyclerView.setAdapter(guestFoodAdapter);
        }
    }

    private Food getFoodDetails(int foodId) {
        Cursor cursor = dbHelper.getAllFoods();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ID));
                if (id == foodId) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
                    int views = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIEWS));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION));
                    String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INGREDIENTS));
                    String instructions = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INSTRUCTIONS));
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID));
                    cursor.close();
                    return new Food(categoryId, null, description, id, image, ingredients, instructions, name, views);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return null;
    }
}
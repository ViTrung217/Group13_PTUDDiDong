package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.fragments.FoodDetailFragment;
import com.nhom12.LetsCookApp.models.Food;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GuestFoodAdapter extends RecyclerView.Adapter<GuestFoodAdapter.FoodViewHolder> {
    private static Context context;
    private final List<Food> foodList;
    private DatabaseHelper dbHelper;

    // Constructor khởi tạo adapter
    public GuestFoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_food_guest
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_guest, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        Food food = foodList.get(position);
        holder.setData(food);
        String imageName = food.getImage().replace(".jpg", "");
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.img_food.setImageResource(imageResId);
        }
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng món ăn
        return foodList.size();
    }

    // ViewHolder ánh xạ và xử lý dữ liệu giao diện
    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView img_food;
        TextView txt_food_name, txt_cate_food, txt_food_views, txt_food_like;
        FloatingActionButton btn_fav;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            img_food = itemView.findViewById(R.id.img_food);
            txt_food_name = itemView.findViewById(R.id.txt_food_name);
            txt_cate_food = itemView.findViewById(R.id.txt_cate_food);
            txt_food_views = itemView.findViewById(R.id.txt_food_views);
            txt_food_like = itemView.findViewById(R.id.txt_food_like);
            btn_fav = itemView.findViewById(R.id.btn_fav);
        }

        public void setData(Food food) {
            txt_food_name.setText(food.getName());
            txt_cate_food.setText(food.getCategoryName() != null && !food.getCategoryName().isEmpty() ? food.getCategoryName() : "");
            txt_food_views.setText(food.getViews() > 0 ? "👁 " + food.getViews() + " Lượt xem" :
                    (food.getTimestamp() != null && !food.getTimestamp().isEmpty() ? "Thời gian: " + food.getTimestamp() : ""));
            int likesCount = dbHelper.getLikesCount(food.getId());
            food.setLikes(likesCount);
            txt_food_like.setText("❤️ " + likesCount + " Lượt thích");

            // Cập nhật trạng thái nút yêu thích
            btn_fav.setImageResource(food.isFavourite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            btn_fav.setContentDescription(food.isFavourite() ? context.getString(R.string.favorite_remove_description) :
                    context.getString(R.string.favorite_add_description));

            // Xử lý sự kiện nhấn nút yêu thích
            btn_fav.setOnClickListener(v -> {
                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                if (userId == -1) {
                    Toast.makeText(context, "Vui lòng đăng nhập để yêu thích món ăn", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                if (food.isFavourite()) {
                    food.setFavourite(false);
                    btn_fav.setImageResource(R.drawable.ic_heart_outline);
                    btn_fav.setContentDescription(context.getString(R.string.favorite_add_description));
                    dbHelper.removeFavorite(userId, food.getId());
                    Toast.makeText(context, "Đã bỏ yêu thích: " + food.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    food.setFavourite(true);
                    btn_fav.setImageResource(R.drawable.ic_heart_filled);
                    btn_fav.setContentDescription(context.getString(R.string.favorite_remove_description));
                    dbHelper.addFavorite(userId, food.getId(), timestamp);
                    Toast.makeText(context, "Đã yêu thích: " + food.getName(), Toast.LENGTH_SHORT).show();
                }
                int updatedLikesCount = dbHelper.getLikesCount(food.getId());
                food.setLikes(updatedLikesCount);
                txt_food_like.setText("❤️ " + updatedLikesCount + " Lượt thích");
            });

            // Xử lý sự kiện nhấn vào món ăn để xem chi tiết
            itemView.setOnClickListener(v -> {
                txt_food_views.setText("👁 " + (food.getViews() + 1) + " Lượt xem");
                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                if (userId != -1) {
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    dbHelper.addViewHistory(userId, food.getId(), timestamp);
                }
                FoodDetailFragment fragment = new FoodDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", food.getName());
                bundle.putString("description", food.getDescription());
                bundle.putString("image", food.getImage());
                bundle.putString("ingredients", food.getIngredients());
                bundle.putString("instructions", food.getInstructions());
                bundle.putInt("views", food.getViews());
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.guest_frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}
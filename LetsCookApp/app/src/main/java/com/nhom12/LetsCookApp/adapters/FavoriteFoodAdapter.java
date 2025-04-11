package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.models.FavoriteFood;

public class FavoriteFoodAdapter extends RecyclerView.Adapter<FavoriteFoodAdapter.FavoriteFoodViewHolder> {
    private Context context;
    private List<FavoriteFood> favoriteFoodList;

    // Constructor khởi tạo adapter
    public FavoriteFoodAdapter(Context context, List<FavoriteFood> favoriteFoodList) {
        this.context = context;
        this.favoriteFoodList = favoriteFoodList;
    }

    @Override
    public FavoriteFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_favorite_food
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_food, parent, false);
        return new FavoriteFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteFoodViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        FavoriteFood favoriteFood = favoriteFoodList.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvFoodName.setText(favoriteFood.getFoodName());
        holder.tvFavoriteCount.setText(favoriteFood.getFavoriteCount() + " lượt thích");
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng món ăn yêu thích
        return favoriteFoodList.size();
    }
    // ViewHolder ánh xạ các thành phần giao diện
    static class FavoriteFoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvFoodName, tvFavoriteCount;
        FavoriteFoodViewHolder(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFavoriteCount = itemView.findViewById(R.id.tv_favorite_count);
        }
    }
}
package com.nhom12.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.nhom12.letscook.Model.FavoriteFood;

public class FavoriteFoodAdapter extends RecyclerView.Adapter<FavoriteFoodAdapter.FavoriteFoodViewHolder> {
    private Context context;
    private List<FavoriteFood> favoriteFoodList;

    // Constructor để khởi tạo adapter với context và danh sách món ăn yêu thích
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
        // Gắn dữ liệu cho từng item trong danh sách
        FavoriteFood favoriteFood = favoriteFoodList.get(position);
        holder.tvRank.setText(String.valueOf(position + 1)); // Hiển thị thứ hạng (bắt đầu từ 1)
        holder.tvFoodName.setText(favoriteFood.getFoodName()); // Hiển thị tên món ăn
        holder.tvFavoriteCount.setText(favoriteFood.getFavoriteCount() + " lượt thích"); // Hiển thị số lượt thích
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng món ăn trong danh sách
        return favoriteFoodList.size();
    }

    // ViewHolder để ánh xạ các thành phần giao diện trong item_favorite_food.xml
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
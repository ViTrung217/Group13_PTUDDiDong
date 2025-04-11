package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foodList;
    private final FoodClickListener listener;

    // Constructor khởi tạo adapter
    public FoodAdapter(Context context, List<Food> foodList, FoodClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_food_admin
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_admin, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvDescription.setText("Mô tả: " + food.getDescription());
        holder.tvIngredients.setText("Nguyên liệu: " + food.getIngredients());
        holder.tvInstructions.setText("Hướng dẫn: " + food.getInstructions());
        holder.tvViews.setText("Lượt xem: " + food.getViews());
        holder.tvCategoryId.setText("Danh mục ID: " + food.getCategoryId());

        // Gắn ảnh từ drawable
        String imageName = food.getImage().replace(".jpg", "");
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.ivImage.setImageResource(imageResId);
        }

        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng món ăn
        return foodList.size();
    }

    // ViewHolder ánh xạ các thành phần giao diện
    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvIngredients, tvInstructions, tvViews, tvCategoryId;
        ImageView ivImage;

        FoodViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvDescription = itemView.findViewById(R.id.tv_food_description);
            tvIngredients = itemView.findViewById(R.id.tv_food_ingredients);
            tvInstructions = itemView.findViewById(R.id.tv_food_instructions);
            tvViews = itemView.findViewById(R.id.tv_food_views);
            tvCategoryId = itemView.findViewById(R.id.tv_food_category_id);
            ivImage = itemView.findViewById(R.id.iv_food_image);
        }
    }

    // Interface xử lý sự kiện click
    public interface FoodClickListener {
        void onFoodClick(Food food);
    }
}
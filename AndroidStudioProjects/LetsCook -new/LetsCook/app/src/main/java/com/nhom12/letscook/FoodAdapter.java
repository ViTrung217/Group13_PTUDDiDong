package com.nhom12.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.nhom12.letscook.Model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foodList;
    private final FoodClickListener listener;

    // Constructor để khởi tạo adapter với context, danh sách món ăn và listener cho sự kiện click
    public FoodAdapter(Context context, List<Food> foodList, FoodClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_food
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item trong danh sách
        Food food = foodList.get(position);
        holder.tvName.setText(food.getName());
        holder.tvDescription.setText("Mô tả: " + food.getDescription());
        holder.tvIngredients.setText("Nguyên liệu: " + food.getIngredients());
        holder.tvInstructions.setText("Hướng dẫn: " + food.getInstructions());
        holder.tvViews.setText("Lượt xem: " + food.getViews());
        holder.tvCategoryId.setText("Danh mục ID: " + food.getCategoryId());

        // Ánh xạ tên ảnh sang tài nguyên drawable
        String imageName = food.getImage().replace(".jpg", "");
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.ivImage.setImageResource(imageResId); // Gán ảnh nếu tìm thấy
        }

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> listener.onFoodClick(food));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng món ăn trong danh sách
        return foodList.size();
    }

    // ViewHolder để ánh xạ các thành phần giao diện trong item_food.xml
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

    // Interface để xử lý sự kiện click vào món ăn
    public interface FoodClickListener {
        void onFoodClick(Food food);
    }
}
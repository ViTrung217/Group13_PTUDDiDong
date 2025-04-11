package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.models.Category;

import java.util.List;

public class GuestCategoryAdapter extends RecyclerView.Adapter<GuestCategoryAdapter.CategoryViewHolder> {
    Context context;
    List<Category> categories;
    OnItemClickListener listener;

    // Interface xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    // Constructor khởi tạo adapter
    public GuestCategoryAdapter(Context context, List<Category> categories, OnItemClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_category_guest
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_guest, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        Category category = categories.get(position);
        holder.txt_cate_name.setText(category.getName());
        holder.txt_cate_description.setText(category.getDescription());
        holder.image.setImageResource(category.getImage());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng danh mục
        return categories.size();
    }

    // ViewHolder ánh xạ các thành phần giao diện
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txt_cate_name, txt_cate_description;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_cate);
            txt_cate_name = itemView.findViewById(R.id.txt_cate_name);
            txt_cate_description = itemView.findViewById(R.id.txt_cate_description);
        }
    }
}
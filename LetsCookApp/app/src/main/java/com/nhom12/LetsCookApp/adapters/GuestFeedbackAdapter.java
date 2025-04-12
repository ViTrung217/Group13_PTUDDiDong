package com.nhom12.LetsCookApp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.models.FeedbackItem;

public class GuestFeedbackAdapter extends RecyclerView.Adapter<GuestFeedbackAdapter.FeedbackViewHolder> {
    private List<FeedbackItem> feedbackList;
    private OnFeedbackActionListener actionListener;

    public interface OnFeedbackActionListener {
        void onEditFeedback(int position, FeedbackItem item);
        void onDeleteFeedback(int position, FeedbackItem item);
    }

    // Constructor khởi tạo adapter
    public GuestFeedbackAdapter(List<FeedbackItem> feedbackList, OnFeedbackActionListener listener) {
        this.feedbackList = feedbackList;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_feedback
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        FeedbackItem item = feedbackList.get(position);
        holder.textFoodName.setText("Món ăn: " + item.getFoodName());
        holder.textMessage.setText("Nội dung: " + item.getMessage());
        holder.textTimestamp.setText("Thời gian: " + item.getTimestamp());

        holder.buttonEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditFeedback(position, item);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteFeedback(position, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng phản hồi
        return feedbackList.size();
    }

    // ViewHolder ánh xạ các thành phần giao diện
    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textMessage, textTimestamp;
        Button buttonEdit, buttonDelete;

        FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            textMessage = itemView.findViewById(R.id.text_message);
            textTimestamp = itemView.findViewById(R.id.text_timestamp);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }

    // Cập nhật danh sách phản hồi sau khi sửa/xóa
    public void updateFeedbackList(List<FeedbackItem> newList) {
        this.feedbackList = newList;
        notifyDataSetChanged();
    }
}
package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.Feedback;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private Context context;
    private List<Feedback> feedbackList;
    private DatabaseHelper dbHelper;

    // Constructor khởi tạo adapter
    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_feedback_admin
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback_admin, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        Feedback feedback = feedbackList.get(position);
        holder.tvUsername.setText("Người dùng: " + (feedback.getUsername() != null ? feedback.getUsername() : "N/A"));
        holder.tvFoodName.setText("Món ăn: " + (feedback.getFoodName() != null ? feedback.getFoodName() : "N/A"));
        holder.tvMessage.setText("Phản hồi: " + (feedback.getMessage() != null ? feedback.getMessage() : "N/A"));
        holder.tvStatus.setText("Trạng thái: " + (feedback.getStatus() == 0 ? "Chưa xem" : "Đã xem"));

        // Xử lý nút đánh dấu đã xem
        holder.btnMarkViewed.setOnClickListener(v -> {
            dbHelper.updateFeedbackStatus(feedback.getFeedbackId(), 1);
            feedback.setStatus(1);
            notifyItemChanged(position);
        });

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteFeedback(feedback.getFeedbackId());
            feedbackList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, feedbackList.size());
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng phản hồi
        return feedbackList.size();
    }

    // ViewHolder ánh xạ các thành phần giao diện
    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvFoodName, tvMessage, tvStatus;
        Button btnMarkViewed, btnDelete;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnMarkViewed = itemView.findViewById(R.id.btn_mark_viewed);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
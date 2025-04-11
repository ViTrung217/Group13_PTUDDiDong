package com.nhom12.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.nhom12.letscook.Model.Feedback;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private Context context;
    private List<Feedback> feedbackList;
    private DatabaseHelper dbHelper;

    // Constructor để khởi tạo adapter với context và danh sách phản hồi
    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_feedback
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item trong danh sách
        Feedback feedback = feedbackList.get(position);
        holder.tvUsername.setText("Người dùng: " + feedback.getUsername());
        holder.tvFoodName.setText("Món ăn: " + feedback.getFoodName());
        holder.tvMessage.setText("Phản hồi: " + feedback.getMessage());
        holder.tvStatus.setText("Trạng thái: " + (feedback.getStatus() == 0 ? "Chưa xem" : "Đã xem"));

        // Xử lý nút đánh dấu đã xem
        holder.btnMarkViewed.setOnClickListener(v -> {
            dbHelper.updateFeedbackStatus(feedback.getFeedbackId(), 1);
            feedback.setStatus(1);
            notifyItemChanged(position);
        });

        // Xử lý nút xóa phản hồi
        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteFeedback(feedback.getFeedbackId());
            feedbackList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, feedbackList.size());
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng phản hồi trong danh sách
        return feedbackList.size();
    }

    // ViewHolder để ánh xạ các thành phần giao diện trong item_feedback.xml
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
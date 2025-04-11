package com.nhom12.LetsCookApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private final UserClickListener listener;

    // Constructor khởi tạo adapter
    public UserAdapter(Context context, List<User> userList, UserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_user
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item
        User user = userList.get(position);
        holder.tvUsername.setText("Tên: " + user.getUsername());
        holder.tvEmail.setText("Email: " + user.getEmail());
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng người dùng
        return userList.size();
    }

    // ViewHolder ánh xạ các thành phần giao diện
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail;

        UserViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEmail = itemView.findViewById(R.id.tv_email);
        }
    }

    // Interface xử lý sự kiện click
    public interface UserClickListener {
        void onUserClick(User user);
    }
}
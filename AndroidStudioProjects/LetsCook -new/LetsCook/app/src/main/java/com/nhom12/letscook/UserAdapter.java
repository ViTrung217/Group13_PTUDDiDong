package com.nhom12.letscook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.nhom12.letscook.Model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private final UserClickListener listener;

    // Constructor để khởi tạo adapter với context, danh sách người dùng và listener
    public UserAdapter(Context context, List<User> userList, UserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item từ layout item_user (giả định)
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        // Gắn dữ liệu cho từng item trong danh sách
        User user = userList.get(position);
        holder.tvUsername.setText("Tên: " + user.getUsername());
        holder.tvEmail.setText("Email: " + user.getEmail());


        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng người dùng trong danh sách
        return userList.size();
    }

    // ViewHolder để ánh xạ các thành phần giao diện trong item_user.xml
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail, tvRole;

        UserViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEmail = itemView.findViewById(R.id.tv_email);

        }
    }

    // Interface để xử lý sự kiện click vào người dùng
    public interface UserClickListener {
        void onUserClick(User user);
    }
}
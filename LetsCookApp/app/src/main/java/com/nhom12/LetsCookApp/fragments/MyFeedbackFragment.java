package com.nhom12.LetsCookApp.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.adapters.GuestFeedbackAdapter;
import com.nhom12.LetsCookApp.database.DatabaseHelper;
import com.nhom12.LetsCookApp.models.FeedbackItem;

public class MyFeedbackFragment extends Fragment implements GuestFeedbackAdapter.OnFeedbackActionListener {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private GuestFeedbackAdapter guestFeedbackAdapter;
    private List<FeedbackItem> feedbackList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_my_feedback
        View view = inflater.inflate(R.layout.fragment_my_feedback, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_feedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tải danh sách phản hồi
        loadMyFeedback();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Phản hồi của tôi");
    }
    private void loadMyFeedback() {
        // Lấy user_id từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            feedbackList = new ArrayList<>();
            Cursor cursor = dbHelper.getFeedbackByUserId(userId);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int feedbackId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FEEDBACK_ID));
                    int foodId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ID_FK));
                    String message = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MESSAGE));
                    String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMESTAMP));
                    String foodName = getFoodName(foodId);
                    feedbackList.add(new FeedbackItem(feedbackId, foodName, message, timestamp));
                } while (cursor.moveToNext());
                cursor.close();
            }

            // Thiết lập Adapter cho RecyclerView
            guestFeedbackAdapter = new GuestFeedbackAdapter(feedbackList, this);
            recyclerView.setAdapter(guestFeedbackAdapter);
        }
    }

    // Lấy tên món ăn từ bảng foods dựa trên foodId
    private String getFoodName(int foodId) {
        Cursor cursor = dbHelper.getAllFoods();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ID));
                if (id == foodId) {
                    String foodName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                    cursor.close();
                    return foodName;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return "Không xác định";
    }

    // Xử lý sự kiện sửa phản hồi
    @Override
    public void onEditFeedback(int position, FeedbackItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sửa phản hồi");
        final EditText input = new EditText(getContext());
        input.setText(item.getMessage());
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newMessage = input.getText().toString();
            if (!newMessage.isEmpty()) {
                if (dbHelper.updateFeedback(item.getFeedbackId(), newMessage)) {
                    feedbackList.get(position).setMessage(newMessage);
                    guestFeedbackAdapter.notifyItemChanged(position);
                    Toast.makeText(getContext(), "Cập nhật phản hồi thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Xử lý sự kiện xóa phản hồi
    @Override
    public void onDeleteFeedback(int position, FeedbackItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa phản hồi")
                .setMessage("Bạn có chắc muốn xóa phản hồi này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (dbHelper.deleteFeedback(item.getFeedbackId())) {
                        feedbackList.remove(position);
                        guestFeedbackAdapter.notifyItemRemoved(position);
                        guestFeedbackAdapter.notifyItemRangeChanged(position, feedbackList.size());
                        Toast.makeText(getContext(), "Xóa phản hồi thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
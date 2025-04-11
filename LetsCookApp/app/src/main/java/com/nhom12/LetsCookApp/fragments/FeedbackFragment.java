package com.nhom12.LetsCookApp.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.database.DatabaseHelper;

public class FeedbackFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private Spinner spinnerFoods;
    private EditText editMessage;
    private Button buttonSubmit;
    private ArrayList<String> foodNames;
    private ArrayList<Integer> foodIds;
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Gửi phản hồi món ăn");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_feedback
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        dbHelper = new DatabaseHelper(getContext());
        spinnerFoods = view.findViewById(R.id.spinner_foods);
        editMessage = view.findViewById(R.id.edit_message);
        buttonSubmit = view.findViewById(R.id.button_submit);

        // Load danh sách món ăn vào Spinner
        loadFoodsIntoSpinner();

        // Xử lý sự kiện nhấn nút gửi phản hồi
        buttonSubmit.setOnClickListener(v -> submitFeedback());

        return view;
    }

    private void loadFoodsIntoSpinner() {
        foodNames = new ArrayList<>();
        foodIds = new ArrayList<>();
        Cursor cursor = dbHelper.getAllFoods();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int foodId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FOOD_ID));
                String foodName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                foodIds.add(foodId);
                foodNames.add(foodName);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Thiết lập Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, foodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoods.setAdapter(adapter);

        if (foodNames.isEmpty()) {
            Toast.makeText(getContext(), "Không có món ăn nào để chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitFeedback() {
        String message = editMessage.getText().toString();

        // Kiểm tra dữ liệu đầu vào
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập nội dung phản hồi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (foodIds.isEmpty()) {
            Toast.makeText(getContext(), "Không có món ăn nào để gửi phản hồi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy user_id từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi phản hồi vào cơ sở dữ liệu
        int selectedPosition = spinnerFoods.getSelectedItemPosition();
        int foodId = foodIds.get(selectedPosition);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (dbHelper.addFeedback(userId, foodId, message, timestamp)) {
            Toast.makeText(getContext(), "Gửi phản hồi thành công", Toast.LENGTH_SHORT).show();
            editMessage.setText("");
        } else {
            Toast.makeText(getContext(), "Gửi phản hồi thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Chuyển sang MyFeedbackFragment để xem phản hồi của người dùng
    private void viewMyFeedback() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.guest_frame_layout, new MyFeedbackFragment())
                .addToBackStack(null)
                .commit();
    }
}
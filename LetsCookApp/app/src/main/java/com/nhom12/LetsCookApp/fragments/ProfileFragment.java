package com.nhom12.LetsCookApp.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.database.DatabaseHelper;

public class ProfileFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private TextView textUsername, textEmail;
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Thông tin cá nhân");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_profile
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());
        textUsername = view.findViewById(R.id.text_username);
        textEmail = view.findViewById(R.id.text_email);

        // Lấy user_id từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            // Lấy thông tin người dùng từ cơ sở dữ liệu
            Cursor cursor = dbHelper.getUserById(userId);
            if (cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
                textUsername.setText("Tên người dùng: " + username);
                textEmail.setText("Email: " + email);
            }
            cursor.close();
        } else {
            textUsername.setText("Không tìm thấy thông tin người dùng");
            textEmail.setText("");
        }

        return view;
    }
}
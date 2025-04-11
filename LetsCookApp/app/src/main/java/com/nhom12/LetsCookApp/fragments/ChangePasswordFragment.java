package com.nhom12.LetsCookApp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.nhom12.LetsCookApp.R;
import com.nhom12.LetsCookApp.activities.SignInActivity;
import com.nhom12.LetsCookApp.database.DatabaseHelper;

public class ChangePasswordFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private EditText editCurrentPassword, editNewPassword, editConfirmPassword;
    private Button buttonChangePassword;
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Đổi mật khẩu");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_change_password
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        dbHelper = new DatabaseHelper(getContext());
        editCurrentPassword = view.findViewById(R.id.edit_current_password);
        editNewPassword = view.findViewById(R.id.edit_new_password);
        editConfirmPassword = view.findViewById(R.id.edit_confirm_password);
        buttonChangePassword = view.findViewById(R.id.button_change_password);

        buttonChangePassword.setOnClickListener(v -> changePassword());

           return view;
    }

    private void changePassword() {
        String currentPassword = editCurrentPassword.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            Cursor cursor = dbHelper.getUserById(userId);
            if (cursor.moveToFirst()) {
                String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD));
                if (storedPassword.equals(currentPassword)) {
                    if (dbHelper.updatePassword(userId, newPassword)) {
                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
        }
    }
}
package com.nhom12.LetsCookApp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom12.LetsCookApp.R;

public class FoodDetailFragment extends Fragment {
    TextView tvName, tvDescription, tvIngredients, tvInstructions, tvViews;
    ImageView ivImage;
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chi tiết món ăn");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo giao diện từ layout fragment_food_detail
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);

        // Ánh xạ các thành phần giao diện
        tvName = view.findViewById(R.id.tv_food_name);
        tvDescription = view.findViewById(R.id.tv_food_description);
        tvIngredients = view.findViewById(R.id.tv_ingredients);
        tvInstructions = view.findViewById(R.id.tv_instructions);
        tvViews = view.findViewById(R.id.tv_views);
        ivImage = view.findViewById(R.id.iv_food_image);

        // Lấy dữ liệu từ arguments và hiển thị lên giao diện
        Bundle args = getArguments();
        if (args != null) {
            tvName.setText(args.getString("name"));
            tvDescription.setText(args.getString("description"));
            tvIngredients.setText("Nguyên liệu: " + args.getString("ingredients"));
            tvInstructions.setText("Cách làm:\n" + args.getString("instructions"));
            tvViews.setText("Lượt xem: " + args.getInt("views"));

            // Tải ảnh từ drawable dựa trên tên ảnh
            String imageName = args.getString("image");
            int imageResId = getResources().getIdentifier(imageName.replace(".jpg", ""), "drawable", getContext().getPackageName());
            ivImage.setImageResource(imageResId);
        }

        return view;
    }
}
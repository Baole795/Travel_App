package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.databinding.ActivityAdminDashboardBinding;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Thiết lập sự kiện click cho từng mục
        // Hiện tại chỉ hiển thị Toast, sau này sẽ mở các màn hình tương ứng
        binding.cardManageUsers.setOnClickListener(v -> {
            Toast.makeText(this, "Manage Users Clicked", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, ManageUsersActivity.class);
            // startActivity(intent);
        });

        binding.cardManageContent.setOnClickListener(v -> {
            Toast.makeText(this, "Manage Content Clicked", Toast.LENGTH_SHORT).show();
        });

        binding.cardManageBookings.setOnClickListener(v -> {
            Toast.makeText(this, "Manage Bookings Clicked", Toast.LENGTH_SHORT).show();
        });

        binding.cardAnalytics.setOnClickListener(v -> {
            Toast.makeText(this, "Analytics Clicked", Toast.LENGTH_SHORT).show();
        });
        binding.cardManageUsers.setOnClickListener(v -> {
            // Bỏ comment các dòng dưới đây
            Intent intent = new Intent(this, ManageUsersActivity.class);
            startActivity(intent);
        });
        binding.cardManageContent.setOnClickListener(v -> {
            // Bỏ comment các dòng dưới đây
            Intent intent = new Intent(this, ManageContentActivity.class);
            startActivity(intent);
        });

    }
}
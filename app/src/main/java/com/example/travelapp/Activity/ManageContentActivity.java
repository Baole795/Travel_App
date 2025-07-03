package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.travelapp.databinding.ActivityManageContentBinding;

public class ManageContentActivity extends AppCompatActivity {
    private ActivityManageContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Mở màn hình quản lý danh sách chuyến đi
        binding.cardManageTrips.setOnClickListener(v -> {
            startActivity(new Intent(this, ItemListAdminActivity.class));
        });

        binding.cardManageBanners.setOnClickListener(v -> {
            // Sẽ làm sau
            Toast.makeText(this, "Manage Banners function coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}
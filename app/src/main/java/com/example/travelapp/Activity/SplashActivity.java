package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Đúng cú pháp: Inflate layout và gán vào binding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập sự kiện cho nút start
        binding.startBtn.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        });
    }
}

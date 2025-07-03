//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.travelapp.databinding.ActivitySplashBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class SplashActivity extends AppCompatActivity {
//    private ActivitySplashBinding binding;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        binding = ActivitySplashBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mAuth = FirebaseAuth.getInstance();
//
//        // Thiết lập sự kiện cho nút start
//        binding.startBtn.setOnClickListener(v -> {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser != null) {
//                // User is signed in, go to MainActivity
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            } else {
//                // No user is signed in, go to LoginActivity
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//            }
//            finish();
//        });
//    }
//
//}


package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.Domain.User;
import com.example.travelapp.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.startBtn.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Đã đăng nhập, kiểm tra trạng thái khóa
                checkUserStatus(currentUser.getUid());
            } else {
                // Chưa đăng nhập, vào Login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void checkUserStatus(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.isLocked()) {
                        // Tài khoản bị khóa, đăng xuất và báo lỗi
                        mAuth.signOut();
                        Toast.makeText(SplashActivity.this, "Your account has been locked.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        // Tài khoản không bị khóa, vào trang chủ MainActivity
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                } else {
                    // Không có thông tin -> vẫn vào trang chủ
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Lỗi -> vẫn vào trang chủ
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
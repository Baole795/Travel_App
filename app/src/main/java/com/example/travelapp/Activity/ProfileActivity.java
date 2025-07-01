package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.Domain.User;
import com.example.travelapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users").child(currentUser.getUid());

        setupButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load hoặc làm mới dữ liệu mỗi khi Activity này hiển thị
        if (currentUser != null) {
            loadUserProfile();
        }
    }

    private void loadUserProfile() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // Lấy email từ Firebase Auth (luôn có)
        String email = currentUser.getEmail();
        binding.emailTxt.setText(email);

        // Lấy các thông tin khác từ Realtime Database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Hiển thị tên
                        String fullName = user.getLastName() + " " + user.getFirstName();
                        binding.nameTxt.setText(fullName);

                        // Kiểm tra vai trò và hiển thị/ẩn nút Admin Panel
                        if (user.getRole() != null && user.getRole().equals("admin")) {
                            binding.adminPanelBtn.setVisibility(View.VISIBLE);
                        } else {
                            binding.adminPanelBtn.setVisibility(View.GONE);
                        }
                    }
                } else {
                    // Trường hợp dự phòng: nếu không có dữ liệu trong database
                    // thì thử lấy tên từ Auth Profile và ẩn nút admin
                    if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
                        binding.nameTxt.setText(currentUser.getDisplayName());
                    } else {
                        binding.nameTxt.setText("No Name Found");
                    }
                    binding.adminPanelBtn.setVisibility(View.GONE);
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Bắt sự kiện cho nút Edit Profile
        binding.editProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });
        binding.historyBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, BookingHistoryActivity.class));
        });
        binding.adminPanelBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AdminDashboardActivity.class));
        });
    }
}
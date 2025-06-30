package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.Domain.User;
import com.example.travelapp.databinding.ActivityEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users").child(currentUser.getUid());

        loadUserData();
        setupClickListeners();
    }

    private void loadUserData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emailTxt.setText(currentUser.getEmail());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        binding.firstNameEdt.setText(user.getFirstName());
                        binding.lastNameEdt.setText(user.getLastName());
                        // ▼▼▼ THÊM MỚI: HIỂN THỊ SỐ ĐIỆN THOẠI ▼▼▼
                        if (user.getPhoneNumber() != null) {
                            binding.phoneEdt.setText(user.getPhoneNumber());
                        }
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.saveBtn.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String firstName = binding.firstNameEdt.getText().toString().trim();
        String lastName = binding.lastNameEdt.getText().toString().trim();
        // ▼▼▼ THÊM MỚI: LẤY DỮ LIỆU SỐ ĐIỆN THOẠI ▼▼▼
        String phoneNumber = binding.phoneEdt.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "First name and last name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        // ▼▼▼ THÊM MỚI: ĐƯA SỐ ĐIỆN THOẠI VÀO MAP ĐỂ CẬP NHẬT ▼▼▼
        updates.put("phoneNumber", phoneNumber);

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fullName = lastName + " " + firstName;
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                currentUser.updateProfile(profileUpdates).addOnCompleteListener(authTask -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (authTask.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Database updated, but failed to update display name.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                binding.progressBar.setVisibility(View.GONE);
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Toast.makeText(EditProfileActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
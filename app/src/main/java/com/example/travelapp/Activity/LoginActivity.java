//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.travelapp.databinding.ActivityLoginBinding;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LoginActivity extends AppCompatActivity {
//    private ActivityLoginBinding binding;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mAuth = FirebaseAuth.getInstance();
//
//        binding.loginBtn.setOnClickListener(v -> {
//            String email = binding.emailEdt.getText().toString().trim();
//            String password = binding.passwordEdt.getText().toString().trim();
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            mAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, task -> {
//                        if (task.isSuccessful()) {
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//
//        binding.registerTxt.setOnClickListener(v -> {
//            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//        });
//
//        binding.forgotPasswordTxt.setOnClickListener(v -> {
//            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//        });
//    }
//}

package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.Domain.User;
import com.example.travelapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString().trim();
            String password = binding.passwordEdt.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, kiểm tra trạng thái
                            checkUserStatus(task.getResult().getUser().getUid());
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.registerTxt.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        binding.forgotPasswordTxt.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
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
                        mAuth.signOut();
                        Toast.makeText(LoginActivity.this, "Your account has been locked.", Toast.LENGTH_LONG).show();
                        // Ở lại màn hình Login, không làm gì cả
                    } else {
                        // Không bị khóa, vào trang chủ
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // Không có thông tin -> vẫn vào trang chủ
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Lỗi -> vẫn vào trang chủ
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.bumptech.glide.Glide;
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.R;
//import com.example.travelapp.databinding.ActivityDetailBinding;
//import com.example.travelapp.databinding.ActivityMainBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class DetailActivity extends AppCompatActivity {
//    ActivityDetailBinding binding;
//    private Item object;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        binding= ActivityDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        getIntentExtra();
//        setVariable();
//        }
//
//    private void setVariable() {
//        binding.titleTxt.setText(object.getTitle());
//        binding.priceTxt.setText("$"+object.getPrice());
//        binding.addressTxt.setText(object.getAddress());
//        binding.backBtn.setOnClickListener(v -> finish());
//        binding.bedTxt.setText(""+object.getBed());
//        binding.durationTxt.setText(object.getDuration());
//        binding.distanceTxt.setText(object.getDistance());
//        binding.descriptionTxt.setText(object.getDescription());
//        binding.addressTxt.setText(object.getAddress());
//        binding.ratingBar.setRating((float) object.getScore());
//        binding.ratingTxt.setText(object.getScore()+" Rating");
//
//    Glide.with(DetailActivity.this)
//            .load(object.getPic())
//            .into(binding.pic);
//
//        binding.addToCartBtn.setOnClickListener(view -> {
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (currentUser == null) {
//                Toast.makeText(DetailActivity.this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
//                // Có thể chuyển người dùng đến trang Login ở đây
//                // startActivity(new Intent(DetailActivity.this, LoginActivity.class));
//                return;
//            }
//
//            if (object.getId() == null || object.getId().isEmpty()) {
//                Toast.makeText(DetailActivity.this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Hiển thị loading...
//            Toast.makeText(DetailActivity.this, "Booking...", Toast.LENGTH_SHORT).show();
//
//            saveBookingToFirebase(currentUser.getUid(), object.getId());
//        });
//
//// Thêm hàm mới này vào DetailActivity.java
//        private void saveBookingToFirebase(String userId, String itemId) {
//            DatabaseReference bookingsRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .getReference("bookings")
//                    .child(userId);
//
//            // Tạo một ID duy nhất cho lần đặt vé này
//            String bookingId = bookingsRef.push().getKey();
//
//            // Tạo một đối tượng để lưu
//            Map<String, Object> bookingData = new HashMap<>();
//            bookingData.put("itemId", itemId);
//            bookingData.put("bookingDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
//
//            if (bookingId != null) {
//                bookingsRef.child(bookingId).setValue(bookingData)
//                        .addOnSuccessListener(aVoid -> {
//                            // Sau khi lưu thành công, mở màn hình Ticket
//                            Toast.makeText(DetailActivity.this, "Booked successfully!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
//                            intent.putExtra("object", object);
//                            startActivity(intent);
//                        })
//                        .addOnFailureListener(e -> {
//                            Toast.makeText(DetailActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//            }
//        }
//
//    private void getIntentExtra() {
//        object = (Item) getIntent().getSerializableExtra("object");
//    }
//}


package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date; // <-- Import này có thể thiếu
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Item object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gọi các hàm ở đây là đúng
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.addressTxt.setText(object.getAddress());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText("" + object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingBar.setRating((float) object.getScore());
        binding.ratingTxt.setText(object.getScore() + " Rating");

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(view -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(DetailActivity.this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (object.getId() == null || object.getId().isEmpty()) {
                Toast.makeText(DetailActivity.this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(DetailActivity.this, "Booking...", Toast.LENGTH_SHORT).show();

            // Gọi hàm ở đây là đúng
            saveBookingToFirebase(currentUser.getUid(), object.getId());
        });
    }

    // ▼▼▼ CÁC HÀM NÀY PHẢI NẰM BÊN NGOÀI setVariable(), NGANG HÀNG VỚI NÓ ▼▼▼

    private void saveBookingToFirebase(String userId, String itemId) {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("bookings")
                .child(userId);

        // Tạo một ID duy nhất cho lần đặt vé này
        String bookingId = bookingsRef.push().getKey();

        // Tạo một đối tượng để lưu
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("itemId", itemId);
        bookingData.put("bookingDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        if (bookingId != null) {
            bookingsRef.child(bookingId).setValue(bookingData)
                    .addOnSuccessListener(aVoid -> {
                        // Sau khi lưu thành công, mở màn hình Ticket
                        Toast.makeText(DetailActivity.this, "Booked successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
                        intent.putExtra("object", object);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetailActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void getIntentExtra() {
        object = (Item) getIntent().getSerializableExtra("object");
    }
}
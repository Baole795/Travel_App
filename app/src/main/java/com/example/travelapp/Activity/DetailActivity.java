////package com.example.travelapp.Activity;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Toast;
////
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////
////import com.bumptech.glide.Glide;
////import com.example.travelapp.Domain.Item;
////import com.example.travelapp.R;
////import com.example.travelapp.databinding.ActivityDetailBinding;
////import com.example.travelapp.databinding.ActivityMainBinding;
////import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.auth.FirebaseUser;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////
////import java.text.SimpleDateFormat;
////import java.util.HashMap;
////import java.util.Locale;
////import java.util.Map;
////
////public class DetailActivity extends AppCompatActivity {
////    ActivityDetailBinding binding;
////    private Item object;
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        binding= ActivityDetailBinding.inflate(getLayoutInflater());
////        setContentView(binding.getRoot());
////
////        getIntentExtra();
////        setVariable();
////        }
////
////    private void setVariable() {
////        binding.titleTxt.setText(object.getTitle());
////        binding.priceTxt.setText("$"+object.getPrice());
////        binding.addressTxt.setText(object.getAddress());
////        binding.backBtn.setOnClickListener(v -> finish());
////        binding.bedTxt.setText(""+object.getBed());
////        binding.durationTxt.setText(object.getDuration());
////        binding.distanceTxt.setText(object.getDistance());
////        binding.descriptionTxt.setText(object.getDescription());
////        binding.addressTxt.setText(object.getAddress());
////        binding.ratingBar.setRating((float) object.getScore());
////        binding.ratingTxt.setText(object.getScore()+" Rating");
////
////    Glide.with(DetailActivity.this)
////            .load(object.getPic())
////            .into(binding.pic);
////
////        binding.addToCartBtn.setOnClickListener(view -> {
////            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
////            if (currentUser == null) {
////                Toast.makeText(DetailActivity.this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
////                // Có thể chuyển người dùng đến trang Login ở đây
////                // startActivity(new Intent(DetailActivity.this, LoginActivity.class));
////                return;
////            }
////
////            if (object.getId() == null || object.getId().isEmpty()) {
////                Toast.makeText(DetailActivity.this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
////                return;
////            }
////
////            // Hiển thị loading...
////            Toast.makeText(DetailActivity.this, "Booking...", Toast.LENGTH_SHORT).show();
////
////            saveBookingToFirebase(currentUser.getUid(), object.getId());
////        });
////
////// Thêm hàm mới này vào DetailActivity.java
////        private void saveBookingToFirebase(String userId, String itemId) {
////            DatabaseReference bookingsRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
////                    .getReference("bookings")
////                    .child(userId);
////
////            // Tạo một ID duy nhất cho lần đặt vé này
////            String bookingId = bookingsRef.push().getKey();
////
////            // Tạo một đối tượng để lưu
////            Map<String, Object> bookingData = new HashMap<>();
////            bookingData.put("itemId", itemId);
////            bookingData.put("bookingDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
////
////            if (bookingId != null) {
////                bookingsRef.child(bookingId).setValue(bookingData)
////                        .addOnSuccessListener(aVoid -> {
////                            // Sau khi lưu thành công, mở màn hình Ticket
////                            Toast.makeText(DetailActivity.this, "Booked successfully!", Toast.LENGTH_SHORT).show();
////                            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
////                            intent.putExtra("object", object);
////                            startActivity(intent);
////                        })
////                        .addOnFailureListener(e -> {
////                            Toast.makeText(DetailActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                        });
////            }
////        }
////
////    private void getIntentExtra() {
////        object = (Item) getIntent().getSerializableExtra("object");
////    }
////}
//
//
//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.R;
//import com.example.travelapp.databinding.ActivityDetailBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.SimpleDateFormat;
//import java.util.Date; // <-- Import này có thể thiếu
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class DetailActivity extends AppCompatActivity {
//    ActivityDetailBinding binding;
//    private Item object;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        binding = ActivityDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Gọi các hàm ở đây là đúng
//        getIntentExtra();
//        setVariable();
//    }
//
//    private void setVariable() {
//        binding.titleTxt.setText(object.getTitle());
//        binding.priceTxt.setText("$" + object.getPrice());
//        binding.addressTxt.setText(object.getAddress());
//        binding.backBtn.setOnClickListener(v -> finish());
//        binding.bedTxt.setText("" + object.getBed());
//        binding.durationTxt.setText(object.getDuration());
//        binding.distanceTxt.setText(object.getDistance());
//        binding.descriptionTxt.setText(object.getDescription());
//        binding.ratingBar.setRating((float) object.getScore());
//        binding.ratingTxt.setText(object.getScore() + " Rating");
//
//        Glide.with(DetailActivity.this)
//                .load(object.getPic())
//                .into(binding.pic);
//
//        binding.addToCartBtn.setOnClickListener(view -> {
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (currentUser == null) {
//                Toast.makeText(DetailActivity.this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (object.getId() == null || object.getId().isEmpty()) {
//                Toast.makeText(DetailActivity.this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Toast.makeText(DetailActivity.this, "Booking...", Toast.LENGTH_SHORT).show();
//
//            // Gọi hàm ở đây là đúng
//            saveBookingToFirebase(currentUser.getUid(), object.getId());
//        });
//    }
//
//    // ▼▼▼ CÁC HÀM NÀY PHẢI NẰM BÊN NGOÀI setVariable(), NGANG HÀNG VỚI NÓ ▼▼▼
//
//    private void saveBookingToFirebase(String userId, String itemId) {
//        DatabaseReference bookingsRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("bookings")
//                .child(userId);
//
//        // Tạo một ID duy nhất cho lần đặt vé này
//        String bookingId = bookingsRef.push().getKey();
//
//        // Tạo một đối tượng để lưu
//        Map<String, Object> bookingData = new HashMap<>();
//        bookingData.put("itemId", itemId);
//        bookingData.put("bookingDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
//
//        if (bookingId != null) {
//            bookingsRef.child(bookingId).setValue(bookingData)
//                    .addOnSuccessListener(aVoid -> {
//                        // Sau khi lưu thành công, mở màn hình Ticket
//                        Toast.makeText(DetailActivity.this, "Booked successfully!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
//                        intent.putExtra("object", object);
//                        startActivity(intent);
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(DetailActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }
//
//    private void getIntentExtra() {
//        object = (Item) getIntent().getSerializableExtra("object");
//    }
//}

package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.R; // Đảm bảo bạn có import này
import com.example.travelapp.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Item object;
    private FirebaseUser currentUser;
    private DatabaseReference favoritesRef;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy thông tin người dùng hiện tại
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Lấy dữ liệu item được gửi qua Intent
        getIntentExtra();
        // Thiết lập giao diện và các sự kiện
        setVariable();

        // Nếu người dùng đã đăng nhập và item hợp lệ, kiểm tra trạng thái yêu thích
        if (currentUser != null && object != null && object.getId() != null) {
            String firebaseUrl = "https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app";
            favoritesRef = FirebaseDatabase.getInstance(firebaseUrl)
                    .getReference("favorites").child(currentUser.getUid()).child(object.getId());
            checkIfFavorite();
        }
    }

    private void setVariable() {
        if (object == null) return;

        // Điền thông tin cơ bản của item
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.addressTxt.setText(object.getAddress());
        binding.bedTxt.setText(String.valueOf(object.getBed()));
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingBar.setRating((float) object.getScore());
        binding.ratingTxt.setText(object.getScore() + " Rating");

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        // Thiết lập sự kiện click cho các nút
        binding.backBtn.setOnClickListener(v -> finish());

        binding.addToCartBtn.setOnClickListener(view -> {
            if (currentUser == null) {
                Toast.makeText(DetailActivity.this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (object.getId() == null || object.getId().isEmpty()) {
                Toast.makeText(DetailActivity.this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
                return;
            }
            saveBookingToFirebase(currentUser.getUid(), object.getId());
        });

        // Sự kiện click cho nút yêu thích (trái tim)
        binding.favBtn.setOnClickListener(v -> toggleFavorite());
    }

    private void getIntentExtra() {
        object = (Item) getIntent().getSerializableExtra("object");
    }

    // --- LOGIC CHO CHỨC NĂNG YÊU THÍCH ---

    /**
     * Lắng nghe sự thay đổi trên node favorites để cập nhật trạng thái
     */
    private void checkIfFavorite() {
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // snapshot.exists() sẽ trả về true nếu có key, false nếu không
                isFavorite = snapshot.exists();
                updateFavoriteIcon();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Error checking favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Cập nhật icon trái tim dựa trên trạng thái isFavorite
     */
    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.favBtn.setImageResource(R.drawable.ic_favorite_filled); // Icon tô đầy
        } else {
            binding.favBtn.setImageResource(R.drawable.ic_favorite_border); // Icon rỗng
        }
    }

    /**
     * Xử lý khi người dùng nhấn vào nút trái tim
     */
    private void toggleFavorite() {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to add to favorites.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isFavorite) {
            // Nếu đang là yêu thích -> Xóa khỏi danh sách bằng cách removeValue()
            favoritesRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Nếu chưa phải yêu thích -> Thêm vào danh sách bằng cách setValue(true)
            favoritesRef.setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // --- LOGIC CHO CHỨC NĂNG ĐẶT VÉ ---

    private void saveBookingToFirebase(String userId, String itemId) {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("bookings").child(userId);

        String bookingId = bookingsRef.push().getKey();
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("itemId", itemId);
        bookingData.put("bookingDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        if (bookingId != null) {
            bookingsRef.child(bookingId).setValue(bookingData)
                    .addOnSuccessListener(aVoid -> {
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
}
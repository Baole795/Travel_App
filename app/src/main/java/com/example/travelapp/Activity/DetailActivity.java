//
//
//
//
//
//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.travelapp.Domain.Booking;
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.Domain.User;
//import com.example.travelapp.R;
//import com.example.travelapp.databinding.ActivityDetailBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class DetailActivity extends AppCompatActivity {
//    private ActivityDetailBinding binding;
//    private Item object;
//    private FirebaseUser currentUser;
//    private DatabaseReference favoritesRef;
//    private boolean isFavorite = false;
//    private FirebaseDatabase database;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Khởi tạo các đối tượng Firebase
//        String firebaseUrl = "https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app";
//        database = FirebaseDatabase.getInstance(firebaseUrl);
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        getIntentExtra();
//        // Kiểm tra nếu không có dữ liệu item thì thoát
//        if (object == null) {
//            Toast.makeText(this, "Error: Item data is missing.", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
//
//        setVariable();
//
//        // Kiểm tra trạng thái yêu thích
//        if (currentUser != null && object.getId() != null) {
//            favoritesRef = database.getReference("favorites").child(currentUser.getUid()).child(object.getId());
//            checkIfFavorite();
//        }
//    }
//
//    private void setVariable() {
//        // Điền thông tin cơ bản của item
//        binding.titleTxt.setText(object.getTitle());
//        binding.priceTxt.setText("$" + object.getPrice());
//        binding.addressTxt.setText(object.getAddress());
//        binding.bedTxt.setText(String.valueOf(object.getBed()));
//        binding.durationTxt.setText(object.getDuration());
//        binding.distanceTxt.setText(object.getDistance());
//        binding.descriptionTxt.setText(object.getDescription());
//        binding.ratingBar.setRating((float) object.getScore());
//        binding.ratingTxt.setText(object.getScore() + " Rating");
//
//        Glide.with(this).load(object.getPic()).into(binding.pic);
//
//        // Thiết lập sự kiện click
//        binding.backBtn.setOnClickListener(v -> finish());
//        binding.favBtn.setOnClickListener(v -> toggleFavorite());
//        binding.addToCartBtn.setOnClickListener(v -> handleBooking());
//    }
//
//    private void getIntentExtra() {
//        object = (Item) getIntent().getSerializableExtra("object");
//    }
//
//    // Bắt đầu luồng đặt vé
//    private void handleBooking() {
//        if (currentUser == null) {
//            Toast.makeText(this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (object.getId() == null || object.getId().isEmpty()) {
//            Toast.makeText(this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // Vô hiệu hóa nút để tránh click nhiều lần
//        binding.addToCartBtn.setEnabled(false);
//        binding.addToCartBtn.setText("Booking...");
//
//        // Lấy thông tin người dùng trước khi lưu booking
//        getUserInfoAndSaveBooking(currentUser.getUid(), object.getId());
//    }
////tạm comment lại phần lấy thông tin người dùng từ node /users
//    // Lấy thông tin người dùng từ node /users
////    private void getUserInfoAndSaveBooking(String userId, String itemId) {
////        DatabaseReference userRef = database.getReference("users").child(userId);
////        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                String fullName = currentUser.getDisplayName(); // Lấy tên mặc định từ Auth
////                if (snapshot.exists()) {
////                    User user = snapshot.getValue(User.class);
////                    if (user != null && user.getFirstName() != null && user.getLastName() != null) {
////                        fullName = user.getFirstName() + " " + user.getLastName();
////                    }
////                }
////                // Sau khi có thông tin, tiến hành lưu booking
////                saveBookingToFirebase(userId, itemId, fullName, currentUser.getEmail());
////            }
////
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////                Toast.makeText(DetailActivity.this, "Could not get user info. Please try again.", Toast.LENGTH_SHORT).show();
////                binding.addToCartBtn.setEnabled(true); // Kích hoạt lại nút
////                binding.addToCartBtn.setText("Đặt vé");
////            }
////        });
////    }
//    //hàm mới
//private void getUserInfoAndSaveBooking(String userId, String itemId) {
//    binding.addToCartBtn.setEnabled(false);
//    binding.addToCartBtn.setText("Booking...");
//    DatabaseReference userRef = database.getReference("users").child(userId);
//    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            String fullName = currentUser.getDisplayName();
//            String email = currentUser.getEmail();
//            String phoneNumber = ""; // Giá trị mặc định
//
//            if (snapshot.exists()) {
//                User user = snapshot.getValue(User.class);
//                if (user != null) {
//                    if (user.getFirstName() != null && user.getLastName() != null) {
//                        fullName = user.getFirstName() + " " + user.getLastName();
//                    }
//                    if (user.getPhoneNumber() != null) {
//                        phoneNumber = user.getPhoneNumber(); // Lấy SĐT
//                    }
//                }
//            }
//            saveBookingToFirebase(userId, itemId, fullName, email, phoneNumber);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//            Toast.makeText(DetailActivity.this, "Could not get user info.", Toast.LENGTH_SHORT).show();
//            binding.addToCartBtn.setEnabled(true);
//            binding.addToCartBtn.setText("Đặt vé");
//        }
//    });
//}
//
//    // Lưu booking vào Firebase
//    private void saveBookingToFirebase(String userId, String itemId, String userFullName, String userEmail, String userPhoneNumber) {
//        DatabaseReference allBookingsRef = database.getReference("all_bookings");
//        String bookingId = allBookingsRef.push().getKey();
//
//        if (bookingId == null) {
//            Toast.makeText(this, "Failed to create booking ID.", Toast.LENGTH_SHORT).show();
//            binding.addToCartBtn.setEnabled(true);
//            binding.addToCartBtn.setText("Đặt vé");
//            return;
//        }
//
//        // Tạo đối tượng Booking
//        Booking booking = new Booking();
//        booking.setBookingId(bookingId);
//        booking.setUserId(userId);
//        booking.setItemId(itemId);
//        booking.setBookingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
//        booking.setStatus("Confirmed");
//        booking.setUserFullName(userFullName);
//        booking.setUserEmail(userEmail);
//        booking.setItemTitle(object.getTitle());
//        booking.setItemPicUrl(object.getPic());
//        booking.setItemPrice(object.getPrice());
//
//        // Ghi vào /all_bookings
//        allBookingsRef.child(bookingId).setValue(booking).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Ghi vào lịch sử của user /bookings/{userId}
//                DatabaseReference userBookingRef = database.getReference("bookings").child(userId).child(bookingId);
//                userBookingRef.child("itemId").setValue(itemId);
//
//                Toast.makeText(this, "Booked successfully!", Toast.LENGTH_SHORT).show();
//
//                // Chuyển sang màn hình Ticket
//                Intent intent = new Intent(this, TicketActivity.class);
//                intent.putExtra("object", object);
//                startActivity(intent);
//                finish(); // Đóng màn hình Detail sau khi đặt vé thành công
//
//            } else {
//                Toast.makeText(this, "Booking failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
//            }
//            // Kích hoạt lại nút dù thành công hay thất bại (trường hợp này không cần vì đã finish)
//            binding.addToCartBtn.setEnabled(true);
//            binding.addToCartBtn.setText("Đặt vé");
//        });
//    }
//
//    // --- LOGIC CHO CHỨC NĂNG YÊU THÍCH ---
//    private void checkIfFavorite() {
//        favoritesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                isFavorite = snapshot.exists();
//                updateFavoriteIcon();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DetailActivity.this, "Error checking favorites.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateFavoriteIcon() {
//        if (isFavorite) {
//            binding.favBtn.setImageResource(R.drawable.ic_favorite_filled);
//        } else {
//            binding.favBtn.setImageResource(R.drawable.ic_favorite_border);
//        }
//    }
//
//    private void toggleFavorite() {
//        if (currentUser == null) {
//            Toast.makeText(this, "Please log in to add to favorites.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (isFavorite) {
//            favoritesRef.removeValue();
//        } else {
//            favoritesRef.setValue(true);
//        }
//    }
//}

package com.example.travelapp.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.Domain.User;
import com.example.travelapp.R;
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
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private Item object;
    private FirebaseUser currentUser;
    private DatabaseReference favoritesRef;
    private boolean isFavorite = false;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các đối tượng Firebase
        String firebaseUrl = "https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app";
        database = FirebaseDatabase.getInstance(firebaseUrl);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getIntentExtra();
        // Kiểm tra nếu không có dữ liệu item thì thoát Activity để tránh crash
        if (object == null) {
            Toast.makeText(this, "Error: Item data is missing.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setVariable();

        // Kiểm tra trạng thái yêu thích của item
        if (currentUser != null && object.getId() != null) {
            favoritesRef = database.getReference("favorites").child(currentUser.getUid()).child(object.getId());
            checkIfFavorite();
        }
    }

    private void setVariable() {
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

        Glide.with(this).load(object.getPic()).into(binding.pic);

        // Thiết lập sự kiện click cho các nút
        binding.backBtn.setOnClickListener(v -> finish());
        binding.favBtn.setOnClickListener(v -> toggleFavorite());
        binding.addToCartBtn.setOnClickListener(v -> handleBooking());

    }

    private void getIntentExtra() {
        object = (Item) getIntent().getSerializableExtra("object");
    }

    // --- Bắt đầu luồng đặt vé ---
    private void handleBooking() {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to book a ticket.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (object.getId() == null || object.getId().isEmpty()) {
            Toast.makeText(this, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Vô hiệu hóa nút để tránh click nhiều lần
        binding.addToCartBtn.setEnabled(false);
        binding.addToCartBtn.setText("Booking...");

        // Lấy thông tin người dùng trước khi lưu booking
        getUserInfoAndSaveBooking(currentUser.getUid(), object.getId());
    }

    // Lấy thông tin người dùng từ node /users
    private void getUserInfoAndSaveBooking(String userId, String itemId) {
        DatabaseReference userRef = database.getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = currentUser.getDisplayName(); // Lấy tên mặc định từ Auth
                String email = currentUser.getEmail(); // Email luôn có từ Auth
                String phoneNumber = ""; // Giá trị mặc định

                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getFirstName() != null && user.getLastName() != null) {
                            fullName = user.getFirstName() + " " + user.getLastName();
                        }
                        if (user.getPhoneNumber() != null) {
                            phoneNumber = user.getPhoneNumber();
                        }
                    }
                }
                // Sau khi có thông tin, tiến hành lưu booking
                saveBookingToFirebase(userId, itemId, fullName, email, phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Could not get user info. Please try again.", Toast.LENGTH_SHORT).show();
                binding.addToCartBtn.setEnabled(true); // Kích hoạt lại nút nếu có lỗi
                binding.addToCartBtn.setText("Đặt vé");
            }
        });
    }

    // Lưu booking vào Firebase
    private void saveBookingToFirebase(String userId, String itemId, String userFullName, String userEmail, String userPhoneNumber) {
        DatabaseReference allBookingsRef = database.getReference("all_bookings");
        String bookingId = allBookingsRef.push().getKey();

        if (bookingId == null) {
            Toast.makeText(this, "Failed to create booking ID.", Toast.LENGTH_SHORT).show();
            binding.addToCartBtn.setEnabled(true);
            binding.addToCartBtn.setText("Đặt vé");
            return;
        }

        // Tạo đối tượng Booking với đầy đủ thông tin
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUserId(userId);
        booking.setItemId(itemId);
        booking.setBookingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        booking.setStatus("Confirmed");
        booking.setUserFullName(userFullName);
        booking.setUserEmail(userEmail);
        booking.setUserPhoneNumber(userPhoneNumber);
        booking.setItemTitle(object.getTitle());
        booking.setItemPicUrl(object.getPic());
        booking.setItemPrice(object.getPrice());
        booking.setItemDuration(object.getDuration());
        booking.setItemBed(object.getBed());

        Log.d(TAG, "Saving Booking Object: Phone=" + booking.getUserPhoneNumber() + ", Price=" + booking.getItemPrice());

        // Ghi vào Firebase
        allBookingsRef.child(bookingId).setValue(booking).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Ghi vào lịch sử cá nhân của user
                DatabaseReference userBookingRef = database.getReference("bookings").child(userId).child(bookingId);
                userBookingRef.child("itemId").setValue(itemId);

                Toast.makeText(this, "Booked successfully!", Toast.LENGTH_SHORT).show();

                // Chuyển sang màn hình Ticket
                Intent intent = new Intent(this, TicketActivity.class);
                intent.putExtra("object", object);
                startActivity(intent);
                finish(); // Đóng màn hình Detail
            } else {
                Toast.makeText(this, "Booking failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                binding.addToCartBtn.setEnabled(true);
                binding.addToCartBtn.setText("Đặt vé");
            }
        });
    }

    // --- LOGIC CHO CHỨC NĂNG YÊU THÍCH ---
    private void checkIfFavorite() {
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isFavorite = snapshot.exists();
                updateFavoriteIcon();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Error checking favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.favBtn.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            binding.favBtn.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void toggleFavorite() {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to add to favorites.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isFavorite) {
            favoritesRef.removeValue();
        } else {
            favoritesRef.setValue(true);
        }
    }
}
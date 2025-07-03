//package com.example.travelapp.Activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.example.travelapp.Adapter.BookingAdminAdapter;
//import com.example.travelapp.Domain.Booking;
//import com.example.travelapp.databinding.ActivityManageBookingsBinding;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class ManageBookingsActivity extends AppCompatActivity {
//    private ActivityManageBookingsBinding binding;
//    private BookingAdminAdapter adapter;
//    private ArrayList<Booking> bookingList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityManageBookingsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.toolbar.setNavigationOnClickListener(v -> finish());
//
//        bookingList = new ArrayList<>();
//        adapter = new BookingAdminAdapter(this, bookingList);
//        binding.bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.bookingsRecyclerView.setAdapter(adapter);
//
//        loadAllBookings();
//    }
//
//    private void loadAllBookings() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        DatabaseReference ref = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("all_bookings");
//
//        ref.orderByChild("bookingDate").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                bookingList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Booking booking = dataSnapshot.getValue(Booking.class);
//                    if (booking != null) { // Thêm kiểm tra null để tránh crash
//                        bookingList.add(booking);
//                    }
//                }
//                Collections.reverse(bookingList); // Mới nhất lên đầu
//                adapter.notifyDataSetChanged();
//                binding.progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                binding.progressBar.setVisibility(View.GONE);
//                Toast.makeText(ManageBookingsActivity.this, "Failed to load bookings: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView; // Import đúng thư viện SearchView
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.BookingAdminAdapter;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.databinding.ActivityManageBookingsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageBookingsActivity extends AppCompatActivity {
    private ActivityManageBookingsBinding binding;
    private BookingAdminAdapter adapter;

    // ▼▼▼ THÊM 2 DANH SÁCH MỚI ▼▼▼
    private ArrayList<Booking> fullBookingList;    // Danh sách gốc chứa tất cả booking
    private ArrayList<Booking> filteredBookingList; // Danh sách đã lọc để hiển thị
    // ▲▲▲

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo các danh sách và adapter
        initRecyclerView();

        // Thiết lập SearchView
        setupSearchView();

        // Tải tất cả booking từ Firebase
        loadAllBookings();
    }

    private void initRecyclerView() {
        fullBookingList = new ArrayList<>();
        filteredBookingList = new ArrayList<>();

        // Adapter bây giờ sẽ làm việc với danh sách đã lọc (filteredBookingList)
        adapter = new BookingAdminAdapter(this, filteredBookingList);

        binding.bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.bookingsRecyclerView.setAdapter(adapter);
    }

    // ▼▼▼ THÊM MỚI: HÀM THIẾT LẬP SEARCHVIEW ▼▼▼
    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Không cần làm gì khi submit, vì đã lọc ở onQueryTextChange
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Gọi hàm lọc mỗi khi văn bản trong SearchView thay đổi
                filterBookings(newText);
                return true;
            }
        });
    }

    // ▼▼▼ THÊM MỚI: HÀM LỌC BOOKING ▼▼▼
    private void filterBookings(String text) {
        // Xóa danh sách đã lọc hiện tại
        filteredBookingList.clear();

        if (text.isEmpty()) {
            // Nếu ô tìm kiếm trống, hiển thị tất cả booking
            filteredBookingList.addAll(fullBookingList);
        } else {
            // Nếu có từ khóa, lặp qua danh sách gốc
            String searchText = text.toLowerCase().trim();
            for (Booking booking : fullBookingList) {
                // Kiểm tra xem email của khách hàng có chứa từ khóa tìm kiếm không
                if (booking.getUserEmail() != null && booking.getUserEmail().toLowerCase().contains(searchText)) {
                    filteredBookingList.add(booking);
                }
            }
        }

        // Thông báo cho adapter rằng dữ liệu đã thay đổi
        adapter.notifyDataSetChanged();

        // Hiển thị/ẩn thông báo "No bookings found"
        checkIfEmpty();
    }

    private void loadAllBookings() {
        binding.progressBar.setVisibility(View.VISIBLE);
        DatabaseReference ref = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("all_bookings");

        ref.orderByChild("bookingDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Xóa danh sách gốc trước khi thêm dữ liệu mới
                fullBookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        fullBookingList.add(booking);
                    }
                }
                Collections.reverse(fullBookingList); // Mới nhất lên đầu

                // Sau khi tải xong, áp dụng bộ lọc hiện tại (thường là rỗng)
                filterBookings(binding.searchView.getQuery().toString());

                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ManageBookingsActivity.this, "Failed to load bookings: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ▼▼▼ THÊM MỚI: HÀM KIỂM TRA DANH SÁCH RỖNG ▼▼▼
    private void checkIfEmpty() {
        if (filteredBookingList.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }
    }
}
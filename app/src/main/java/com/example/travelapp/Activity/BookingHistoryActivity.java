package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.HistoryAdapter;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivityBookingHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    private ActivityBookingHistoryBinding binding;
    private HistoryAdapter adapter;
    private ArrayList<Item> historyList;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app");

        initRecyclerView();
        loadBookingHistory();

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        historyList = new ArrayList<>();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(historyList, this);
        binding.historyRecyclerView.setAdapter(adapter);
    }

    private void loadBookingHistory() {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to see history.", Toast.LENGTH_SHORT).show();
            binding.emptyView.setVisibility(View.VISIBLE);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        DatabaseReference bookingIdsRef = database.getReference("bookings").child(currentUser.getUid());

        bookingIdsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.emptyView.setVisibility(View.VISIBLE);
                    return;
                }

                List<String> itemIds = new ArrayList<>();
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    String itemId = bookingSnapshot.child("itemId").getValue(String.class);
                    if (itemId != null) {
                        itemIds.add(itemId);
                    }
                }
                // Đảo ngược danh sách để hiển thị các booking mới nhất lên đầu
                Collections.reverse(itemIds);
                loadItemDetails(itemIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingHistoryActivity.this, "Failed to load booking IDs.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadItemDetails(List<String> itemIds) {
        DatabaseReference itemsRef = database.getReference("Item"); // Hoặc "Popular" tùy nơi bạn lưu item
        historyList.clear();

        if (itemIds.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
            return;
        }

        for (String itemId : itemIds) {
            itemsRef.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Item item = snapshot.getValue(Item.class);
                        if (item != null) {
                            item.setId(snapshot.getKey()); // Gán ID cho item
                            historyList.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    // Ẩn progress bar khi đã load xong item cuối cùng
                    if (historyList.size() == itemIds.size()) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (historyList.size() == itemIds.size()) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
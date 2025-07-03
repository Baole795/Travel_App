package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.PopularAdapter;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivityBookmarkBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private ActivityBookmarkBinding binding;
    private PopularAdapter adapter;
    private ArrayList<Item> bookmarkedList;
    private FirebaseUser currentUser;

    // ▼▼▼ SỬA LẠI KHAI BÁO BIẾN ▼▼▼
    private FirebaseDatabase database; // Giữ một instance của FirebaseDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // ▼▼▼ SỬA LẠI CÁCH KHỞI TẠO ▼▼▼
        // Khởi tạo đối tượng database chính
        database = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app");

        initRecyclerView();
        loadBookmarkedItems();

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        bookmarkedList = new ArrayList<>();
        binding.bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PopularAdapter(bookmarkedList);
        binding.bookmarkRecyclerView.setAdapter(adapter);
    }

    private void loadBookmarkedItems() {
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to see your bookmarks.", Toast.LENGTH_SHORT).show();
            binding.emptyView.setVisibility(View.VISIBLE);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        // ▼▼▼ SỬA LẠI CÁCH LẤY REFERENCE ▼▼▼
        // Lấy tham chiếu đến node 'favorites' từ đối tượng database
        DatabaseReference favoriteIdsRef = database.getReference("favorites").child(currentUser.getUid());

        favoriteIdsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> itemIds = new ArrayList<>();
                for (DataSnapshot idSnapshot : snapshot.getChildren()) {
                    itemIds.add(idSnapshot.getKey());
                }
                fetchItemDetails(itemIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(BookmarkActivity.this, "Failed to load bookmarks.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchItemDetails(List<String> itemIds) {
        bookmarkedList.clear();
        if (itemIds.isEmpty()) {
            binding.progressBar.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            return;
        }

        binding.emptyView.setVisibility(View.GONE);

        // ▼▼▼ SỬA LẠI CÁCH LẤY REFERENCE ▼▼▼
        // Lấy tham chiếu đến node 'Item' từ đối tượng database
        DatabaseReference itemsRef = database.getReference("Item");

        final int[] itemsToLoad = {itemIds.size()}; // Dùng để đếm ngược

        for (String itemId : itemIds) {
            itemsRef.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Item item = snapshot.getValue(Item.class);
                        if (item != null) {
                            item.setId(snapshot.getKey());
                            bookmarkedList.add(item);
                            adapter.notifyItemInserted(bookmarkedList.size() - 1);
                        }
                    }
                    itemsToLoad[0]--; // Giảm bộ đếm
                    if (itemsToLoad[0] <= 0) { // Khi đã load xong tất cả
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    itemsToLoad[0]--; // Giảm bộ đếm
                    if (itemsToLoad[0] <= 0) { // Khi đã load xong tất cả
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
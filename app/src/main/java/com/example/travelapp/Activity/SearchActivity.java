package com.example.travelapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.SearchAdapter;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchAdapter adapter;
    private ArrayList<Item> searchResults;
    private String queryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy từ khóa tìm kiếm được gửi từ MainActivity
        queryText = getIntent().getStringExtra("query");

        // Thiết lập Toolbar
        binding.toolbar.setTitle("Results for: \"" + queryText + "\"");
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo RecyclerView
        initRecyclerView();

        // Thực hiện tìm kiếm
        if (queryText != null && !queryText.isEmpty()) {
            performSearch(queryText);
        } else {
            Toast.makeText(this, "Search query is empty", Toast.LENGTH_SHORT).show();
            binding.emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        searchResults = new ArrayList<>();
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(searchResults, this);
        binding.searchResultsRecyclerView.setAdapter(adapter);
    }

    private void performSearch(String searchText) {
        binding.progressBar.setVisibility(View.VISIBLE);

        // Chúng ta sẽ tìm kiếm trên cả 2 node "Item" và "Popular"
        searchInNode("Item", searchText);
        searchInNode("Popular", searchText);
    }

    private void searchInNode(String node, String searchText) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(node);

        // Query để tìm các item có 'title' bắt đầu bằng searchText
        // \uf8ff là một ký tự Unicode rất cao, giúp tạo một khoảng tìm kiếm
        Query query = reference.orderByChild("title")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Item item = issue.getValue(Item.class);
                        if (item != null) {
                            item.setId(issue.getKey());

                            // Kiểm tra để không thêm item trùng lặp
                            boolean alreadyExists = false;
                            for (Item existingItem : searchResults) {
                                if (existingItem.getId().equals(item.getId())) {
                                    alreadyExists = true;
                                    break;
                                }
                            }
                            if (!alreadyExists) {
                                searchResults.add(item);
                            }
                        }
                    }
                }

                // Cập nhật giao diện sau khi tìm kiếm xong
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void updateUI() {
        binding.progressBar.setVisibility(View.GONE);
        if (searchResults.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.searchResultsRecyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}
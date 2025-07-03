//package com.example.travelapp.Activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.travelapp.Adapter.PopularAdapter; // Có thể tái sử dụng Adapter này
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.databinding.ActivityItemListBinding;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class ItemListActivity extends AppCompatActivity {
//
//    private ActivityItemListBinding binding;
//    private RecyclerView.Adapter adapter;
//    private ArrayList<Item> itemList;
//    private String categoryNode;
//    private String categoryTitle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityItemListBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Lấy dữ liệu được gửi từ MainActivity
//        categoryNode = getIntent().getStringExtra("CATEGORY_NODE");
//        categoryTitle = getIntent().getStringExtra("CATEGORY_TITLE");
//
//        // Thiết lập Toolbar
//        binding.toolbar.setTitle(categoryTitle);
//        binding.toolbar.setNavigationOnClickListener(v -> finish());
//
//        // Khởi tạo RecyclerView
//        initRecyclerView();
//
//        // Tải dữ liệu từ Firebase
//        loadItemsFromFirebase();
//    }
//
//    private void initRecyclerView() {
//        itemList = new ArrayList<>();
//        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        // Chúng ta có thể tái sử dụng PopularAdapter vì chúng có cùng cấu trúc hiển thị
//        adapter = new PopularAdapter(itemList);
//        binding.itemsRecyclerView.setAdapter(adapter);
//    }
//
//    private void loadItemsFromFirebase() {
//        if (categoryNode == null || categoryNode.isEmpty()) {
//            Toast.makeText(this, "Category not specified", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        binding.progressBar.setVisibility(View.VISIBLE);
//        DatabaseReference reference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(categoryNode);
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        Item item = issue.getValue(Item.class);
//                        if (item != null) {
//                            item.setId(issue.getKey());
//                            itemList.add(item);
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//
//                binding.progressBar.setVisibility(View.GONE);
//                if (itemList.isEmpty()) {
//                    binding.emptyView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                binding.progressBar.setVisibility(View.GONE);
//                Toast.makeText(ItemListActivity.this, "Failed to load items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Adapter.PopularAdapter; // Tái sử dụng Adapter này là một ý tưởng hay
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivityItemListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private ActivityItemListBinding binding;
    private RecyclerView.Adapter adapter;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy dữ liệu được gửi từ MainActivity
        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        String categoryNode = getIntent().getStringExtra("CATEGORY_NODE");
        String categoryTitle = getIntent().getStringExtra("CATEGORY_TITLE");

        // Thiết lập Toolbar
        if (categoryTitle != null) {
            binding.toolbar.setTitle(categoryTitle);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo RecyclerView
        initRecyclerView();

        // Tải dữ liệu từ Firebase dựa trên thông tin nhận được
        loadItemsFromFirebase(categoryId, categoryNode);
    }

    private void initRecyclerView() {
        itemList = new ArrayList<>();
        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // Tái sử dụng PopularAdapter là hợp lý vì chúng có cùng cấu trúc hiển thị
        adapter = new PopularAdapter(itemList);
        binding.itemsRecyclerView.setAdapter(adapter);
    }

    private void loadItemsFromFirebase(String categoryId, String categoryNode) {
        binding.progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference;
        Query query;

        // Kịch bản 1: Lọc theo Category ID (khi người dùng nhấn vào một icon Category)
        if (categoryId != null) {
            reference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Item");
            // Tạo một query để chỉ lấy các item có categoryId khớp
            query = reference.orderByChild("categoryId").equalTo(categoryId);
        }
        // Kịch bản 2: Lấy tất cả item từ một node (khi người dùng nhấn "See All")
        else if (categoryNode != null) {
            reference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(categoryNode);
            query = reference; // Lấy toàn bộ node
        }
        // Kịch bản 3: Không có thông tin -> không làm gì
        else {
            Toast.makeText(this, "No category specified.", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear(); // Xóa dữ liệu cũ trước khi thêm mới
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Item item = issue.getValue(Item.class);
                        if (item != null) {
                            item.setId(issue.getKey());
                            itemList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                // Cập nhật giao diện sau khi tải xong
                binding.progressBar.setVisibility(View.GONE);
                if (itemList.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ItemListActivity.this, "Failed to load items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
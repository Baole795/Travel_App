//package com.example.travelapp.Activity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.example.travelapp.Adapter.UserAdapter;
//import com.example.travelapp.Domain.User;
//import com.example.travelapp.databinding.ActivityManageUsersBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ManageUsersActivity extends AppCompatActivity {
//    private ActivityManageUsersBinding binding;
//    private UserAdapter userAdapter;
//    private List<User> userList;
//    private List<User> filteredList;
//    private DatabaseReference databaseReference;
//    private String currentUserId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityManageUsersBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        binding.toolbar.setNavigationOnClickListener(v -> finish());
//
//        initRecyclerView();
//        setupSearchView();
//
//        databaseReference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
//        loadUsers();
//    }
//
//    private void initRecyclerView() {
//        userList = new ArrayList<>();
//        filteredList = new ArrayList<>();
//        binding.usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        userAdapter = new UserAdapter(this, filteredList);
//        binding.usersRecyclerView.setAdapter(userAdapter);
//    }
//
//    private void setupSearchView() {
//        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });
//    }
//
//    private void filter(String text) {
//        filteredList.clear();
//        if (text.isEmpty()) {
//            filteredList.addAll(userList);
//        } else {
//            text = text.toLowerCase();
//            for (User user : userList) {
//                String fullName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
//                String email = user.getEmail().toLowerCase();
//                if (fullName.contains(text) || email.contains(text)) {
//                    filteredList.add(user);
//                }
//            }
//        }
//        userAdapter.notifyDataSetChanged();
//    }
//
//    private void loadUsers() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        user.setUid(dataSnapshot.getKey());
//                        // Không thêm admin hiện tại vào danh sách để tránh tự khóa/đổi vai trò
//                        if (!user.getUid().equals(currentUserId)) {
//                            userList.add(user);
//                        }
//                    }
//                }
//                // Sau khi tải, áp dụng bộ lọc hiện tại (hoặc hiển thị tất cả nếu rỗng)
//                filter(binding.searchView.getQuery().toString());
//                binding.progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                binding.progressBar.setVisibility(View.GONE);
//                Toast.makeText(ManageUsersActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.UserAdapter;
import com.example.travelapp.Domain.User;
import com.example.travelapp.databinding.ActivityManageUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private ActivityManageUsersBinding binding;
    private UserAdapter userAdapter;
    private List<User> fullUserList;
    private List<User> filteredUserList;
    private DatabaseReference databaseReference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        initRecyclerView();
        setupSearchView();

        databaseReference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
        loadUsers();
    }

    private void initRecyclerView() {
        fullUserList = new ArrayList<>();
        filteredUserList = new ArrayList<>();
        binding.usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, filteredUserList);
        binding.usersRecyclerView.setAdapter(userAdapter);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

    // ▼▼▼ PHIÊN BẢN HÀM filterUsers ĐÃ ĐƯỢC SỬA LỖI ▼▼▼
    private void filterUsers(String text) {
        filteredUserList.clear();

        if (text.isEmpty()) {
            filteredUserList.addAll(fullUserList);
        } else {
            text = text.toLowerCase().trim();
            for (User user : fullUserList) {
                // Kiểm tra null trước khi sử dụng
                String firstName = user.getFirstName() != null ? user.getFirstName() : "";
                String lastName = user.getLastName() != null ? user.getLastName() : "";
                String email = user.getEmail() != null ? user.getEmail() : "";

                String fullName = (firstName + " " + lastName).toLowerCase().trim();

                // Bây giờ việc tìm kiếm đã an toàn
                if (fullName.contains(text) || email.toLowerCase().contains(text)) {
                    filteredUserList.add(user);
                }
            }
        }

        userAdapter.notifyDataSetChanged();
        checkIfEmpty();
    }
    // ▲▲▲ KẾT THÚC PHẦN SỬA LỖI ▲▲▲

    private void loadUsers() {
        binding.progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullUserList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUid(dataSnapshot.getKey());
                        if (currentUserId != null && !user.getUid().equals(currentUserId)) {
                            fullUserList.add(user);
                        }
                    }
                }
                filterUsers(binding.searchView.getQuery().toString());
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ManageUsersActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfEmpty() {
        if (filteredUserList.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }
    }
}
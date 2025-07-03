//
//
//package com.example.travelapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.CompositePageTransformer;
//import androidx.viewpager2.widget.MarginPageTransformer;
//
//import com.example.travelapp.Adapter.CategoryAdapter;
//import com.example.travelapp.Adapter.PopularAdapter;
//import com.example.travelapp.Adapter.RecommendedAdapter;
//import com.example.travelapp.Adapter.SliderAdapter;
//import com.example.travelapp.Domain.Category;
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.Domain.Location;
//import com.example.travelapp.Domain.SliderItems;
//import com.example.travelapp.R;
//import com.example.travelapp.databinding.ActivityMainBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding;
//    private FirebaseDatabase database;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mAuth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app");
//
//        setUserInfo();
//        setupLogoutButton();
//        setupClickListeners(); // <-- Hàm mới để gom các listener
//
//        initLocations();
//        initBanners();
//        initCategory();
//        initPopular();
//        initRecommended();
//    }
//
//    private void setupClickListeners() {
//        // Listener cho Bottom Menu
//        binding.bottomMenu.setItemSelected(R.id.exporer, true);
//        binding.bottomMenu.setOnItemSelectedListener(i -> {
//            if (i == R.id.cart) {
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//            }
//        });
//
//        // Listener cho nút Search
//        binding.button.setOnClickListener(v -> {
//            String searchText = binding.editTextText.getText().toString().trim();
//            if (searchText.isEmpty()) {
//                Toast.makeText(MainActivity.this, "Please enter a destination to search", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                intent.putExtra("query", searchText);
//                startActivity(intent);
//            }
//        });
//
//        // ▼▼▼ THÊM MỚI: Listener cho hai nút "See All" ▼▼▼
//        binding.seeAllPopular.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
//            // Gửi tên node Firebase và tiêu đề cho màn hình tiếp theo
//            intent.putExtra("CATEGORY_NODE", "Popular");
//            intent.putExtra("CATEGORY_TITLE", "Popular Destinations");
//            startActivity(intent);
//        });
//
//        binding.seeAllRecommendedBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
//            // Gửi tên node Firebase và tiêu đề cho màn hình tiếp theo
//            intent.putExtra("CATEGORY_NODE", "Item");
//            intent.putExtra("CATEGORY_TITLE", "Recommended");
//            startActivity(intent);
//        });
//        // ▲▲▲ KẾT THÚC PHẦN THÊM MỚI ▲▲▲
//    }
//
//    private void setUserInfo() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            String displayName = user.getDisplayName();
//            if (displayName != null && !displayName.isEmpty()) {
//                binding.welcomeTxt.setText("Welcome " + displayName + "!");
//            } else {
//                binding.welcomeTxt.setText("Welcome!");
//            }
//        }
//    }
//
//    private void setupLogoutButton() {
//        binding.logoutBtn.setOnClickListener(v -> {
//            mAuth.signOut();
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private void initRecommended() {
//        DatabaseReference myref = database.getReference("Item");
//        ArrayList<Item> list = new ArrayList<>();
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot issuee : snapshot.getChildren()) {
//                        Item item = issuee.getValue(Item.class);
//                        if (item != null) {
//                            item.setId(issuee.getKey());
//                            list.add(item);
//                        }
//                    }
//                    if(!list.isEmpty()){
//                        binding.recyclerViewRecommended.setLayoutManager(
//                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                        RecyclerView.Adapter adapter = new RecommendedAdapter(list);
//                        binding.recyclerViewRecommended.setAdapter(adapter);
//                    }
//                    binding.progressBarRecommended.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void initPopular() {
//        DatabaseReference myref = database.getReference("Popular");
//        ArrayList<Item> list = new ArrayList<>();
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot issuee : snapshot.getChildren()) {
//                        Item item = issuee.getValue(Item.class);
//                        if (item != null) {
//                            item.setId(issuee.getKey());
//                            list.add(item);
//                        }
//                    }
//                    if(!list.isEmpty()){
//                        binding.recyclerViewPopular.setLayoutManager(
//                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                        RecyclerView.Adapter adapter = new PopularAdapter(list);
//                        binding.recyclerViewPopular.setAdapter(adapter);
//                    }
//                    binding.progressBarPopular.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    // Các hàm initCategory, initLocations, banners, initBanners giữ nguyên không đổi
//    private void initCategory() {
//        DatabaseReference myref = database.getReference("Category");
//        ArrayList<Category> list=new ArrayList<>();
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot issuee : snapshot.getChildren()) {
//                        list.add(issuee.getValue(Category.class));
//                    }
//                    if(!list.isEmpty()){
//                        binding.recyclerViewCategory.setLayoutManager(
//                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
//                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
//                        binding.recyclerViewCategory.setAdapter(adapter);
//                    }
//                    binding.progressBarCategory.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void initLocations() {
//        DatabaseReference myref = database.getReference("Location");
//        ArrayList<Location> list=new ArrayList<>();
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot issuee : snapshot.getChildren()) {
//                        list.add(issuee.getValue(Location.class));
//                    }
//                    ArrayAdapter<Location> adapter=new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    binding.locationSp.setAdapter(adapter);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void banners(ArrayList<SliderItems> items) {
//        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
//        binding.viewPager2.setClipToPadding(false);
//        binding.viewPager2.setClipChildren(false);
//        binding.viewPager2.setOffscreenPageLimit(3);
//        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        binding.viewPager2.setPageTransformer(compositePageTransformer);
//    }
//
//    private void initBanners() {
//        DatabaseReference myref = database.getReference("Banner");
//        binding.progressBarBanner.setVisibility(View.VISIBLE);
//        ArrayList<SliderItems> items = new ArrayList<>();
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue : snapshot.getChildren()){
//                        items.add(issue.getValue(SliderItems.class));
//                    }
//                    banners(items);
//                    binding.progressBarBanner.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                binding.progressBarBanner.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Failed to load banners.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.travelapp.Adapter.CategoryAdapter;
import com.example.travelapp.Adapter.PopularAdapter;
import com.example.travelapp.Adapter.RecommendedAdapter;
import com.example.travelapp.Adapter.SliderAdapter;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.Domain.Location;
import com.example.travelapp.Domain.SliderItems;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {
    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app");

        // Thiết lập thông tin và các sự kiện click
        setUserInfo();
        setupClickListeners();

        // Tải dữ liệu từ Firebase
        initLocations();
        initBanners();
        initCategory();
        initPopular();
        initRecommended();
    }

    private void setupClickListeners() {
        // Listener cho nút Đăng xuất
        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Listener cho Bottom Menu
        binding.bottomMenu.setItemSelected(R.id.exporer, true); // Đặt mục Home làm mặc định
        binding.bottomMenu.setOnItemSelectedListener(i -> {
            if (i == R.id.cart) { // ID của mục Profile trong file menu của bạn
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        // Listener cho nút Search
        binding.button.setOnClickListener(v -> {
            String searchText = binding.editTextText.getText().toString().trim();
            if (searchText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a destination to search", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", searchText);
                startActivity(intent);
            }
        });

        // Listener cho hai nút "See All"
        binding.seeAllPopular.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            intent.putExtra("CATEGORY_NODE", "Popular"); // Gửi tên node Firebase
            intent.putExtra("CATEGORY_TITLE", "Popular Destinations");
            startActivity(intent);
        });

        binding.seeAllRecommendedBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            intent.putExtra("CATEGORY_NODE", "Item"); // Gửi tên node Firebase
            intent.putExtra("CATEGORY_TITLE", "Recommended");
            startActivity(intent);
        });
        binding.bottomMenu.setOnItemSelectedListener(i -> {
            if (i == R.id.cart) { // ID của Profile
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (i == R.id.bookmark) { // <-- THÊM MỚI
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
            }
        });
    }

    private void setUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                binding.welcomeTxt.setText("Welcome " + displayName + "!");
            } else {
                binding.welcomeTxt.setText("Welcome!");
            }
        }
    }

    private void initRecommended() {
        DatabaseReference myref = database.getReference("Item");
        ArrayList<Item> list = new ArrayList<>();
        myref.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() { // Giới hạn 10 item
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        Item item = issuee.getValue(Item.class);
                        if (item != null) {
                            item.setId(issuee.getKey());
                            list.add(item);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new RecommendedAdapter(list);
                        binding.recyclerViewRecommended.setAdapter(adapter);
                    }
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void initPopular() {
        DatabaseReference myref = database.getReference("Popular");
        ArrayList<Item> list = new ArrayList<>();
        myref.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() { // Giới hạn 10 item
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        Item item = issuee.getValue(Item.class);
                        if (item != null) {
                            item.setId(issuee.getKey());
                            list.add(item);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new PopularAdapter(list);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void initCategory() {
        DatabaseReference myref = database.getReference("Category");
        ArrayList<Category> list = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        Category category = issuee.getValue(Category.class);
                        if (category != null) {
                            try {
                                category.setId(Integer.parseInt(issuee.getKey())); // Lấy ID của category
                                list.add(category);
                            } catch (NumberFormatException e) {
                                // Bỏ qua nếu key không phải số
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        // Truyền 'this' vì MainActivity đã implements OnCategoryClickListener
                        RecyclerView.Adapter adapter = new CategoryAdapter(list, MainActivity.this);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void initLocations() {
        DatabaseReference myref = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        list.add(issuee.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2.setPageTransformer(compositePageTransformer);
    }

    private void initBanners() {
        DatabaseReference myref = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load banners.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm này được gọi từ CategoryAdapter khi một danh mục được nhấn
    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(this, ItemListActivity.class);
        // Gửi ID và tên của category sang màn hình danh sách
        intent.putExtra("CATEGORY_ID", String.valueOf(category.getId()));
        intent.putExtra("CATEGORY_TITLE", category.getName());
        startActivity(intent);
    }

}
package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
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
import com.google.firebase.auth.FirebaseUser; // Import FirebaseUser
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth; // Thêm biến FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth
        database = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app");

        // --- BẮT ĐẦU CODE MỚI ---
        setUserInfo();
        setupLogoutButton();
        // --- KẾT THÚC CODE MỚI ---

        initLocations();
        initBanners();
        initCategory();
        initPopular();
        initRecommended();
    }

    // --- BẮT ĐẦU CODE MỚI ---
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

    private void setupLogoutButton() {
        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    // --- KẾT THÚC CODE MỚI ---

    // ... các phương thức init... của bạn giữ nguyên ...
    private void initRecommended() {

        DatabaseReference myref = database.getReference("Item");
        ArrayList<Item> list = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        list.add(issuee.getValue(Item.class));
                    }
                    if(!list.isEmpty()){
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new RecommendedAdapter(list);
                        binding.recyclerViewRecommended.setAdapter(adapter);
                    }
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPopular() {

        DatabaseReference myref = database.getReference("Popular");
        ArrayList<Item> list = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        list.add(issuee.getValue(Item.class));
                    }
                    if(!list.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new PopularAdapter(list);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initCategory() {

        DatabaseReference myref = database.getReference("Category");
        ArrayList<Category> list=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        list.add(issuee.getValue(Category.class));
                    }
                    if(!list.isEmpty()){
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initLocations() {

        DatabaseReference myref = database.getReference("Location");
        ArrayList<Location> list=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issuee : snapshot.getChildren()) {
                        list.add(issuee.getValue(Location.class));
                    }

                    ArrayAdapter<Location> adapter=new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(
            ArrayList<SliderItems> items
    ) {
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
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

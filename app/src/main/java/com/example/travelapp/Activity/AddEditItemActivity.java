//package com.example.travelapp.Activity;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.travelapp.Domain.Item;
//import com.example.travelapp.databinding.ActivityAddEditItemBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class AddEditItemActivity extends AppCompatActivity {
//    private ActivityAddEditItemBinding binding;
//    private DatabaseReference databaseReference;
//    private Item currentItem; // Item hiện tại nếu ở chế độ sửa
//    private boolean isEditMode = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAddEditItemBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Khởi tạo tham chiếu đến node "Item" trong Firebase
//        databaseReference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Item");
//
//        // Kiểm tra xem có dữ liệu item được gửi qua Intent không
//        if (getIntent().hasExtra("itemObject")) {
//            isEditMode = true;
//            currentItem = (Item) getIntent().getSerializableExtra("itemObject");
//            binding.toolbar.setTitle("Edit Trip"); // Đổi tiêu đề nếu là chế độ sửa
//            populateFields(); // Điền dữ liệu cũ vào các ô
//        } else {
//            isEditMode = false;
//            binding.toolbar.setTitle("Add New Trip"); // Tiêu đề cho chế độ thêm mới
//        }
//
//        // Thiết lập sự kiện click cho các nút
//        setupClickListeners();
//    }
//
//    // Điền dữ liệu của item vào các ô EditText
//    private void populateFields() {
//        if (currentItem == null) return;
//
//        binding.titleEdt.setText(currentItem.getTitle());
//        binding.addressEdt.setText(currentItem.getAddress());
//        binding.descriptionEdt.setText(currentItem.getDescription());
//        binding.picUrlEdt.setText(currentItem.getPic());
//        binding.priceEdt.setText(String.valueOf(currentItem.getPrice()));
//        binding.scoreEdt.setText(String.valueOf(currentItem.getScore()));
//        binding.bedEdt.setText(String.valueOf(currentItem.getBed()));
//        binding.distanceEdt.setText(currentItem.getDistance());
//        binding.durationEdt.setText(currentItem.getDuration());
//        binding.dateTourEdt.setText(currentItem.getDateTour());
//        binding.timeTourEdt.setText(currentItem.getTimeTour());
//        binding.guideNameEdt.setText(currentItem.getTourGuideName());
//        binding.guidePhoneEdt.setText(currentItem.getTourGuidePhone());
//        binding.guidePicUrlEdt.setText(currentItem.getTourGuidePic());
//    }
//
//    // Thiết lập sự kiện cho các nút
//    private void setupClickListeners() {
//        // Sự kiện cho nút Back trên Toolbar
//        binding.toolbar.setNavigationOnClickListener(v -> finish());
//
//        // Sự kiện cho nút Save
//        binding.saveBtn.setOnClickListener(v -> saveItem());
//    }
//
//    // Hàm chính để lưu dữ liệu
//    private void saveItem() {
//        // Lấy dữ liệu từ tất cả các ô EditText
//        String title = binding.titleEdt.getText().toString().trim();
//        String address = binding.addressEdt.getText().toString().trim();
//        String description = binding.descriptionEdt.getText().toString().trim();
//        String picUrl = binding.picUrlEdt.getText().toString().trim();
//        String priceStr = binding.priceEdt.getText().toString().trim();
//        String scoreStr = binding.scoreEdt.getText().toString().trim();
//        String bedStr = binding.bedEdt.getText().toString().trim();
//        String distance = binding.distanceEdt.getText().toString().trim();
//        String duration = binding.durationEdt.getText().toString().trim();
//        String dateTour = binding.dateTourEdt.getText().toString().trim();
//        String timeTour = binding.timeTourEdt.getText().toString().trim();
//        String guideName = binding.guideNameEdt.getText().toString().trim();
//        String guidePhone = binding.guidePhoneEdt.getText().toString().trim();
//        String guidePicUrl = binding.guidePicUrlEdt.getText().toString().trim();
//
//        // Kiểm tra các trường bắt buộc
//        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(priceStr)) {
//            Toast.makeText(this, "Title and Price cannot be empty.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Chuyển đổi chuỗi sang số, xử lý lỗi nếu có
//        int price = 0;
//        double score = 0.0;
//        int bed = 0;
//        try {
//            price = Integer.parseInt(priceStr);
//            if (!scoreStr.isEmpty()) score = Double.parseDouble(scoreStr);
//            if (!bedStr.isEmpty()) bed = Integer.parseInt(bedStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Please enter valid numbers for Price, Score, and Bed.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Hiển thị ProgressBar
//        binding.progressBar.setVisibility(View.VISIBLE);
//
//        // Tạo đối tượng Item mới với dữ liệu đã thu thập
//        Item itemToSave = new Item();
//        itemToSave.setTitle(title);
//        itemToSave.setAddress(address);
//        itemToSave.setDescription(description);
//        itemToSave.setPic(picUrl);
//        itemToSave.setPrice(price);
//        itemToSave.setScore(score);
//        itemToSave.setBed(bed);
//        itemToSave.setDistance(distance);
//        itemToSave.setDuration(duration);
//        itemToSave.setDateTour(dateTour);
//        itemToSave.setTimeTour(timeTour);
//        itemToSave.setTourGuideName(guideName);
//        itemToSave.setTourGuidePhone(guidePhone);
//        itemToSave.setTourGuidePic(guidePicUrl);
//
//        // Tiến hành lưu lên Firebase
//        if (isEditMode) {
//            // Chế độ Sửa: Cập nhật dữ liệu cho item đã có bằng ID
//            databaseReference.child(currentItem.getId()).setValue(itemToSave)
//                    .addOnSuccessListener(aVoid -> {
//                        binding.progressBar.setVisibility(View.GONE);
//                        Toast.makeText(AddEditItemActivity.this, "Trip updated successfully!", Toast.LENGTH_SHORT).show();
//                        finish(); // Đóng màn hình và quay lại danh sách
//                    })
//                    .addOnFailureListener(e -> {
//                        binding.progressBar.setVisibility(View.GONE);
//                        Toast.makeText(AddEditItemActivity.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            // Chế độ Thêm mới: Tạo một ID mới và lưu dữ liệu
//            String newItemId = databaseReference.push().getKey();
//            if (newItemId != null) {
//                databaseReference.child(newItemId).setValue(itemToSave)
//                        .addOnSuccessListener(aVoid -> {
//                            binding.progressBar.setVisibility(View.GONE);
//                            Toast.makeText(AddEditItemActivity.this, "Trip added successfully!", Toast.LENGTH_SHORT).show();
//                            finish(); // Đóng màn hình và quay lại danh sách
//                        })
//                        .addOnFailureListener(e -> {
//                            binding.progressBar.setVisibility(View.GONE);
//                            Toast.makeText(AddEditItemActivity.this, "Failed to add: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//            }
//        }
//    }
//}


package com.example.travelapp.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivityAddEditItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddEditItemActivity extends AppCompatActivity {
    private ActivityAddEditItemBinding binding;
    private DatabaseReference itemsRef;
    private DatabaseReference categoriesRef;

    private Item currentItem;
    private boolean isEditMode = false;

    // Danh sách để quản lý dữ liệu cho Spinner Category
    private List<Category> categoryList = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các tham chiếu tới Firebase
        String firebaseUrl = "https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app";
        itemsRef = FirebaseDatabase.getInstance(firebaseUrl).getReference("Item");
        categoriesRef = FirebaseDatabase.getInstance(firebaseUrl).getReference("Category");

        // Thiết lập Spinner trước khi tải dữ liệu
        setupCategorySpinner();
        // Bắt đầu tải danh sách danh mục
        loadCategories();

        // Kiểm tra xem đây là màn hình Thêm mới hay Sửa
        if (getIntent().hasExtra("itemObject")) {
            isEditMode = true;
            currentItem = (Item) getIntent().getSerializableExtra("itemObject");
            binding.toolbar.setTitle("Edit Trip");
            populateFields();
        } else {
            isEditMode = false;
            binding.toolbar.setTitle("Add New Trip");
        }

        // Thiết lập sự kiện cho các nút
        setupClickListeners();
    }

    private void setupCategorySpinner() {
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(categoryAdapter);
    }

    private void loadCategories() {
        binding.progressBar.setVisibility(View.VISIBLE); // Hiển thị loading khi tải categories
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                categoryNames.clear();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        try {
                            // Firebase không tự lưu key, ta phải gán thủ công
                            category.setId(Integer.parseInt(categorySnapshot.getKey()));
                            categoryList.add(category);
                            categoryNames.add(category.getName());
                        } catch (NumberFormatException e) {
                            // Bỏ qua nếu key không phải là số
                        }
                    }
                }
                categoryAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE); // Ẩn loading

                // Nếu đang ở chế độ sửa, chọn sẵn category của item
                if (isEditMode && currentItem != null && currentItem.getCategoryId() != null) {
                    selectCurrentCategoryInSpinner();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddEditItemActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectCurrentCategoryInSpinner() {
        for (int i = 0; i < categoryList.size(); i++) {
            if (String.valueOf(categoryList.get(i).getId()).equals(currentItem.getCategoryId())) {
                binding.categorySpinner.setSelection(i);
                break;
            }
        }
    }

    private void populateFields() {
        if (currentItem == null) return;
        binding.titleEdt.setText(currentItem.getTitle());
        binding.addressEdt.setText(currentItem.getAddress());
        binding.descriptionEdt.setText(currentItem.getDescription());
        binding.picUrlEdt.setText(currentItem.getPic());
        binding.priceEdt.setText(String.valueOf(currentItem.getPrice()));
        binding.scoreEdt.setText(String.valueOf(currentItem.getScore()));
        binding.bedEdt.setText(String.valueOf(currentItem.getBed()));
        binding.distanceEdt.setText(currentItem.getDistance());
        binding.durationEdt.setText(currentItem.getDuration());
        binding.dateTourEdt.setText(currentItem.getDateTour());
        binding.timeTourEdt.setText(currentItem.getTimeTour());
        binding.guideNameEdt.setText(currentItem.getTourGuideName());
        binding.guidePhoneEdt.setText(currentItem.getTourGuidePhone());
        binding.guidePicUrlEdt.setText(currentItem.getTourGuidePic());
    }

    private void setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.saveBtn.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        // Thu thập dữ liệu
        String title = binding.titleEdt.getText().toString().trim();
        String address = binding.addressEdt.getText().toString().trim();
        String description = binding.descriptionEdt.getText().toString().trim();
        String picUrl = binding.picUrlEdt.getText().toString().trim();
        String priceStr = binding.priceEdt.getText().toString().trim();
        String scoreStr = binding.scoreEdt.getText().toString().trim();
        String bedStr = binding.bedEdt.getText().toString().trim();
        String distance = binding.distanceEdt.getText().toString().trim();
        String duration = binding.durationEdt.getText().toString().trim();
        String dateTour = binding.dateTourEdt.getText().toString().trim();
        String timeTour = binding.timeTourEdt.getText().toString().trim();
        String guideName = binding.guideNameEdt.getText().toString().trim();
        String guidePhone = binding.guidePhoneEdt.getText().toString().trim();
        String guidePicUrl = binding.guidePicUrlEdt.getText().toString().trim();

        // Kiểm tra các trường bắt buộc
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Title and Price cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy Category ID từ Spinner
        int selectedPos = binding.categorySpinner.getSelectedItemPosition();
        if (selectedPos < 0 || selectedPos >= categoryList.size()) {
            Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
            return;
        }
        String categoryId = String.valueOf(categoryList.get(selectedPos).getId());

        // Chuyển đổi kiểu dữ liệu một cách an toàn
        int price;
        double score = 0.0;
        int bed = 0;
        try {
            price = Integer.parseInt(priceStr);
            if (!scoreStr.isEmpty()) score = Double.parseDouble(scoreStr);
            if (!bedStr.isEmpty()) bed = Integer.parseInt(bedStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for Price, Score, and Bed.", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        // Tạo đối tượng Item để lưu
        Item itemToSave = new Item();
        itemToSave.setTitle(title);
        itemToSave.setAddress(address);
        itemToSave.setDescription(description);
        itemToSave.setPic(picUrl);
        itemToSave.setPrice(price);
        itemToSave.setScore(score);
        itemToSave.setBed(bed);
        itemToSave.setDistance(distance);
        itemToSave.setDuration(duration);
        itemToSave.setDateTour(dateTour);
        itemToSave.setTimeTour(timeTour);
        itemToSave.setTourGuideName(guideName);
        itemToSave.setTourGuidePhone(guidePhone);
        itemToSave.setTourGuidePic(guidePicUrl);
        itemToSave.setCategoryId(categoryId); // Gán Category ID

        // Xác định ID và tiến hành lưu
        String idToSave = isEditMode ? currentItem.getId() : itemsRef.push().getKey();

        if (idToSave == null) {
            Toast.makeText(this, "Could not create or find item ID.", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.GONE);
            return;
        }

        itemsRef.child(idToSave).setValue(itemToSave)
                .addOnSuccessListener(aVoid -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddEditItemActivity.this, isEditMode ? "Trip updated successfully!" : "Trip added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddEditItemActivity.this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
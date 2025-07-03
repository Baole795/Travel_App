package com.example.travelapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Domain.User;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderUserItemBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private DatabaseReference databaseReference;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.databaseReference = FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderUserItemBinding binding = ViewholderUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.binding.userNameTxt.setText(user.getLastName() + " " + user.getFirstName());
        holder.binding.userEmailTxt.setText(user.getEmail());

        // Cập nhật giao diện cho vai trò
        if ("admin".equals(user.getRole())) {
            holder.binding.roleTxt.setText("Admin");
            holder.binding.roleTxt.setBackgroundResource(R.drawable.purple_bg);
            holder.binding.roleTxt.setTextColor(Color.WHITE);
        } else {
            holder.binding.roleTxt.setText("User");
            holder.binding.roleTxt.setBackgroundResource(R.drawable.grey_bg2);
            holder.binding.roleTxt.setTextColor(Color.BLACK);
        }

        // Thiết lập trạng thái khóa, bỏ qua listener để tránh vòng lặp
        holder.binding.lockSwitch.setOnCheckedChangeListener(null);
        holder.binding.lockSwitch.setChecked(user.isLocked());
        // Gán lại listener sau khi đã set trạng thái
        holder.binding.lockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateUserLockStatus(user.getUid(), isChecked);
        });

        // Thiết lập sự kiện đổi vai trò
        holder.binding.changeRoleBtn.setOnClickListener(v -> {
            showChangeRoleDialog(user);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void updateUserLockStatus(String uid, boolean isLocked) {
        databaseReference.child(uid).child("locked").setValue(isLocked)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "User status updated.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showChangeRoleDialog(User user) {
        final String[] roles = {"user", "admin"};
        int currentRoleIndex = "admin".equals(user.getRole()) ? 1 : 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Role for " + user.getFirstName())
                .setSingleChoiceItems(roles, currentRoleIndex, null)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    String newRole = roles[selectedPosition];
                    if (!newRole.equals(user.getRole())) {
                        updateUserRole(user.getUid(), newRole);
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateUserRole(String uid, String newRole) {
        databaseReference.child(uid).child("role").setValue(newRole)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "User role updated.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update role: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ViewholderUserItemBinding binding;

        public UserViewHolder(ViewholderUserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
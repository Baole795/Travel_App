package com.example.travelapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.travelapp.Activity.AddEditItemActivity;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ViewholderItemAdminBinding;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ItemAdminAdapter extends RecyclerView.Adapter<ItemAdminAdapter.ViewHolder> {
    private ArrayList<Item> items;
    private Context context;

    public ItemAdminAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderItemAdminBinding binding = ViewholderItemAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.binding.itemTitleTxt.setText(item.getTitle());
        holder.binding.itemPriceTxt.setText("$" + item.getPrice());
        Glide.with(context).load(item.getPic()).into(holder.binding.itemPic);

        holder.binding.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditItemActivity.class);
            intent.putExtra("itemObject", item);
            context.startActivity(intent);
        });

        holder.binding.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete '" + item.getTitle() + "'?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteItem(item.getId()))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteItem(String itemId) {
        FirebaseDatabase.getInstance("https://travel-app-12-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Item").child(itemId)
                .removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete item.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderItemAdminBinding binding;
        public ViewHolder(ViewholderItemAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
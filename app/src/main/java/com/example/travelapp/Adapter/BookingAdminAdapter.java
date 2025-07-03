//package com.example.travelapp.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.example.travelapp.Domain.Booking;
//import com.example.travelapp.R;
//import com.example.travelapp.databinding.ViewholderBookingAdminBinding;
//import java.util.ArrayList;
//
//public class BookingAdminAdapter extends RecyclerView.Adapter<BookingAdminAdapter.ViewHolder> {
//    private Context context;
//    private ArrayList<Booking> list;
//
//    public BookingAdminAdapter(Context context, ArrayList<Booking> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ViewholderBookingAdminBinding binding = ViewholderBookingAdminBinding.inflate(LayoutInflater.from(context), parent, false);
//        return new ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Booking booking = list.get(position);
//
//        holder.binding.itemTitleTxt.setText(booking.getItemTitle());
//        holder.binding.itemPriceTxt.setText("$" + booking.getItemPrice());
//
//        holder.binding.customerNameTxt.setText(booking.getUserFullName());
//        holder.binding.customerEmailTxt.setText(booking.getUserEmail());
//        holder.binding.customerPhoneTxt.setText(booking.getUserPhoneNumber());
//
//        holder.binding.bookingDateTxt.setText(booking.getBookingDate());
//        holder.binding.statusTxt.setText(booking.getStatus());
//
//        Glide.with(context)
//                .load(booking.getItemPicUrl())
//                .placeholder(R.drawable.grey_bg)
//                .error(R.drawable.grey_bg)
//                .into(holder.binding.itemPic);
//
//        // Thay đổi màu sắc của status
//        switch (booking.getStatus().toLowerCase()) {
//            case "confirmed":
//                holder.binding.statusTxt.setBackgroundResource(R.drawable.green_bg); // Tạo một drawable green_bg
//                break;
//            case "cancelled":
//                holder.binding.statusTxt.setBackgroundResource(R.drawable.red_bg); // Tạo một drawable red_bg
//                break;
//            default: // "Pending"
//                holder.binding.statusTxt.setBackgroundResource(R.drawable.orange_bg); // Tạo một drawable orange_bg
//                break;
//        }
//
//        // Bắt sự kiện click cho nút Manage
//        holder.binding.manageBtn.setOnClickListener(v -> {
//            // Sau này sẽ mở dialog để cập nhật trạng thái
//            Toast.makeText(context, "Manage booking: " + booking.getBookingId(), Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ViewholderBookingAdminBinding binding;
//        public ViewHolder(ViewholderBookingAdminBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
//}

package com.example.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderBookingAdminBinding;

import java.util.ArrayList;

public class BookingAdminAdapter extends RecyclerView.Adapter<BookingAdminAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Booking> list;

    public BookingAdminAdapter(Context context, ArrayList<Booking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBookingAdminBinding binding = ViewholderBookingAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = list.get(position);
        if (booking == null) return;

        // Gán dữ liệu Item
        holder.binding.itemTitleTxt.setText(booking.getItemTitle());
        holder.binding.itemPriceTxt.setText("$" + booking.getItemPrice());

        // ▼▼▼ THÊM MỚI: GÁN DỮ LIỆU DURATION VÀ BED ▼▼▼
        holder.binding.itemDurationTxt.setText(booking.getItemDuration());
        holder.binding.itemBedTxt.setText(booking.getItemBed() + " Beds");

        // Gán dữ liệu Customer
        holder.binding.customerNameTxt.setText(booking.getUserFullName());
        holder.binding.customerEmailTxt.setText(booking.getUserEmail());
        holder.binding.customerPhoneTxt.setText(booking.getUserPhoneNumber());
        holder.binding.bookingDateTxt.setText("Booked on: " + booking.getBookingDate());
        holder.binding.statusTxt.setText(booking.getStatus());

        // Tải hình ảnh
        Glide.with(context)
                .load(booking.getItemPicUrl())
                .placeholder(R.drawable.grey_bg)
                .error(R.drawable.grey_bg)
                .into(holder.binding.itemPic);

        // Thay đổi màu sắc của status
        if (booking.getStatus() != null) {
            switch (booking.getStatus().toLowerCase()) {
                case "confirmed":
                    holder.binding.statusTxt.setBackgroundResource(R.drawable.green_bg);
                    break;
                case "cancelled":
                    holder.binding.statusTxt.setBackgroundResource(R.drawable.red_bg);
                    break;
                default:
                    holder.binding.statusTxt.setBackgroundResource(R.drawable.orange_bg);
                    break;
            }
        }

        holder.binding.manageBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Manage booking: " + booking.getBookingId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderBookingAdminBinding binding;
        public ViewHolder(ViewholderBookingAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
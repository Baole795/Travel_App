package com.example.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.travelapp.Domain.Slideritems;
import com.example.travelapp.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewholder> {
    private ArrayList<Slideritems> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runable=new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(ArrayList<Slideritems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context= parent.getContext();
        return new SliderViewholder(LayoutInflater.from(context)
                .inflate(R.layout.slider_item_container,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
    public class SliderViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
        void setImage(SliderItems.getUrl())

    }
}

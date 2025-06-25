package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityDetailBinding;
import com.example.travelapp.databinding.ActivityMainBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Item object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
        }

    private void setVariable() {
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());
        binding.addressTxt.setText(object.getAddress());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText(""+object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingBar.setRating((float) object.getScore());
        binding.ratingTxt.setText(object.getScore()+" Rating");

    Glide.with(DetailActivity.this)
            .load(object.getPic())
            .into(binding.pic);

    binding.addToCartBtn.setOnClickListener(view -> {
        Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
        intent.putExtra("object", object);
        startActivity(intent);
    });
    }

    private void getIntentExtra() {
        object = (Item) getIntent().getSerializableExtra("object");
    }
}
package com.example.travelapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityDetailBinding;
import com.example.travelapp.databinding.ActivityTicketBinding;

public class TicketActivity extends AppCompatActivity {
    @NonNull ActivityTicketBinding binding;
    private Item object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        getVatiable();

    }

    private void getVatiable() {

        Glide.with(TicketActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        Glide.with(TicketActivity.this)
                .load(object.getTourGuidePic())
                .into(binding.profile);

        binding.titleTxt.setText(object.getTitle());
        binding.backBtn.setOnClickListener(v-> finish());
        binding.durationTxt.setText(object.getDuration());
        binding.tourGuideNameTxt.setText(object.getTourGuideName());
        binding.timeTxt.setText(object.getTimeTour());
        binding.timeTxttourGuideTxt.setText(object.getDateTour());

        binding.callBtn.setOnClickListener(view-> {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("tel:" + object.getTourGuidePhone()));
                sendIntent.putExtra("sms_body", "type your message");
                startActivity(sendIntent);
        });

        binding.messageBtn.setOnClickListener(view -> {
            String phone = object.getTourGuidePhone();
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.fromParts("tel", phone, null));
            startActivity(intent);
        });

    }

    private void getIntentExtra() {
        object=(Item) getIntent().getSerializableExtra("object");
    }
}
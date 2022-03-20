package com.example.excamera2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class SaveActivity extends AppCompatActivity {
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Intent intent = getIntent();
        Bitmap bitmap = intent.getParcelableExtra("bitmap");
        iv = findViewById(R.id.save_img);
        iv.setImageBitmap(bitmap);
    }
}
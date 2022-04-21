package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Photos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Album gotten = (Album) getIntent().getSerializableExtra("album");
        Toast.makeText(Photos.this, "received item: " + gotten.getAlbum(), Toast.LENGTH_SHORT).show();
    }
}
package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Photos extends AppCompatActivity {
    Album current;
    ArrayList<Photo> photos;
    ArrayList<Album> update;
    ArrayList<String> urls;
    ListView listView;
    ImageViewAdapter arrayAdapter;
    Button backButton;
    Button editButton;
    int save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        current = (Album) getIntent().getSerializableExtra("album");
        update = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        photos = current.getPhotos();
        save = getIntent().getIntExtra("index", 0);

        listView = findViewById(R.id.PhotoEditList);
        arrayAdapter = new ImageViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, photos);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // go to carousel
                Intent intent = new Intent(Photos.this, PhotoDisplay.class);
                intent.putExtra("photo", photos.get(i));
                intent.putExtra("album", current);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });
        configureEditButton();
        configureBackButton();
    }

    private void configureEditButton() {
        editButton = (Button) findViewById(R.id.button9);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Photos.this, Edit.class);
                intent.putExtra("albums", update);
                intent.putExtra("album", current);
                intent.putExtra("index", save);
                startActivity(intent);
            }
        });
    }

    private void configureBackButton() {
        backButton = (Button) findViewById(R.id.button10);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        arrayAdapter = new ImageViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, current.getPhotos());
        listView.setAdapter(arrayAdapter);
    }

    private ArrayList<Bitmap> fetchImages(ArrayList<String> urls){
        ArrayList<Bitmap> images = new ArrayList<>();
        for(String u : urls){
            Bitmap bmp = BitmapFactory.decodeFile(u);
            images.add(bmp);
        }
        return images;
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(update);
        editor.putString("album list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("album list", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        update = gson.fromJson(json, type);
        if (update == null) {
            update = new ArrayList<>();
        }
        current = update.get(save);
        photos = current.getPhotos();
    }
}
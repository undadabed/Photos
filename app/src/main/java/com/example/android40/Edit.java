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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Edit extends AppCompatActivity {
    Album current;
    ArrayList<Photo> photos;
    ArrayList<Album> update;
    ArrayList<String> urls;
    ListView listView;
    ImageViewAdapter arrayAdapter;
    Button backButton;
    Button deleteButton;
    Button editButton;
    Button moveButton;
    Photo selected;
    Integer index;
    int save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        listView = findViewById(R.id.PhotoEditList);

        current = (Album) getIntent().getSerializableExtra("album");
        update = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        photos = current.getPhotos();
        selected = null;
        save = getIntent().getIntExtra("index", 0);
        arrayAdapter = new ImageViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, photos);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = photos.get(i);
                index = i;
                Toast.makeText(Edit.this, photos.get(i) + " selected", Toast.LENGTH_SHORT).show();
            }
        });

        configureEditButton();
        configureDeleteButton();
        configureBackButton();
        configureFloatingButton();
        configureMoveButton();
    }

    private void configureFloatingButton() {
        FloatingActionButton addButton = findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(Edit.this, PhotoAdd.class);
                intent.putExtra("albums", update);
                intent.putExtra("album", current);
                intent.putExtra("index", save);
                startActivity(intent);
            }
        });
    }

    private void configureMoveButton(){
        moveButton = findViewById(R.id.move);
        moveButton.setOnClickListener((v)->{
            Intent intent = new Intent(Edit.this, MovePhoto.class);
            intent.putExtra("albums", update);
            intent.putExtra("album", current);
            intent.putExtra("photo", selected);
            intent.putExtra("photoIndex", index);
            startActivity(intent);
        });
    }

    private void configureEditButton() {
        editButton = (Button) findViewById(R.id.button3);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == null) {
                    Toast.makeText(Edit.this, "No photo selected to edit", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Edit.this, PhotoEdit.class);
                    intent.putExtra("albums", update);
                    intent.putExtra("album", current);
                    intent.putExtra("photo", selected);
                    intent.putExtra("index", save);
                    intent.putExtra("photoIndex", index);
                    startActivity(intent);
                }
            }
        });
    }

    private void configureDeleteButton() {
        deleteButton = (Button) findViewById(R.id.button4);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == null) {
                    Toast.makeText(Edit.this, "No photo selected to delete", Toast.LENGTH_SHORT).show();
                }
                else {
                    arrayAdapter.removeItem(index);
                    saveData();
                    index = -1;
                    selected = null;
                }
            }
        });
    }

    private void configureBackButton() {
        backButton = (Button) findViewById(R.id.button5);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        current.setPhotos(photos);
        update.set(save, current);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        photos = current.getPhotos();
        arrayAdapter = new ImageViewAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, photos);
        listView.setAdapter(arrayAdapter);
    }
}
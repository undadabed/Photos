package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MovePhoto extends AppCompatActivity {

    //UI Elements
    Button moveButton;
    Button cancelButton;
    ImageView moveImage;
    ListView albumList;
    ArrayAdapter albumAdapter;
    TextView currentAlbumTag;

    //Objects
    ArrayList<Album> albums;
    Album currentAlbum;
    Album selectedAlbum;
    Photo selected;
    ArrayList<Photo> photos;
    int photoIndex;
    int albumIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_photo);

        //Getting Objects
        albums = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        currentAlbum = (Album) getIntent().getSerializableExtra("album");
        photoIndex = getIntent().getIntExtra("photoIndex", 0);
        selected = (Photo) getIntent().getSerializableExtra("photo");
        selectedAlbum = null;
        photos = null;
        albumIndex = -1;
        //Getting UI
        moveImage = findViewById(R.id.moveImage);
        moveButton = findViewById(R.id.moveButton);
        cancelButton = findViewById(R.id.cancelButton);
        currentAlbumTag = findViewById(R.id.currentAlbumTag);
        albumList = findViewById(R.id.albumList);
        albumAdapter = new ArrayAdapter(MovePhoto.this, android.R.layout.simple_list_item_1, albums);
        //Setting up functionality
        currentAlbumTag.setText(String.format("Current Album: %s", currentAlbum.getAlbum()));
        albumList.setAdapter(albumAdapter);
       // moveImage.setLayoutParams(new ConstraintLayout.LayoutParams(300, 300));
        //moveImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader loader = new ImageLoader(moveImage);
        loader.execute(selected.getPath());
        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAlbum = albums.get(i);
                photos = selectedAlbum.getPhotos();
                albumIndex = i;
                Toast.makeText(MovePhoto.this, albums.get(i).getAlbum() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
        configureMoveButton();
        configureBackButton();

    }

    private void configureMoveButton(){
        moveButton.setOnClickListener((v)->{
            if(selectedAlbum == null || albumIndex == -1){
                Toast.makeText(MovePhoto.this, "Please Select and Album", Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectedAlbum.equals(currentAlbum)){
                Toast.makeText(MovePhoto.this, "Can not move to same album", Toast.LENGTH_SHORT).show();
                return;
            }
            if(photos.contains(selected)){
                Toast.makeText(MovePhoto.this, "Photo Already in Album", Toast.LENGTH_SHORT).show();
                return;
            }
            photos.add(selected);
            saveData();
            finish();
        });
    }

    private void configureBackButton(){
        cancelButton.setOnClickListener((v)->{
            finish();
        });
    }

    private void saveData() {
        selectedAlbum.setPhotos(photos);
        albums.set(albumIndex, selectedAlbum);
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(albums);
        editor.putString("album list", json);
        editor.apply();
    }





}
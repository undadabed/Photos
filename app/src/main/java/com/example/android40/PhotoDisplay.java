package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhotoDisplay extends AppCompatActivity {

    //UI Elements
    ImageButton leftButton;
    ImageButton rightButton;
    Button back;
    TextView caption;
    ImageView imageDisplay;
    ListView displayPtags;
    ListView displayLtags;
    ArrayAdapter personAdapter;
    ArrayAdapter locationAdapter;

    //Objects
    Photo photo;
    ArrayList<Photo> photos;
    int photoIndex;
    int size;
    ArrayList<PersonTag> ptags;
    ArrayList<LocationTag> ltags;
    ImageLoader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        //Get UI
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        caption = findViewById(R.id.caption);
        imageDisplay = findViewById(R.id.imageDisplay);
        displayLtags = findViewById(R.id.displayLtags);
        displayPtags = findViewById(R.id.displayPtags);
        back = findViewById(R.id.back);
        //Get objects
        photo = (Photo) getIntent().getSerializableExtra("photo");
        photoIndex = getIntent().getIntExtra("index", 0);
        photos = (ArrayList<Photo>) getIntent().getSerializableExtra("photos");
        size = photos.size();
        loader = new ImageLoader(imageDisplay);
        //Load Information
        personAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, photo.getPersonTags());
        displayPtags.setAdapter(personAdapter);
        locationAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, photo.getLocationTags());
        displayLtags.setAdapter(locationAdapter);
        caption.setText(photo.getCaption());
        loader.execute(photo.getPath());
        if(photoIndex == 0)
            leftButton.setEnabled(false);
        if(photoIndex == size -1)
            rightButton.setEnabled(false);
        configureLeftButton();
        configureRightButton();
        configureBackButton();
    }

    private void configureBackButton(){
        back.setOnClickListener((v)->{
            finish();
        });
    }

    private void configureLeftButton(){
        leftButton.setOnClickListener((v)->{
            if(photoIndex > 0){
                photo = photos.get(--photoIndex);
                loader = new ImageLoader(imageDisplay);
                loader.execute(photo.getPath());
                caption.setText(photo.getCaption());
                updateTags();
                if(photoIndex==0){
                    leftButton.setEnabled(false);
                }
                if(!rightButton.isEnabled())
                    rightButton.setEnabled(true);
            }
        });
    }

    private void configureRightButton(){
        rightButton.setOnClickListener((v)->{
            if(photoIndex < size - 1){
                photo = photos.get(++photoIndex);
                loader = new ImageLoader(imageDisplay);
                loader.execute(photo.getPath());
                caption.setText(photo.getCaption());
                updateTags();
                if(photoIndex == size - 1){
                    rightButton.setEnabled(false);
                }
                if(!leftButton.isEnabled())
                    leftButton.setEnabled(true);
            }
        });
    }

    private void updateTags(){
        personAdapter.clear();
        personAdapter.addAll(photo.getPersonTags());
        locationAdapter.clear();
        locationAdapter.addAll(photo.getLocationTags());
    }





}
package com.example.android40;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class PhotoEdit extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView imageView;
    String filePath;
    Button updateButton;
    Button deleteButton;
    Button addPersonButton;
    Button addLocationButton;
    EditText tagValue;
    ListView personTagList;
    ListView locationTagList;
    EditText editText;
    Album current;
    ArrayList<Album> update;
    ArrayList<Photo> photos;
    ArrayList<PersonTag> ptags;
    ArrayList<LocationTag> ltags;
    ArrayAdapter personTags;
    ArrayAdapter locationTags;
    Photo selected;
    PersonTag selectedTagP;
    LocationTag selectedTagL;
    int save;
    int photoIndex;
    int tagIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        //Get Widgets
        imageView = findViewById(R.id.editImage);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        editText = findViewById(R.id.captionEdit);
        deleteButton = findViewById(R.id.deleteTag);
        addPersonButton = findViewById(R.id.addTagPerson);
        addLocationButton = findViewById(R.id.addTagLocation);
        tagValue = findViewById(R.id.tagValue);
        personTagList = findViewById(R.id.personTagList);
        locationTagList = findViewById(R.id.locationTagList);
        //Load Information
        current = (Album) getIntent().getSerializableExtra("album");
        update = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        photos = current.getPhotos();
        save = getIntent().getIntExtra("index", 0);
        photoIndex = getIntent().getIntExtra("photoIndex", 0);
        selected = (Photo) getIntent().getSerializableExtra("photo");
        ptags = selected.getPersonTags();
        ltags = selected.getLocationTags();
        filePath = selected.getPath();
        tagIndex = -1;
        //Set information
        personTags= new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, ptags);
        locationTags= new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, ltags);
        personTagList.setAdapter(personTags);
        locationTagList.setAdapter(locationTags);
        personTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTagP = selected.getPersonTags().get(i);
                selectedTagL = null;
                tagIndex = i;
                Toast.makeText(PhotoEdit.this, selected.getPersonTags().get(i).toString() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
        locationTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTagL = selected.getLocationTags().get(i);
                selectedTagP = null;
                tagIndex = i;
                Toast.makeText(PhotoEdit.this, selected.getLocationTags().get(i).toString() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
        editText.setText(selected.getCaption());
        //Display Image
        File imgFile = new File(selected.getPath());
        if (imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Toast.makeText(PhotoEdit.this, imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(b);
        }
        configureDeleteTagButton();
        configureAddPersonButton();
        configureAddLocationButton();
        configureUpdateButton();
    }

    private void configureUpdateButton() {
        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    Toast.makeText(PhotoEdit.this, "Please choose an image to add", Toast.LENGTH_SHORT).show();
                }
                else {
                    Photo p = new Photo(filePath);
                    p.setCaption(editText.getText().toString());
                    p.setTags(ptags, ltags);
                    photos.set(photoIndex, p);
                    saveData();
                    finish();
                }
            }
        });

    }

    private void configureDeleteTagButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((selectedTagP == null && selectedTagL==null)|| tagIndex == -1){
                    Toast.makeText(PhotoEdit.this, "Please select a tag", Toast.LENGTH_SHORT).show();
                }
                if (selectedTagP != null) {
                    ptags.remove(tagIndex);
                    personTags.notifyDataSetChanged();
                    tagIndex = -1;
                } else if(selectedTagL != null){
                    ltags.remove(tagIndex);
                    locationTags.notifyDataSetChanged();
                    tagIndex = -1;
                }else
                    Toast.makeText(PhotoEdit.this, "Tag not deleted", Toast.LENGTH_SHORT).show();
                Toast.makeText(PhotoEdit.this, "Tag deleted", Toast.LENGTH_SHORT).show();
            }
        });
        saveData();
    }

    private void configureAddPersonButton() {
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tagValue.getText().toString().trim() == ""){
                    Toast.makeText(PhotoEdit.this, "Please enter a tag value", Toast.LENGTH_SHORT).show();
                }
                PersonTag t = new PersonTag(tagValue.getText().toString().trim().toLowerCase());
                ptags.add((PersonTag) t);
                personTags.notifyDataSetChanged();
                Toast.makeText(PhotoEdit.this, "Tag Added", Toast.LENGTH_SHORT).show();
                tagValue.setText("");
            }
        });
        saveData();
    }

    private void configureAddLocationButton() {
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tagValue.getText().toString().trim() == ""){
                    Toast.makeText(PhotoEdit.this, "Please enter a tag value", Toast.LENGTH_SHORT).show();
                }
                LocationTag t = new LocationTag(tagValue.getText().toString().trim().toLowerCase());
                ltags.add((LocationTag) t);
                locationTags.notifyDataSetChanged();
                Toast.makeText(PhotoEdit.this, "Tag Added", Toast.LENGTH_SHORT).show();
                tagValue.setText("");
            }
        });
        saveData();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                filePath = getURL(uri);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getURL(Uri uri) {
        String path;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            path = uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            path = cursor.getString(index);
            cursor.close();
        }
        return path;
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
}
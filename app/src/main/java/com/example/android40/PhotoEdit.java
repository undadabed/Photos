package com.example.android40;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PhotoEdit extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView imageView;
    String filePath;
    Button updateButton;
    EditText editText;
    Album current;
    ArrayList<Album> update;
    ArrayList<Photo> photos;
    Photo selected;
    int save;
    int photoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        Button selectImage = findViewById(R.id.button13);
        imageView = findViewById(R.id.imageView2);
        editText = findViewById(R.id.Caption2);

        current = (Album) getIntent().getSerializableExtra("album");
        update = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        photos = current.getPhotos();
        save = getIntent().getIntExtra("index", 0);
        photoIndex = getIntent().getIntExtra("photoIndex", 0);
        selected = (Photo) getIntent().getSerializableExtra("photo");
        filePath = selected.getPath();

        editText.setText(selected.getCaption());

        File imgFile = new File(selected.getPath());
        if (imgFile.exists()) {
            Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Toast.makeText(PhotoEdit.this, imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(b);
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        configureUpdateButton();
    }

    private void configureUpdateButton() {
        updateButton = findViewById(R.id.button14);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    Toast.makeText(PhotoEdit.this, "Please choose an image to add", Toast.LENGTH_SHORT).show();
                }
                else {
                    Photo p = new Photo(filePath);
                    p.setCaption(editText.getText().toString());
                    photos.set(photoIndex, p);
                    saveData();
                    finish();
                }
            }
        });

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
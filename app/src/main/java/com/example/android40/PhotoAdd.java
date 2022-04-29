package com.example.android40;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PhotoAdd extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView imageView;
    String filePath;
    Button addButton;
    EditText editText;
    Album current;
    ArrayList<Album> update;
    ArrayList<Photo> photos;
    Photo selected;
    int save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_add);

        Button selectImage = findViewById(R.id.button11);
        imageView = findViewById(R.id.imageView);
        filePath = null;
        editText = findViewById(R.id.Caption);

        current = (Album) getIntent().getSerializableExtra("album");
        update = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        photos = current.getPhotos();
        selected = null;
        save = getIntent().getIntExtra("index", 0);
        uri = null;

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
        configureAddButton();
    }

    private void configureAddButton() {
        addButton = (Button) findViewById(R.id.button12);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    Toast.makeText(PhotoAdd.this, "Please choose an image to add", Toast.LENGTH_SHORT).show();
                }
                else {
                    Photo p = new Photo(filePath);
                    p.setCaption(editText.getText().toString());
                    photos.add(p);
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
                filePath = getURL(this, uri);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getURL(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
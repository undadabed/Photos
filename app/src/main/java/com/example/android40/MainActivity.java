package com.example.android40;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ArrayList<Album> albums;
    ArrayAdapter arrayAdapter;
    ListView listView;


    private static final int REQUEST_PERSMISIONS = 1234;

    private static final String[] PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSIONS_COUNT = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.PhotoEditList);
        loadData();

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, albums);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Photos.class);
                intent.putExtra("album", albums.get(i));
                intent.putExtra("albums", albums);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });
        configureEditButton();
    }


    private boolean permissionsDenied(){
        for(int i = 0; i < PERMISSIONS_COUNT; i++){
            if(checkSelfPermission(PERMISSION[i]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERSMISIONS && grantResults.length>0){
            if(permissionsDenied()){
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }else{
                onResume();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions(PERMISSION, REQUEST_PERSMISIONS);
        loadData();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, albums);
        listView.setAdapter(arrayAdapter);
    }

    private void configureEditButton() {
        Button editButton = (Button) findViewById(R.id.button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditList.class);
                intent.putExtra("albums", albums);
                startActivity(intent);
            }
        });
    }

    private void saveData() {
        //Objects Being saved
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(albums);
        editor.putString("album list", json);
        editor.apply();
        //Want to save the folders and images
        
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("album list", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        albums = gson.fromJson(json, type);
        if (albums == null) {
            albums = new ArrayList<>();
        }
    }

    public void clean() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }

    public void initialize() {
        ArrayList<Album> list = new ArrayList<>();
        Album test1 = new Album("test1", 0);
        Photo p = new Photo("hello world");
        test1.addPhoto(p);
        Album test2 = new Album("test2", 0);
        Album test3 = new Album("test3", 0);
        Album test4 = new Album("test4", 0);
        list.add(test1);
        list.add(test2);
        list.add(test3);
        list.add(test4);

        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("album list", json);
        editor.apply();
    }
}
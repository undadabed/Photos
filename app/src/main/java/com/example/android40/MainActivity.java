package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Album> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listview);
        list = new ArrayList<>();
        Album test1 = new Album("test1", 0);
        Photo random = new Photo("hello world");
        Photo random2 = new Photo("hello world2");
        test1.addPhoto(random);
        test1.addPhoto(random2);
        Album test2 = new Album("test2", 0);
        Album test3 = new Album("test3", 0);
        Album test4 = new Album("test4", 0);
        list.add(test1);
        list.add(test2);
        list.add(test3);
        list.add(test4);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Photos.class);
                intent.putExtra("album", list.get(i));
                startActivity(intent);
            }
        });
        configureEditButton();
    }
    private void configureEditButton() {
        Button editButton = (Button) findViewById(R.id.button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditList.class);
                intent.putExtra("albums", list);
                startActivity(intent);
            }
        });
    }
}
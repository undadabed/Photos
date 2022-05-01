package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    //UI
    Button backButton;
    ListView resultList;
    ImageViewAdapter adapter;

    //Objects
    ArrayList<Photo> result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backButton = findViewById(R.id.backResult);
        resultList = findViewById(R.id.resultList);


        result = (ArrayList<Photo>) getIntent().getSerializableExtra("photos");
        adapter = new ImageViewAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // go to carousel
                Intent intent = new Intent(Result.this, PhotoDisplay.class);
                intent.putExtra("photo", result.get(i));
                intent.putExtra("photos", result);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(v->{
            finish();
        });

    }




}
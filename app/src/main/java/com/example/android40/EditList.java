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

public class EditList extends AppCompatActivity {
    ArrayList<Album> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        ListView listView = findViewById(R.id.listView);

        list = new ArrayList<Album>();
        list = (ArrayList<Album>) getIntent().getSerializableExtra("albums");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EditList.this, Edit.class);
                intent.putExtra("albums", list);
                intent.putExtra("album", list.get(i));
                startActivity(intent);
            }
        });
        configureBackButton();
    }

    private void configureBackButton() {
        Button backButton = (Button) findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
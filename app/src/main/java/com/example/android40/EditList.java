package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EditList extends AppCompatActivity {
    ArrayList<Album> list;
    EditText editText;
    ListView listView;
    Button addButton;
    Button renameButton;
    Button deleteButton;
    Button backButton;
    ArrayAdapter arrayAdapter;
    Album current;
    Integer index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        listView = findViewById(R.id.listView);
        current = null;
        index = -1;
        editText = findViewById(R.id.inputName);

        list = new ArrayList<Album>();
        list = (ArrayList<Album>) getIntent().getSerializableExtra("albums");

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current = list.get(i);
                index = i;
                Toast.makeText(EditList.this, list.get(i).getAlbum() + " selected", Toast.LENGTH_SHORT).show();
            }
        });
        configureAddButton();
        configureRenameButton();
        configureDeleteButton();
        configureBackButton();
    }

    private void configureAddButton() {
        addButton = findViewById(R.id.button6);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                Album a = new Album(name, 0);
                if (list.contains(a) || name.trim().equals("")) {
                    Toast.makeText(EditList.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }
                else {
                    list.add(a);
                    arrayAdapter.notifyDataSetChanged();
                    editText.setText("");
                    saveData();
                }
            }
        });
    }

    private void configureRenameButton() {
        renameButton = findViewById(R.id.button7);
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (current == null || index == -1) {
                    Toast.makeText(EditList.this, "No album selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean duplicate = false;
                    for (Album a : list) {
                        if (a.getAlbum().equals(name)) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (duplicate) {
                        Toast.makeText(EditList.this, "Album name already exists", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Album a = new Album(name, 0);
                        list.set(index, a);
                        arrayAdapter.notifyDataSetChanged();
                        saveData();
                    }
                }
            }
        });
    }

    private void configureDeleteButton() {
        deleteButton = findViewById(R.id.button8);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current == null || index == -1) {
                    Toast.makeText(EditList.this, "No album selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditList.this, "index = " + index, Toast.LENGTH_SHORT).show();
                    list.remove((int)index);
                    arrayAdapter.notifyDataSetChanged();
                    current = null;
                    index = -1;
                    saveData();
                }
            }
        });
    }

    private void configureBackButton() {
        backButton = (Button) findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                finish();
            }
        });
    }


    private void saveData() {
        SharedPreferences prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("album list", json);
        editor.apply();
    }
}
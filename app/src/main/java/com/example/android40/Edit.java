package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Edit extends AppCompatActivity {
    ArrayList<Album> list;
    Album current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        current = (Album) getIntent().getSerializableExtra("album");
        EditText text = findViewById(R.id.albumName);
        text.setText(current.getAlbum(), TextView.BufferType.EDITABLE);
        list = new ArrayList<Album>();
        list = (ArrayList<Album>) getIntent().getSerializableExtra("albums");

        configureRenameButton();
        configureDeleteButton();
        configureCancelButton();
    }

    private void configureRenameButton() {
        Button renameButton = (Button) findViewById(R.id.button3);
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configureDeleteButton() {
        Button deleteButton = (Button) findViewById(R.id.button4);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(current);
                finish();
            }
        });
    }

    private void configureCancelButton() {
        Button cancelButton = (Button) findViewById(R.id.button5);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
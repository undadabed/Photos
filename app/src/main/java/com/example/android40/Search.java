package com.example.android40;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Search extends AppCompatActivity {

    //UI
    RadioButton andButton;
    RadioButton orButton;
    EditText personValue;
    EditText locationValue;
    Button cancel;
    Button search;

    //Objects
    ArrayList<Album> albums;
    ArrayList<Photo> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        albums = (ArrayList<Album>) getIntent().getSerializableExtra("albums");
        result = null;

        andButton = findViewById(R.id.andButton);
        orButton = findViewById(R.id.orButton);
        cancel = findViewById(R.id.cancelSearch);
        search = findViewById(R.id.searchTags);
        personValue = findViewById(R.id.personValue);
        locationValue = findViewById(R.id.locationValue);

        configureSearchButton();
        configureCancelButton();

    }

    private void configureCancelButton(){
        cancel.setOnClickListener(v->{
            finish();
        });
    }

    private void configureSearchButton(){
        search.setOnClickListener(v->{
            String pValue = personValue.getText().toString().toLowerCase().trim();
            String lValue = locationValue.getText().toString().toLowerCase().trim();
            boolean operation = andButton.isChecked();
            if(personValue.getText().toString().trim().equals("") && locationValue.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please enter values to search", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!personValue.getText().toString().trim().equals("") && locationValue.getText().toString().trim().equals("")){
                result = searchOneTag(true, pValue);
            }else if (personValue.getText().toString().trim().equals("") && !locationValue.getText().toString().trim().equals("")){
                result = searchOneTag(false, lValue);
            }else if(!personValue.getText().toString().trim().equals("") && !locationValue.getText().toString().trim().equals("")){
                result = searchTwoTag(pValue, lValue, operation);
            }
            displayResult();
        });
    }

    private ArrayList<Photo> searchOneTag(boolean isPerson, String value){
        ArrayList<Photo> result = new ArrayList<>();
        for(Album a: albums){
            for(Photo p : a.getPhotos()){
                if(isPerson){
                    for(PersonTag t : p.getPersonTags()){
                        if((t.getValue().toLowerCase().startsWith(value) || t.getValue().toLowerCase().equals(value)) && !result.contains(p))
                            result.add(p);
                    }
                }else{
                    for(LocationTag t : p.getLocationTags()){
                        if((t.getValue().toLowerCase().startsWith(value) || t.getValue().toLowerCase().equals(value)) && !result.contains(p))
                            result.add(p);
                    }
                }
            }
        }
        return result;
    }

    private ArrayList<Photo> searchTwoTag(String pvalue, String lvalue, boolean isAdd){
        ArrayList<Photo> result = new ArrayList<>();
        BiPredicate<Boolean, Boolean> predicate = isAdd ? (p, l)-> p && l : (p, l)->p || l;
        for(Album a: albums){
            for(Photo p: a.getPhotos()){
                boolean hasP = false;
                for(PersonTag pt : p.getPersonTags()){
                    Log.d("Tag info",String.format("value: %s comparing to: %s", pt.getValue(), pvalue));
                    if(pt.getValue().toLowerCase().startsWith(pvalue) || pt.getValue().toLowerCase().equals(pvalue)) {
                        hasP = true;
                        break;
                    }
                }
                boolean hasL = false;
                for(LocationTag lt : p.getLocationTags()){
                    Log.d("Tag info",String.format("value: %s comparing to: %s", lt.getValue(), lvalue));
                    if(lt.getValue().toLowerCase().startsWith(lvalue) || lt.getValue().toLowerCase().equals(lvalue)) {
                        hasL = true;
                        break;
                    }
                }
                Log.d("Bool info", String.format("Hasp: %b HasL %b", hasP, hasL));
                if(predicate.test(hasL, hasP) && !result.contains(p))
                    result.add(p);
            }
        }
        return result;
    }

    private void displayResult(){
        if(result == null || result.size() == 0){
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("photos", result);
        startActivity(intent);
    }

}
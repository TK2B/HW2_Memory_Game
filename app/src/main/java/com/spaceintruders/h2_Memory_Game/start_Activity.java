package com.spaceintruders.h2_Memory_Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

public class start_Activity extends AppCompatActivity {
    // Declare Vars 4 elements
    String message;
    EditText editText;
    Slider gridSize;
    int gridSizeInt;


    @Override
    /** CalledonLoad */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        bindUI();
    }


    /** Called when the user taps the Send button */
    public void start_Game(View view) {
        // Make a new Intent
        Intent intent = new Intent(start_Activity.this, Game_Activity.class);

        // Get Data you want
        message = editText.getText().toString();
        gridSizeInt = (int) gridSize.getValue();

        // Write Infos to intent to copy to other activity
        message = "Hello Player " + message ;
        intent.putExtra("Name", message);
        intent.putExtra("Gridsize", gridSizeInt );


        startActivity(intent);
    }
    /** Called by startGame */
    public void bindUI(){
        // place to get elements
        editText = (EditText) findViewById(R.id.myTextbox);
        gridSize = (Slider) findViewById(R.id.slider_nob);
    }
}
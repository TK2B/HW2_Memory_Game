package com.spaceintruders.h2_Memory_Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.slider.Slider;

public class start_Activity extends AppCompatActivity {
    // Declare Vars 4 elements



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
    }
    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Make a new Intent
        Intent intent = new Intent(this, MainActivity2.class);
        // Get Data you want
        EditText editText = (EditText) findViewById(R.id.myTextbox);
        String message = editText.getText().toString();
        Slider nob = (Slider) findViewById(R.id.slider_nob);
        int numberOfButtons = (int) nob.getValue();

        // Creat Player
        Player player = new Player(0,message,0);

        // Write Infos to intent to copy to other activity
        message = "Hello Player " + message ;
        intent.putExtra("nachricht", message);
        intent.putExtra("EXTRA_nob", numberOfButtons );


        startActivity(intent);
    }

}
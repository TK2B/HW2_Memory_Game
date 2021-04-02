package com.spaceintruders.h2_Memory_Game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;

import tyrantgit.explosionfield.ExplosionField;


// TODO extract Winning and Lose Methods and call them from wher you need it (redundance)

public class Game_Activity extends AppCompatActivity implements View.OnClickListener {
    // DECLARE UI ilements
    private GridLayout mygridLay;
    private TextView textTimer;
    private TextView txt_name;
    private TextView scoreDisplay;
    private int gridSize;
    private String name;
    private int nFlips;
    private int overallTime = 0;
    private static int Score = 0;
    private CountDownTimer mytimer;
    // Declare Vars
    private int numberOfElements;
    private CustomButtonClass[] arrayOFButtons;

    private int[] buttonGraphicLocation;
    private int[]buttonGraphics;
    private int[] frontImagesReferences = {
                                                R.drawable.img1,R.drawable.img2,
                                                R.drawable.img3,R.drawable.img4,
                                                R.drawable.img5,R.drawable.img6,
                                                R.drawable.img7,R.drawable.img8,
                                                R.drawable.img8,R.drawable.img10,
                                                R.drawable.img11,R.drawable.img12,
                                                R.drawable.img13,R.drawable.img14,
                                                R.drawable.img15,R.drawable.img16,
                                                R.drawable.img17,R.drawable.img18};

    private CustomButtonClass selcetedButton1;
    private CustomButtonClass selcetedButton2;

    private boolean isBussy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        bindUI();
        getmyDatafromIntent();
        start_one_game();



    }

    private void makeTimer (){
        //TODO Make Timer
    }

    private void start_one_game() {
        // score to 0
        scoreDisplay.setText("Score " + Score);

        makeTimer();

            // TODO Setup Game
            // get size og Grid // set myself bevore just to make sure it fits
            int nColums = mygridLay.getColumnCount();
            int nRows = mygridLay.getRowCount();
            // calc Elements
            numberOfElements =nColums *nRows;
            // 4 extend if you want to split model view
            arrayOFButtons=new CustomButtonClass[numberOfElements];
            //TODO 4 performance just load Drawables you neeed
            //buttonGraphics = new int[numberOfElements/2];
            buttonGraphics =frontImagesReferences;

            buttonGraphicLocation =new int[numberOfElements];

            shuffelButton();
        for(
            int r = 0;
            r<nRows;r++)

            {
                for (int c = 0; c < nColums; c++) {

                    CustomButtonClass temButton = new CustomButtonClass(this, r, c, buttonGraphics[buttonGraphicLocation[r * nColums + c]]);
                    temButton.setId(View.generateViewId());
                    temButton.setOnClickListener(this);
                    arrayOFButtons[r * nColums + c] = temButton;
                    mygridLay.addView(temButton);

                }


            }


        }


    private void shuffelButton() {
        //fill an Shuffel Cards (SWAP RAND SOTRE)
        Random rand = new Random();
        for (int i = 0  ; i < numberOfElements; i++){

            buttonGraphicLocation[i] = i % (numberOfElements/2);

        }
        for (int i = 0  ; i < numberOfElements; i++){

            int temp = buttonGraphicLocation[i];

            int swapIndex = rand.nextInt(numberOfElements);

            buttonGraphicLocation[i] =  buttonGraphicLocation[swapIndex];
            buttonGraphicLocation[swapIndex] = temp;

        }

    }


    public void bindUI(){
        // place to get elements
        mygridLay = (GridLayout) findViewById(R.id.gridLayout_gameActive);
        textTimer   = (TextView) findViewById(R.id.txt_timer);
        txt_name   = (TextView) findViewById(R.id.txt_name);
        scoreDisplay = (TextView) findViewById(R.id.ScoreView);
        //TODO Hardcoded Size remove

    }

    public void getmyDatafromIntent(){
        // Get the Intent that started this activity and extract the string
        Intent intentRES = getIntent();
        name = intentRES.getStringExtra("Name");
        //TODO Remove after fitting in game
        gridSize = intentRES.getIntExtra("Gridsize", 2);
        txt_name.setText(name);
        Log.i("########NAME ", String.valueOf(name));
        // SET GRIDSIZE
        mygridLay.setColumnCount(gridSize);
        mygridLay.setRowCount(gridSize);

    }


    @Override
    public void onClick(View view) {

        if (isBussy){
            return;
        }

        CustomButtonClass button = (CustomButtonClass) view;
        if (button.isMatched){
            return;
        }
        if (selcetedButton1 == null){
            //show first
            selcetedButton1 = button;
            selcetedButton1.flip();

            return;
        }
        if (selcetedButton1.getId()== button.getId()){
            // double selected
            return;
        }
        // Check if matches
        if (selcetedButton1.getFrontImageID() == button.getFrontImageID()){
            button.flip();
            button.setMatched(true);
            selcetedButton1.setMatched(true);

            selcetedButton1.setEnabled(false);
            button.setEnabled(false);
            // dont forget to reset
            selcetedButton1 = null;
            nFlips += 1;
            Score += 5;
            scoreDisplay.setText("Score " + Score+ " Flips " +nFlips);

            return;
        }
        else {
            selcetedButton2 = button;
            selcetedButton2.flip();
            isBussy = true;

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nFlips +=1;
                    scoreDisplay.setText("Score " + Score+ " Flips " +nFlips);
                    selcetedButton2.flip();
                    selcetedButton1.flip();
                    selcetedButton1 = null;
                    selcetedButton2 = null;
                    Score -= 1;
                    if (Score < 1){
                        Score = 0;
                    }
                    scoreDisplay.setText("Score " + Score+ " Flips " +nFlips);
                    isBussy = false;


                }
            },700);
        }
    }

    public void endbutton(View view) {
         int overallTime = 0;
         Score = 0;



        arrayOFButtons = null ;

        buttonGraphicLocation = null ;
        buttonGraphics= null;
        start_one_game();
    }
}

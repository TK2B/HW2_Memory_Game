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
import java.util.Stack;
import tyrantgit.explosionfield.ExplosionField;


// TODO extract Winning and Lose Methods and call them from wher you need it (redundance)

public class Game_Activity extends AppCompatActivity {
    // Declare Vars
    private GridLayout mygridLay;
    private TextView textTimer;
    private TextView txt_name;

    private int numberOfElements;
    private CustomButtonClass[] arrayOFButtons;

    private int[] frontImagesLocation;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        bindUI();

    }
    public void bindUI(){
        // place to get elements
        mygridLay = (GridLayout) findViewById(R.id.gridLayout_gameActive);
        textTimer   = (TextView) findViewById(R.id.txt_timer);
        txt_name   = (TextView) findViewById(R.id.txt_name);


    }


}

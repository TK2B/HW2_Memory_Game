package com.spaceintruders.h2_Memory_Game;

import androidx.annotation.RequiresApi;
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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import tyrantgit.explosionfield.ExplosionField;


// TODO extract Winning and Lose Methods and call them from wher you need it (redundance)

public class MainActivity2 extends AppCompatActivity {
    final static int INTERVAL = 1000;
    private int numOfImages;
    // refernceses to front images for this game
    private Integer[] frontImagesReferences;
    // referenceses to all the back images
    private Integer[] backImagesReferences = {
            R.drawable.img1,R.drawable.img2,
            R.drawable.img3,R.drawable.img4,
            R.drawable.img5,R.drawable.img6,
            R.drawable.img7,R.drawable.img8,
            R.drawable.img8,R.drawable.img10,
            R.drawable.img11,R.drawable.img12,
            R.drawable.img13,R.drawable.img14,
            R.drawable.img15,R.drawable.img16,
            R.drawable.img17,R.drawable.img18};
    // refernceses to back images for this game up to the num of images
    private Integer[] currentBackImagesReferences;


    private ArrayList<ImageView> imageViews;
    private ImageAdapter imageAdapter;
    private Animation scaleAnim;
    private Animation offsetAnim;
    private ExplosionField explosionField;

    private GameBoard board;
    private int frontImageId = R.drawable.back;
    private boolean isBusy = false;

    private String name;
    private int rowNum,colNum;
    private int points;
    private int results;

    private ImageView imageViewSelected1;
    private ImageView imageViewSelected2;
    private TextView textTimer;
    private TextView textName;
    private ConstraintLayout constLayout;
    private GridView myGrid;
    private CountDownTimer mCountDownTimer;
    private ProgressBar progBar;
    private int i = 0;
    private int currentSecond;
    private int time;
    private boolean isBind = false;
    public MemoryGameService.SensorBind binder;
    private Stack<ImageView>gameImageView;
    private  Intent serviceIntent;
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getParametersByIntent();
        frontImagesReferences = new Integer[numOfImages];
        currentBackImagesReferences = new Integer[numOfImages/2]; // num Of Images/2 - every image appear 2 times
        board = new GameBoard(numOfImages);
        gameImageView = new Stack<>();

        initCurrentBackImages();
        initFrontImages();
        addImagesToGameBoard();
        board.shuffle();

        bindUI();
        currentSecond = time/INTERVAL;
        int height = constLayout.getLayoutParams().height;
        int width = constLayout.getLayoutParams().width;
        offsetAnim = AnimationUtils.loadAnimation(this,R.anim.offset_anim);
        explosionField = ExplosionField.attach2Window(this);
        textName.setText(name);
        myGrid.setNumColumns(rowNum);
        imageAdapter = new ImageAdapter(this,height,width,rowNum,colNum,frontImagesReferences);
        myGrid.setAdapter(imageAdapter);

        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView img = (ImageView)v;
                checkBackImagesMatching(img,position);
            }
        });

        textTimer.setText(""+currentSecond);
        progBar.setProgress(i);
        mCountDownTimer = new CountDownTimer(time,INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentSecond--;
                i++;
                int timer = i*100/(time/INTERVAL);
                progBar.setProgress(timer);
                textTimer.setText(""+currentSecond);

            }

            @Override
            public void onFinish() {
                i++;
                stopService(serviceIntent);
                progBar.setProgress(100);
                textTimer.setText(""+0);
                doAnimationTimeOver();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },5000);
            }
        };

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCountDownTimer.start();
            }
        },900);
        serviceIntent = new Intent(this, MemoryGameService.class);
        bindService(serviceIntent, bindService, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(msg, new IntentFilter(getString(R.string.change)));
        scaleAnim = AnimationUtils.loadAnimation(this,R.anim.scale);
    }

    public void doAnimationTimeOver(){
        imageViews = imageAdapter.getImageViews();
        for(int i=0;i<imageViews.size();i++){
            explosionField.explode(textName);
            explosionField.explode(textTimer);
            explosionField.explode(progBar);
            imageViews.get(i).startAnimation(offsetAnim);
        }
        MainActivity2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity2.this, "Time over, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ServiceConnection bindService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MemoryGameService.SensorBind) iBinder;
            binder.notifyService(getString(R.string.listen_message));
            isBind = true;
        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }

    };
    private BroadcastReceiver msg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            state = intent.getBooleanExtra(getString(R.string.move), true);
            if (state) {
                return;
            }
            reverseImage();

        }

    };
    public void getParametersByIntent(){
        Intent intent = getIntent();
        rowNum = intent.getIntExtra("row",-1);
        colNum = intent.getIntExtra("col", -1);
        name = intent.getStringExtra("name");
        time = intent.getIntExtra("time",-1);
        points = intent.getIntExtra("point",-1);
        numOfImages = rowNum*colNum;
    }

    public void bindUI(){
        constLayout = findViewById(R.id.constraint_board);
        textTimer   = findViewById(R.id.txt_timer);
        textName   = findViewById(R.id.txt_name);
        progBar     = findViewById(R.id.progressBar);
        myGrid      = findViewById(R.id.gridview);
    }

    /**
     * This function check if 2 selected images on the board are match.
     * Checks if the player win.
     * @param img
     * @param position
     */
    public void checkBackImagesMatching(ImageView img,int position){

        if(isBusy)
            return;

        if(!board.isImageSelected(0)) {
            board.setImageSelectedFirstByIndex(position);
            if(!board.flipFirst()) {
                imageViewSelected1 = img;
                imageViewSelected1.setImageResource(board.getDrawableId(0));
            }
            else return;
        }
        if(!board.isImageSelected(1)) {
            board.setImageSelectedSecondByIndex(position);
            if (!board.flipSecond()) {
                results -= points/4; // not matching
                imageViewSelected2 = img;
                imageViewSelected2.setImageResource(board.getDrawableId(1));
            }
            else return;

            if (board.isSelectedImagesMatching()) {
                results += points; // matching
                gameImageView.push(imageViewSelected1);
                gameImageView.push(imageViewSelected2);
                if (checkWinGame()) {
                    mCountDownTimer.cancel(); // cancel timer
                    Intent resultIntent = new Intent();
                    if(results<0)// only postive points should be in the table
                        results=0;
                    resultIntent.putExtra("result", results+"");
                    setResult(RESULT_OK, resultIntent);
                    doAnimationWin();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2700);
                }
            } else {
                // do it in the background ;
                isBusy = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        board.resetSelectedImages();
                        imageViewSelected1.setImageResource(frontImageId);
                        imageViewSelected2.setImageResource(frontImageId);
                        isBusy = false;
                    }
                },800);
            }
        }
    }
    public void doAnimationWin(){

        imageViews = imageAdapter.getImageViews();
        for(int i=0;i<imageViews.size();i++){
            imageViews.get(i).startAnimation(scaleAnim);
        }
        MainActivity2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity2.this, "Winner, Your score is "+results, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void reverseImage(){
        ImageView imageView1, imageView2;

        if(!gameImageView.isEmpty())
        {
            board.flipBack();
            imageView2 = gameImageView.pop();
            imageView1 = gameImageView.pop();

            imageView1.setImageResource(frontImageId);
            imageView2.setImageResource(frontImageId);

            if(results > 0){
                results = results - 5;
            }
        }
    }

    public boolean checkWinGame(){
        return board.isAllImagesMatch();
    }

    public void addImagesToGameBoard(){
        int countCurrentBackImages = 0;
        for(int i = 0; i < numOfImages; i ++){
            if(countCurrentBackImages == currentBackImagesReferences.length)
                countCurrentBackImages = 0;
            board.addImage(currentBackImagesReferences[countCurrentBackImages]);
            countCurrentBackImages++;
        }
    }

    public void initCurrentBackImages(){
        for(int i = 0; i < currentBackImagesReferences.length; i ++){
            currentBackImagesReferences[i] = backImagesReferences[i];
        }
    }

    // This function initialize refernces to front image by num of images.
    public void initFrontImages(){
        for(int i = 0 ; i < numOfImages; i ++)
            frontImagesReferences[i] = frontImageId;
    }

    public void onBackPressed() {
        mCountDownTimer.cancel();
        stopService(serviceIntent);
        super.onBackPressed();
    }

    public void showDialogMessage(String message,String title){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity2.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }
}

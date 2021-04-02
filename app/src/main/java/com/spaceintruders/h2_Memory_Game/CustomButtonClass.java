package com.spaceintruders.h2_Memory_Game;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatDrawableManager;

import java.util.logging.LogRecord;

public class CustomButtonClass extends Button {
    // Declare Vars
    protected int row;
    protected int colum;
    protected int frontImageID;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;
    protected Drawable front;
    protected Drawable back;



    public CustomButtonClass (Context context, int row_i, int colum_i, int frontImageID_i ){
        super(context);
        row = row_i;
        colum = colum_i;
        frontImageID = frontImageID_i;

        front = context.getDrawable(frontImageID);
        //front = AppCompatDrawableManager.get().getDrawable(context, frontImageID);
        back = context.getDrawable(R.drawable.back);

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(row_i), GridLayout.spec(colum_i));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 80;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 80;
        //tempParams.setGravity(Gravity.CENTER);
        setLayoutParams(tempParams);

    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontImageID() {
        return frontImageID;
    }

    public void flip(){
        if (isMatched){
            return;
        }
        if (isFlipped){
            setBackground(back);
            isFlipped = false;
        }
        else {
            setBackground(front);
            isFlipped = true;




        }

    }
}

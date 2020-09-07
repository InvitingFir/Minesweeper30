package com.bignerdranch.minesweeper30;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

public class FieldCell{
    private boolean mIsOpened;
    private int mValue;
    private boolean mIsFlagged = false;
    private Button mButton;

    public FieldCell(Button b){
        this.mButton = b;
        nullTheCell();
    }

    public void setBomb(){ mValue = 9; }

    public boolean setFlag(){
        mIsFlagged = !mIsFlagged;
        updateImage();
        return mIsFlagged;
    }

    public void setOpened() {
        this.mIsOpened = true;
        updateImage();
    }

    public void incrementValue() { mValue++; }

    public int getValue(){ return this.mValue; }

    public boolean isFlag(){ return this.mIsFlagged; }

    public boolean isBomb(){ return this.mValue >= 9; }

    public boolean isOpened(){ return mIsOpened;}

    public Button getButton(){ return mButton; }

    private void updateImage(){
        if(mIsOpened){
            if(isFlag()){
                if(isBomb()) mButton.setText(R.string.flag);
                else mButton.setText(R.string.noBomb);
            }
            else if(isBomb()) mButton.setText(R.string.bomb);
            else if(mValue>0) mButton.setText(Integer.toString(mValue));
            else mButton.setText(R.string.empty);
        }
        else{
            if(isFlag()) mButton.setText(R.string.flag);
            else mButton.setText(" ");
        }
    }

    public void nullTheCell(){
        this.mValue = 0;
        this.mIsFlagged = false;
        this.mIsOpened = false;
        updateImage();
        mButton.setClickable(true);
        mButton.setLongClickable(true);
    }

    public void setClickable(boolean clickable){
        mButton.setClickable(clickable);
    }

    public void setLongClickable(boolean clickable){
        mButton.setLongClickable(clickable);
    }

    public void setOnClickListener(View.OnClickListener clickListener){
        mButton.setOnClickListener(clickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener l){
        mButton.setOnLongClickListener(l);
    }
}

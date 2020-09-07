package com.bignerdranch.minesweeper30.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Date;

public class Record {
    private int mTime;
    private Date mDate;
    private int mBombs;
    private int mFieldSize;
    private Bitmap mImage;

    public Record(int mTime, int mBombs, int mFieldSize, Bitmap image) {
        this(mTime, mBombs, mFieldSize, new Date(), image);
    }

    public Record(int mTime, int mBombs, int mFieldSize, Date date, Bitmap image){
        this.mTime = mTime;
        this.mBombs = mBombs;
        this.mFieldSize = mFieldSize;
        mDate = date;
        mImage = image;
    }

    public Record(int mTime, int mBombs, int mFieldSize, Date date, String imageFileName){
        this(mTime, mBombs, mFieldSize, date, BitmapFactory.decodeFile(imageFileName));
    }

    public int getTime() {
        return mTime;
    }

    public int getBombs() {
        return mBombs;
    }

    public int getFieldSize() {
        return mFieldSize;
    }

    public Date getDate() {
        return mDate;
    }

    public String getImageName(){
        return "IMG_" + mTime + ".jpg";
    }

    public Bitmap getImage(){return mImage;}
}

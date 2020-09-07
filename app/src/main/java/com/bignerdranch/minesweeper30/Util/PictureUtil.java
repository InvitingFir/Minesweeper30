package com.bignerdranch.minesweeper30.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class PictureUtil {

    public static Bitmap getBitmapFromView(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}

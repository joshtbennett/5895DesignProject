package joshbennett.designproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

/**
 * Created by Josh Bennett on 2017-03-02.
 */

public class ImageManipulator {
    private Bitmap src;
    private Context context;

    ImageManipulator(Bitmap src, Context context){
        this.src = src;
        this.context = context;
    }

    public void newimage(Bitmap src, Context context){
        this.src = src;
        this.context = context;
    }

    //convert a number from density independent pixels to pixels
    public int dpToPx(int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    //convert a number from pixels to density independent pixels
    public int pxToDp(int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) ((px / metrics.density) + 0.5);
    }

    public Bitmap scale(int newDimensions){ return android.graphics.Bitmap.createScaledBitmap(src, newDimensions, newDimensions, true);}

    public Bitmap rotateImage(float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
}

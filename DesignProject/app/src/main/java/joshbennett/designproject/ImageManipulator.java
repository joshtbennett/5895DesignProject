package joshbennett.designproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

/**
 * Created by Josh Bennett on 2017-03-02.
 */

public class ImageManipulator {
    private Bitmap src;
    private Context context;

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

    public Bitmap rotateImage(Bitmap image, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        return bmp;
    }

    public Bitmap overlayImages(Bitmap image1, Bitmap image2){
        Bitmap bmOverlay = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), image1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(image1, new Matrix(), null);
        canvas.drawBitmap(image2, 0, 0, null);
        return bmOverlay;
    }

    public Bitmap flip(Bitmap d)
    {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap src = d;
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }
}

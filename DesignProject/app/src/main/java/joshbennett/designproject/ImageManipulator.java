package joshbennett.designproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

/**
 * Created by Josh Bennett on 2017-03-02.
 */

public class ImageManipulator {
    private Context context;

    public ImageManipulator(Context context){   this.context = context; }

    /*
    * convert a number from density independent pixels to pixels
    * */
    public int dpToPx(int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    /*
    * Scales a given image to new given dimensions
    * */
    public Bitmap scale(int newDimensions, Bitmap src){ return android.graphics.Bitmap.createScaledBitmap(src, newDimensions, newDimensions, true);}

    /*
    * Rotates a given image a given angle clockwise
    * */
    public Bitmap rotateImage(Bitmap image, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        return bmp;
    }

    /*
    * Returns an image that is the image2 placed on top of image1
    * */
    public Bitmap overlayImages(Bitmap image1, Bitmap image2){
        Bitmap bmOverlay = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), image1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(image1, new Matrix(), null);
        canvas.drawBitmap(image2, 0, 0, null);
        return bmOverlay;
    }
}

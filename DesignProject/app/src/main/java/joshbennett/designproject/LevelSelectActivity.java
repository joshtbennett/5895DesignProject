package joshbennett.designproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LevelSelectActivity extends AppCompatActivity {

    private ImageManipulator manipulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level_select);

        /*ArrayList<ImageButton> buttons = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            final int j = i;

            //create a new button
            final ImageButton level = new ImageButton(this);

            //scale the image for the button
            Bitmap testimage = getBitmapFromAssets("testimage.png", 100);

            //put the scaled image on the button
            level.setImageBitmap(testimage);

            //action for when a button is pressed
            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(level.getContext(), LevelActivity.class);
                    intent.putExtra("levelNum", j+1);
                    intent.putExtra("isTutorial", false);
                    startActivity(intent);
                }
            });

            //add the new button to the buttons array
            buttons.add(level);
        }*/

        ArrayList<Button> buttons = new ArrayList<>();

        for(int i = 0; i < 30; i++){
            final int j = i;
            final Button level = new Button(this);

            level.setText(Integer.toString(i+1));

            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(level.getContext(), LevelActivity.class);
                    intent.putExtra("levelNum", j+1);
                    intent.putExtra("isTutorial", false);
                    startActivity(intent);
                }
            });
            buttons.add(level);
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for(int i = 0; i < 30; i++) {
            //add all the buttons to the grid
            grid.addView(buttons.get(i));
        }
    }

    public Bitmap getBitmapFromAssets(String filename, int dptopx){
        AssetManager assetManager = getAssets();

        try {
            InputStream istr = assetManager.open(filename);
            Bitmap image = BitmapFactory.decodeStream(istr);
            manipulator = new ImageManipulator(image, getApplicationContext());
            int newdimensions = manipulator.dpToPx(dptopx);
            image = manipulator.scale(newdimensions);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}




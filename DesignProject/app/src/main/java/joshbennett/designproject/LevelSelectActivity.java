package joshbennett.designproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

public class LevelSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level_select);

        ArrayList<ImageButton> buttons = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            final int j = i;

            //create a new button
            final ImageButton level = new ImageButton(this);

            //scale the image for the button
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            Bitmap testimage = BitmapFactory.decodeResource(getResources(), R.drawable.testimage, dimensions);
            ImageManipulator manipulator = new ImageManipulator(testimage, getApplicationContext());
            int newDimensions = manipulator.dpToPx(100);
            testimage = manipulator.scale(newDimensions);

            //put the scaled image on the button
            level.setImageBitmap(testimage);

            //action for when a button is pressed
            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(level.getContext(), LevelActivity.class);
                    intent.putExtra("Level Number", j+1);
                    startActivity(intent);
                }
            });

            //add the new button to the buttons array
            buttons.add(level);
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for(int i = 0; i < 30; i++) {
            //add all the buttons to the grid
            grid.addView(buttons.get(i));
        }
    }
}




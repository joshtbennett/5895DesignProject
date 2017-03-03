package joshbennett.designproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {


    private ImageManipulator manipulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        //LayoutParams params = new LinearLayout().LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ArrayList<ImageView> cells = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                final int k = (9*i)+j;

                final ImageView cell = new ImageView(this);

                //get images to display on the grid
                Bitmap bordercell = getBitmapImage(R.drawable.bordercell);
                Bitmap cornercell = getBitmapImage(R.drawable.cornercell);
                Bitmap emptycell = getBitmapImage(R.drawable.emptycell);


                //top row
                if(i == 0) {
                    if(j == 0){
                        cell.setImageBitmap(cornercell);
                    }
                    else if(j == 9) {
                        manipulator.newimage(cornercell, this.getApplicationContext());
                        cornercell = manipulator.rotateImage(90);
                        cell.setImageBitmap(cornercell);
                    }
                    else{
                        manipulator.newimage(bordercell, this.getApplicationContext());
                        bordercell = manipulator.rotateImage(90);
                        cell.setImageBitmap(bordercell);
                    }
                }
                //bottom row
                else if(i == 9) {
                    if(j == 0){
                        manipulator.newimage(cornercell, this.getApplicationContext());
                        cornercell = manipulator.rotateImage(270);
                        cell.setImageBitmap(cornercell);
                    }
                    else if(j == 9){
                        manipulator.newimage(cornercell, this.getApplicationContext());
                        cornercell = manipulator.rotateImage(180);
                        cell.setImageBitmap(cornercell);
                    }
                    else{
                        manipulator.newimage(bordercell, this.getApplicationContext());
                        bordercell = manipulator.rotateImage(270);
                        cell.setImageBitmap(bordercell);
                    }
                }
                //leftmost column
                else if(j== 0) {
                    cell.setImageBitmap(bordercell);
                }
                //rightmost column
                else if(j==9){
                    manipulator.newimage(bordercell, this.getApplicationContext());
                    bordercell = manipulator.rotateImage(180);
                    cell.setImageBitmap(bordercell);
                }
                else
                    cell.setImageBitmap(emptycell);

                //action for when a button is pressed
          /*  level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

                //add the new cell to the cells array
                cells.add(cell);
            }
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for (int i = 0; i < 100; i++) {
            //add all the buttons to the grid
            grid.addView(cells.get(i));
        }
    }

    public Bitmap getBitmapImage(int id){
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeResource(getResources(), id, dimensions);
        manipulator = new ImageManipulator(image, getApplicationContext());
        int newDimensions = manipulator.dpToPx(40);
        image = android.graphics.Bitmap.createScaledBitmap(image, newDimensions, newDimensions, true);
        return image;
    }

}

package joshbennett.designproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {


    private ImageManipulator manipulator;
    private ToggleButton deleteButton;
    private ToggleButton placeButton; private LevelFactory levelFactory = new LevelFactory();
    private ArrayList<LevelEntity> entities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        deleteButton = (ToggleButton) findViewById(R.id.DeleteMirrorToggle);
        placeButton = (ToggleButton) findViewById(R.id.PlaceMirrorToggle);

        levelFactory.setEntities();
        final Level level = levelFactory.generateLevel();
        entities = level.getEntities();

        drawLevel();
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

    public void DeleteButton(View v){
        if(placeButton.isChecked() == true)
            placeButton.setChecked(false);
    }

    public void PlaceButton(View v){
        if(deleteButton.isChecked() == true)
            deleteButton.setChecked(false);
    }

    public void drawLevel(){
        ArrayList<ImageView> cells = new ArrayList<>();
        String color;


        //get images to display on the grid
        Bitmap bordercell;
        Bitmap cornercell;
        Bitmap emptycell;
        Bitmap wallcell;
        Bitmap offboardcollector;
        Bitmap offboardemitter;


        //iterate throught the entity array adding entities to the board as they appear
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {


                //get images to display on the grid
                bordercell = getBitmapFromAssets("bordercell.png", 40);
                cornercell = getBitmapFromAssets("cornercell.png", 40);

                final ImageView cell = new ImageView(this);

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
                        if(entities.get((10 *i) + j) == null){
                            manipulator.newimage(bordercell, this.getApplicationContext());
                            bordercell = manipulator.rotateImage(90);
                            cell.setImageBitmap(bordercell);

                        }
                        else if(entities.get((10 * i) + j).getIdentifier() == 'c') {
                            color  = entities.get((10 * i) + j).getColor();
                            offboardcollector = getBitmapFromAssets(color+"/offboardcollector.png", 40);
                            manipulator.newimage(offboardcollector, this.getApplicationContext());
                            offboardcollector = manipulator.rotateImage(90);
                            cell.setImageBitmap(offboardcollector);
                        }
                        else if (entities.get((10 * i) + j).getIdentifier() == 'e') {
                            color  = entities.get((10 * i) + j).getColor();
                            offboardemitter = getBitmapFromAssets(color+"/offboardemitter.png", 40);
                            manipulator.newimage(offboardemitter, this.getApplicationContext());
                            offboardemitter = manipulator.rotateImage(90);
                            cell.setImageBitmap(offboardemitter);
                        }
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
                        if(entities.get((10 *i) + j) == null){
                            manipulator.newimage(bordercell, this.getApplicationContext());
                            bordercell = manipulator.rotateImage(270);
                            cell.setImageBitmap(bordercell);

                        }
                        else if(entities.get((10 * i) + j).getIdentifier() == 'c') {
                            color  = entities.get((10 * i) + j).getColor();
                            offboardcollector = getBitmapFromAssets(color+"/offboardcollector.png", 40);
                            manipulator.newimage(offboardcollector, this.getApplicationContext());
                            offboardcollector = manipulator.rotateImage(270);
                            cell.setImageBitmap(offboardcollector);
                        }
                        else if (entities.get((10 * i) + j).getIdentifier() == 'e') {
                            color  = entities.get((10 * i) + j).getColor();
                            offboardemitter = getBitmapFromAssets(color+"/offboardemitter.png", 40);
                            manipulator.newimage(offboardemitter, this.getApplicationContext());
                            offboardemitter = manipulator.rotateImage(270);
                            cell.setImageBitmap(offboardemitter);
                        }
                    }
                }
                //leftmost column
                else if(j== 0) {
                    if(entities.get((10 *i) + j) == null){
                        cell.setImageBitmap(bordercell);

                    }
                    else if(entities.get((10 * i) + j).getIdentifier() == 'c') {
                        color  = entities.get((10 * i) + j).getColor();
                        offboardcollector = getBitmapFromAssets(color+"/offboardcollector.png", 40);
                        manipulator.newimage(offboardcollector, this.getApplicationContext());
                        cell.setImageBitmap(offboardcollector);
                    }
                    else if (entities.get((10 * i) + j).getIdentifier() == 'e') {
                        color  = entities.get((10 * i) + j).getColor();
                        offboardemitter = getBitmapFromAssets(color+"/offboardemitter.png", 40);
                        manipulator.newimage(offboardemitter, this.getApplicationContext());
                        cell.setImageBitmap(offboardemitter);
                    }
                    else {
                        cell.setImageBitmap(bordercell);
                    }
                }
                //rightmost column
                else if(j==9){
                    if(entities.get((10 *i) + j) == null){
                        manipulator.newimage(bordercell, this.getApplicationContext());
                        bordercell = manipulator.rotateImage(180);
                        cell.setImageBitmap(bordercell);

                    }
                    else if(entities.get((10 * i) + j).getIdentifier() == 'c') {
                        color  = entities.get((10 * i) + j).getColor();
                        offboardcollector = getBitmapFromAssets(color+"/offboardcollector.png", 40);
                        manipulator.newimage(offboardcollector, this.getApplicationContext());
                        offboardcollector = manipulator.rotateImage(180);
                        cell.setImageBitmap(offboardcollector);
                    }
                    else if (entities.get((10 * i) + j).getIdentifier() == 'e') {
                        color  = entities.get((10 * i) + j).getColor();
                        offboardemitter = getBitmapFromAssets(color+"/offboardemitter.png", 40);
                        manipulator.newimage(offboardemitter, this.getApplicationContext());
                        offboardemitter = manipulator.rotateImage(180);
                        cell.setImageBitmap(offboardemitter);
                    }
                }
                else {

                    if(entities.get((10 *i) + j) == null){
                        emptycell = getBitmapFromAssets("emptycell.png", 40);
                        cell.setImageBitmap(emptycell);

                    }
                    else if(entities.get((10 * i) + j).getIdentifier() == 'w'){
                        wallcell = getBitmapFromAssets("wallcell.png", 40);
                        cell.setImageBitmap(wallcell);
                    }
                }

                //add the new cell to the cells array
                cells.add(cell);
            }
        }


        //if theres a emitter/collector on the border, the cell infront of it must be changed to
        // represent that as well
        for(int i = 0; i < 100; i++){
            final ImageView temp = new ImageView(this);

            Bitmap onboardcollectoroff;
            Bitmap onboardemitteroff;

            if(i > 0 && i <  9) {
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(90);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i + 10, temp);
                    } else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(90);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i + 10, temp);
                    }
                }
            }
            else if(i>90 && i < 99){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(270);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i - 10, temp);
                    } else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(270);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i - 10, temp);
                    }
                }
            }
            else if(i > 0 && i < 90 && i % 10 == 0){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i + 1, temp);
                    } else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i + 1, temp);
                    }
                }
            }
            else if(i > 9 && i < 89 && i % 10 == 9){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(180);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i - 1, temp);
                    } else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(180);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i - 1, temp);
                    }
                }
            }
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        for (int i = 0; i < 100; i++) {
            //add all the buttons to the grid
            grid.addView(cells.get(i));
        }
    }
}
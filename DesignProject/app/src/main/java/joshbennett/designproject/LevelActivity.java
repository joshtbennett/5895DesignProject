package joshbennett.designproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {


    private Level level;
    private ImageManipulator manipulator;
    private ToggleButton deleteButton;
    private ToggleButton placeButton;
    private ToggleButton flipButton;
    private LevelFactory levelFactory = new LevelFactory();
    private ArrayList<LevelEntity> entities;
    private CheckBox redCheckBox;
    private CheckBox greenCheckBox;
    private CheckBox blueCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        deleteButton = (ToggleButton) findViewById(R.id.DeleteMirrorToggle);
        placeButton = (ToggleButton) findViewById(R.id.PlaceMirrorToggle);
        flipButton = (ToggleButton) findViewById(R.id.FlipMirrorToggle);
        redCheckBox = (CheckBox)  findViewById(R.id.Red);
        greenCheckBox = (CheckBox)  findViewById(R.id.Green);
        blueCheckBox = (CheckBox)  findViewById(R.id.Blue);

        levelFactory.setEntities();level = levelFactory.generateLevel();
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
        if(placeButton.isChecked())
            placeButton.setChecked(false);
        if(flipButton.isChecked())
            flipButton.setChecked(false);
    }

    public void PlaceButton(View v){
        if(deleteButton.isChecked())
            deleteButton.setChecked(false);
        if(flipButton.isChecked())
            flipButton.setChecked(false);
    }

    public void FlipButton(View v){
        if(deleteButton.isChecked())
            deleteButton.setChecked(false);
        if(placeButton.isChecked())
            placeButton.setChecked(false);
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

                //placeable area
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

            //top row in placeable area
            if(i > 0 && i <  9) {
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(90);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i + 10, temp);

                        //update entity array
                        Collector collector = new Collector(color);
                        level.addEntity(collector, i+10);
                    }
                    else if (entities.get(i).getIdentifier() == 'e') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(90);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i + 10, temp);

                        //update entity array
                        Emitter emitter = new Emitter(color);
                        level.addEntity(emitter, i+10);
                    }
                }
            }

            //bottom row in placeable area
            else if(i>90 && i < 99){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(270);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i - 10, temp);

                        //update entity array
                        Collector collector = new Collector(color);
                        level.addEntity(collector, i-10);
                    }
                    else if (entities.get(i).getIdentifier() == 'e') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(270);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i - 10, temp);

                        //update entity array
                        Emitter emitter = new Emitter(color);
                        level.addEntity(emitter, i-10);
                    }
                }
            }

            //leftmost colum in placeable area
            else if(i > 0 && i < 90 && i % 10 == 0){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i + 1, temp);

                        //update entity array
                        Collector collector = new Collector(color);
                        level.addEntity(collector, i+1);
                    }
                    else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i + 1, temp);

                        //update entity array
                        Emitter emitter = new Emitter(color);
                        level.addEntity(emitter, i+1);
                    }
                }
            }

            //rightmost column in placeable area
            else if(i > 9 && i < 89 && i % 10 == 9){
                if(entities.get(i) != null) {
                    if (entities.get(i).getIdentifier() == 'c') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardcollectoroff = getBitmapFromAssets(color + "/onboardcollectoroff.png", 40);
                        manipulator.newimage(onboardcollectoroff, this.getApplicationContext());
                        onboardcollectoroff = manipulator.rotateImage(180);
                        temp.setImageBitmap(onboardcollectoroff);
                        cells.set(i - 1, temp);

                        //update entity array
                        Collector collector = new Collector(color);
                        level.addEntity(collector, i-1);
                    }
                    else if (entities.get(i).getIdentifier() == 'e') {

                        //update image
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        onboardemitteroff = manipulator.rotateImage(180);
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i - 1, temp);

                        //update entity array
                        Emitter emitter = new Emitter(color);
                        level.addEntity(emitter, i-1);
                    }
                }
            }
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        for (int i = 0; i < 100; i++) {

            //if current cell is in placeable area
            if(i > 10 && i < 89 && i%10!=9 && i%10!=0){
                setOnClick(cells.get(i), i);
            }
            //add all the buttons to the grid
            grid.addView(cells.get(i));
        }
    }

    private void setOnClick(final ImageView clickableimage, final int location){
        clickableimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color;


                if(entities.get(location) == null) {
                    if (redCheckBox.isChecked())
                        if (greenCheckBox.isChecked())
                            if (blueCheckBox.isChecked())
                                color = "White";
                            else
                                color = "Yellow";
                        else if (blueCheckBox.isChecked())
                            color = "Magenta";
                        else
                            color = "Red";

                    else if (greenCheckBox.isChecked())
                        if (blueCheckBox.isChecked())
                            color = "Cyan";
                        else
                            color = "Green";
                    else
                        color = "Blue";


                    if(placeButton.isChecked()) {
                        //update image
                        Bitmap current = ((BitmapDrawable) clickableimage.getDrawable()).getBitmap();
                        Bitmap layontop = getBitmapFromAssets(color + "/45mirror.png", 40);
                        manipulator.newimage(current, getApplicationContext());
                        Bitmap overlayed = manipulator.overlayImages(layontop);
                        clickableimage.setImageBitmap(overlayed);

                        //update entity array
                        Mirror mirror = new Mirror(color);
                        level.addEntity(mirror, location);
                    }
                }
                else if(entities.get(location).getIdentifier() == 'm'){
                    //update image
                    if(deleteButton.isChecked()) {
                        Bitmap emptycell = getBitmapFromAssets("emptycell.png", 40);
                        clickableimage.setImageBitmap(emptycell);

                        //update entity array
                        level.removeEntity(location);
                    }
                    else if(flipButton.isChecked()){
                        if(clickableimage.getScaleX() == -1)
                            clickableimage.setScaleX(1);
                        else
                            clickableimage.setScaleX(-1);

                        //figure out how to actually flip the mirror


                    }

                }
                else if(entities.get(location).getIdentifier() == 'e' || entities.get(location).getIdentifier() == 'c' || entities.get(location).getIdentifier() == 'w'){}
            }
        });
    }
}
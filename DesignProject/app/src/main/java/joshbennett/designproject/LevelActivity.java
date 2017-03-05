package joshbennett.designproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {


    private Level level;
    private String color;
    private ArrayList<ImageView> cells;
    private ImageManipulator manipulator;
    private EntityHandler entityHandler;
    private LevelFactory levelFactory = new LevelFactory();
    private ArrayList<ColorableEntity> entities;
    private ArrayList<Wall> walls;
    private ArrayList<Mirror> mirrors;
    private CheckBox redCheckBox;
    private CheckBox greenCheckBox;
    private CheckBox blueCheckBox;
    private ToggleButton deleteButton;
    private ToggleButton placeButton;
    private ToggleButton flipButton;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        deleteButton = (ToggleButton) findViewById(R.id.DeleteMirrorToggle);
        placeButton = (ToggleButton) findViewById(R.id.PlaceMirrorToggle);
        flipButton = (ToggleButton) findViewById(R.id.FlipMirrorToggle);
        start = (Button)findViewById(R.id.startButton);
        redCheckBox = (CheckBox)  findViewById(R.id.Red);
        greenCheckBox = (CheckBox)  findViewById(R.id.Green);
        blueCheckBox = (CheckBox)  findViewById(R.id.Blue);

        levelFactory.setEntities();level = levelFactory.generateLevel();
        entities = level.getEntities();
        walls = level.getWalls();

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

    public void start(View v){
        ImageView cell;
        Bitmap image;
        int position = -1;

        for(int i = 0; i < 100; i++) {
            if(i > 10 && i < 89 && i%10!=9 && i%10!=0) {
                if (entities.get(i) != null && entities.get(i).getIdentifier() == 'e') {
                    color = entities.get(i).getColor();
                    cell = cells.get(i);
                    image = getBitmapFromAssets(color + "/onboardemitteron.png", 40);
                    manipulator.newimage(image, getApplicationContext());
                    if (i <19) {
                        if(i == 11 && entities.get(i-10) != null){
                            image = manipulator.rotateImage(90);
                        }
                        else if(i == 18 && entities.get(i-10) != null){
                            image = manipulator.rotateImage(90);
                        }
                    } else if (i % 10 == 8) {
                        image = manipulator.rotateImage(180);
                    } else if (i > 80) {
                        if(i == 11 && entities.get(i+10) != null){
                            image = manipulator.rotateImage(270);
                        }
                        else if(i == 18 && entities.get(i+10) != null){
                            image = manipulator.rotateImage(270);
                        }
                    }
                    cell.setImageBitmap(image);
                    cells.set(i, cell);

                }
            }
        }
    }

    public void drawLevel(){
        entityHandler = new EntityHandler();
        cells = new ArrayList<>();


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
                    if(walls.get((10 * i) + j) != null) {
                        wallcell = getBitmapFromAssets("wallcell.png", 40);
                        cell.setImageBitmap(wallcell);
                    }

                    else if(entities.get((10 *i) + j) == null){
                        emptycell = getBitmapFromAssets("emptycell.png", 40);
                        cell.setImageBitmap(emptycell);
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
                        entityHandler.addEntity(level, collector, i+10);
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
                        entityHandler.addEntity(level, emitter, i+10);
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
                        entityHandler.addEntity(level, collector, i-10);
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
                        entityHandler.addEntity(level, emitter, i-10);
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
                        entityHandler.addEntity(level, collector, i+1);
                    }
                    else if (entities.get(i).getIdentifier() == 'e') {
                        color = entities.get(i).getColor();
                        onboardemitteroff = getBitmapFromAssets(color + "/onboardemitteroff.png", 40);
                        manipulator.newimage(onboardemitteroff, this.getApplicationContext());
                        temp.setImageBitmap(onboardemitteroff);
                        cells.set(i + 1, temp);

                        //update entity array
                        Emitter emitter = new Emitter(color);
                        entityHandler.addEntity(level, emitter, i+1);
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
                        entityHandler.addEntity(level, collector, i-1);
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
                        entityHandler.addEntity(level, emitter, i-1);
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

    private void setOnClick(final ImageView clickableimage, final int position) {
        clickableimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            color = "purple";
            if(level.getWalls().get(position) != null){
                color = "orange";
            }

            else if(level.getEntities().get(position) != null){

                color = "orange";
            }

            //if a mirror is tapped
            else if(level.getMirrors().get(position) != null) {
                //delete
                if (deleteButton.isChecked()) {
                    Bitmap emptycell = getBitmapFromAssets("emptycell.png", 40);
                    clickableimage.setImageBitmap(emptycell);
                    entityHandler.removeMirror(level, position);
                }
                //flip
                else if (flipButton.isChecked()) {


                    Bitmap current = ((BitmapDrawable) clickableimage.getDrawable()).getBitmap();
                    manipulator.newimage(current, getApplicationContext());
                    current = manipulator.rotateImage(90);
                    clickableimage.setImageBitmap(current);

                    if (level.getMirrors().get(position).getAngle() == 45) {
                        Mirror mirror = level.getMirrors().get(position);
                        entityHandler.flipMirror(level, mirror, position);
                    }
                    else if(level.getMirrors().get(position).getAngle() == 135) {
                        Mirror mirror = level.getMirrors().get(position);
                        entityHandler.flipMirror(level, mirror, position);
                    }
                }
                // add
                else {
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

                    //update image
                    Bitmap current = ((BitmapDrawable) clickableimage.getDrawable()).getBitmap();
                    Bitmap layontop = getBitmapFromAssets(color + "/45mirror.png", 40);
                    manipulator.newimage(current, getApplicationContext());
                    Bitmap overlayed = manipulator.overlayImages(layontop);
                    clickableimage.setImageBitmap(overlayed);

                    //update entity array
                    Mirror mirror = new Mirror(color);
                    entityHandler.addMirror(level, mirror, position);
                }

            }

            else if(level.getEntities().get(position) == null && level.getWalls().get(position) == null && level.getEntities().get(position) == null){
                if (placeButton.isChecked()) {
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

                    //update image
                    Bitmap current = ((BitmapDrawable) clickableimage.getDrawable()).getBitmap();
                    Bitmap layontop = getBitmapFromAssets(color + "/45mirror.png", 40);
                    manipulator.newimage(current, getApplicationContext());
                    Bitmap overlayed = manipulator.overlayImages(layontop);
                    clickableimage.setImageBitmap(overlayed);

                    //update entity array
                    Mirror mirror = new Mirror(color);
                    entityHandler.addMirror(level, mirror, position);
                }
                else{
                    //do nothing
                }
            }
            }
        });
    }
}
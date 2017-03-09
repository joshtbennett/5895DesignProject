package joshbennett.designproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {

    private static LevelActivity instance = null;
    private Level level;
    private int levelNum;
    private boolean isTutorial;
    private String color;
    private ArrayList<ImageView> cells;
    private ImageManipulator manipulator;
    private EntityHandler entityHandler = new EntityHandler(levelNum);
    private LevelFactory levelFactory;
    private ArrayList<ColorableEntity> entities;
    private ArrayList<Wall> walls;
    private ArrayList<Mirror> mirrors = new ArrayList<>();
    private CheckBox redCheckBox;
    private CheckBox greenCheckBox;
    private CheckBox blueCheckBox;
    private ToggleButton deleteButton;
    private ToggleButton placeButton;
    private ToggleButton flipButton;
    private Button startButton;
    private PopupWindow endwindow;
    private int length;

    public ArrayList<ImageView> getCells(){ return cells; }

    public static LevelActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        Intent intent = getIntent();
        levelNum = intent.getIntExtra("levelNum", 1);
        isTutorial = intent.getBooleanExtra("isTutorial", false);
        levelFactory = new LevelFactory(levelNum, isTutorial, getApplicationContext());

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        deleteButton = (ToggleButton) findViewById(R.id.DeleteMirrorToggle);
        placeButton = (ToggleButton) findViewById(R.id.PlaceMirrorToggle);
        flipButton = (ToggleButton) findViewById(R.id.FlipMirrorToggle);
        startButton = (Button)findViewById(R.id.startButton);
        redCheckBox = (CheckBox)  findViewById(R.id.red);
        greenCheckBox = (CheckBox)  findViewById(R.id.green);
        blueCheckBox = (CheckBox)  findViewById(R.id.blue);

        //levelFactory.setEntities();
        level = levelFactory.generateLevel();
        length = level.getSideLength();
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

    public void stop(View v) {
        level.isRunning = false;
        startButton.setText("Start");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start(v);
            }
        });
        for (ColorableEntity i : level.getEntities()) {
            if (i.getIdentifier() == 'c') {
                i.setReceived(false);
            }
        }
        level.getBeams().clear();
        drawLevel();

    }

    public void start(View v) {

        ImageView cell;

        level.isRunning = true;
        startButton.setText("Stop");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop(v);
            }
        });

        Bitmap image;
        Beam beam;
        int position;

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getIdentifier() == 'e') {
                position = entities.get(i).getPosition();
                color = entities.get(i).getColor();
                int size = length * length;
                image = getBitmapFromAssets(color + "/onboardemitteron.png", 40);
                manipulator.newimage(image, getApplicationContext());

                if (position < length - 1) {
                    cell = cells.get(position + length);
                    image = manipulator.rotateImage(90);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('d', color, position + length);
                    entityHandler.moveBeam(level, beam, position + length);

                } else if (position > (size - length)) {
                    cell = cells.get(position - length);
                    image = manipulator.rotateImage(270);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('u', color, position - length);
                    entityHandler.moveBeam(level, beam, position - length);

                } else if (position % length == 0) {
                    cell = cells.get(position + 1);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('r', color, position + 1);
                    entityHandler.moveBeam(level, beam, position + 1);

                } else if (position % length == length - 1) {
                    cell = cells.get(position - 1);
                    image = manipulator.rotateImage(180);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('l', color, position - 1);
                    entityHandler.moveBeam(level, beam, position - 1);
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getReceived() == true) {
                Bitmap onboardcollectoron = getBitmapFromAssets(entities.get(i).getColor() + "/onboardcollectoron.png", 40);
                manipulator.newimage(onboardcollectoron, this);
                int location = entities.get(i).getPosition();
                if (location < 2 * length - 1) {
                    onboardcollectoron = manipulator.rotateImage(90);
                } else if (location > (length * length - 2 * length)) {
                    onboardcollectoron = manipulator.rotateImage(270);

                } else if (location % length == 1) {
                    onboardcollectoron = manipulator.rotateImage(0);

                } else if (location % length == length - 2) {
                    onboardcollectoron = manipulator.rotateImage(180);
                }
                cell = cells.get(entities.get(i).getPosition());
                cell.setImageBitmap(onboardcollectoron);
                cells.set(location, cell);

            }
        }

        char direction;
        for (int i = 0; i < level.getBeams().size(); i++) {

            color = level.getBeams().get(i).getColor();
            String originalColor = color;
            int beamposition = level.getBeams().get(i).getPosition();
            Bitmap current = ((BitmapDrawable) cells.get(beamposition).getDrawable()).getBitmap();
            //leaving direction (after its hit the mirror
            direction = level.getBeams().get(i).getDirection();
            Bitmap straightbeam = getBitmapFromAssets(originalColor + "/straightbeam.png", 40);
            manipulator.newimage(straightbeam, getApplicationContext());
            boolean hasMirror = false;

            for (int j = 0; j < level.getMirrors().size(); j++) {
                if (level.getMirrors().get(j).getPosition() == level.getBeams().get(i).getPosition()) {
                    hasMirror = true;
                }
            }
            if (hasMirror == false) {
                //going left or right
                if (direction == 'l' || direction == 'r') {
                    Bitmap background = ((BitmapDrawable) cells.get(beamposition).getDrawable()).getBitmap();
                    straightbeam = manipulator.rotateImage(90);
                    straightbeam = manipulator.overlayImages(background, straightbeam);
                    cell = cells.get(beamposition);
                    cell.setImageBitmap(straightbeam);
                    cells.set(beamposition, cell);
                }
                //going up and down
                if (direction == 'u' || direction == 'd') {
                    Bitmap background = ((BitmapDrawable) cells.get(beamposition).getDrawable()).getBitmap();
                    straightbeam = manipulator.overlayImages(background, straightbeam);
                    cell = cells.get(beamposition);
                    cell.setImageBitmap(straightbeam);
                    cells.set(beamposition, cell);
                }
            }




            /*
            for (int j = 0; j < level.getMirrors().size(); j++) {
                int mirrorposition = level.getMirrors().get(j).getPosition();
                int angle = level.getMirrors().get(j).getAngle();
                if (beamposition == mirrorposition) {
                    if (entityHandler.isComponent(level.getBeams().get(i).getColor(), level.getMirrors().get(j).getColor())) {
                        if (angle == 45 && (direction == 'd' || direction == 'l')) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(270);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);

                        } else if (angle == 45 && (direction == 'u' || direction == 'r')) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam1, getApplicationContext());
                            beam1 = manipulator.rotateImage(180);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(90);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        } else if (angle == 135 && (direction == 'r' || direction == 'd')) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam1, getApplicationContext());
                            beam1 = manipulator.rotateImage(180);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(270);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        } else if (angle == 135 && (direction == 'l' || direction == 'u')) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(90);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        }
                    } else {
                        if ((direction == 'u' || direction == 'd') && angle == 135) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam1, getApplicationContext());
                            beam1 = manipulator.rotateImage(90);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(270);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        } else if ((direction == 'l' || direction == 'r') && angle == 135) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(180);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        } else if ((direction == 'u' || direction == 'd') && angle == 45) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam1, getApplicationContext());
                            beam1 = manipulator.rotateImage(90);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopright.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(270);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        } else if ((direction == 'l' || direction == 'r') && angle == 45) {
                            Bitmap beam2;
                            Bitmap beam1;
                            beam1 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            beam2 = getBitmapFromAssets(color + "/mirrorbeamtopleft.png", 40);
                            manipulator.newimage(beam2, getApplicationContext());
                            beam2 = manipulator.rotateImage(180);
                            beam2 = manipulator.overlayImages(beam2, beam1);
                            current = manipulator.overlayImages(current, beam2);
                            cell = cells.get(beamposition);
                            cell.setImageBitmap(current);
                            cells.set(beamposition, cell);
                        }
                    }*/
            if (level.checkWin()) {
                displayFinish();
            }
        }
    }
    
    public void drawLevel(){
        cells = new ArrayList<>((length) * (length));


        //get images to display on the grid
        Bitmap bordercell = getBitmapFromAssets("bordercell.png", 40);
        Bitmap cornercell = getBitmapFromAssets("cornercell.png", 40);
        Bitmap emptycell = getBitmapFromAssets("emptycell.png", 40);
        Bitmap wallcell = getBitmapFromAssets("wallcell.png", 40);

        char type;
        int position;
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        grid.removeAllViews();
        grid.setRowCount(length);
        grid.setColumnCount(length);

        //start with an empty board
        for(int i = 0; i < length; i++){
            for(int j = 0; j <  length; j++){

                ImageView cell = new ImageView(this);

                if(i == 0){
                    if(j == 0){
                        //corner
                        cell.setImageBitmap(cornercell);
                    }
                    else if(j == length-1){
                        //corner
                        cell = addImage(cornercell, 90);
                    }
                    else{
                        //border
                        cell = addImage(bordercell, 90);
                    }
                }
                else if(i == length-1){
                    if(j == 0){
                        //corner
                        cell = addImage(cornercell, 270);
                    }
                    else if(j == length-1){
                        //corner
                        cell = addImage(cornercell, 180);
                    }
                    else {
                        //border
                        cell = addImage(bordercell, 270);
                    }
                }
                else if(j == 0){
                    //border
                    cell.setImageBitmap(bordercell);
                }
                else if(j == length-1){
                    //border
                    cell = addImage(bordercell,  180);
                }
                else{
                    //middle
                    cell = addImage(emptycell, 0);
                }
                cells.add(cell);
            }

        }

        //get emitters and collectors
        for(int i = 0; i < entities.size(); i++){
            //get attributes
            type = entities.get(i).getIdentifier();
            position = entities.get(i).getPosition();
            color = entities.get(i).getColor();

            cells = displayIfCollector(type, position, color, cells);
            cells = displayIfEmitter(type, position, color, cells);
        }

        for(int i = 0; i < walls.size(); i++){
            ImageView cell = new ImageView(getApplicationContext());
            position = walls.get(i).getPosition();
            cell.setImageBitmap(wallcell);
            cells.set(position, cell);
        }

        //create a grid to display the buttons
        for (int i = 0; i < length * length; i++) {
            //add all the buttons to the grid
            grid.addView(cells.get(i));

            //if current cell is in placeable area
            if(i > length && i < length*length - (length+1)  && (i % length != length - 1) && i % length != 0){
                setOnClick(cells.get(i), i);
            }
        }

        for (Mirror i : level.getMirrors()) {
            Bitmap newimage = getBitmapFromAssets(i.getColor() + "/45mirror.png", 40);
            manipulator.newimage(newimage, this);
            if(i.getAngle()==135) {
                newimage = manipulator.rotateImage(90);
            }
            ImageView cell = cells.get(i.getPosition());
            cell.setImageBitmap(newimage);
            cells.set(i.getPosition(), cell);
        }
    }

    private void setOnClick(final ImageView clickableimage, final int position) {
        clickableimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.isRunning)
                    return;
                walls = level.getWalls();
                entities = level.getEntities();
                mirrors = level.getMirrors();
                boolean wallCheck = false;
                boolean entityCheck = false;
                boolean mirrorCheck = false;
                String color;

                //get player selected color
                if (redCheckBox.isChecked())
                    if (greenCheckBox.isChecked())
                        if (blueCheckBox.isChecked())
                            color = "white";
                        else
                            color = "yellow";
                    else if (blueCheckBox.isChecked())
                        color = "magenta";
                    else
                        color = "red";

                else if (greenCheckBox.isChecked())
                    if (blueCheckBox.isChecked())
                        color = "cyan";
                    else
                        color = "green";
                else
                    color = "blue";

                //check contents of player selected cell
                if(walls.size()>0)
                    for(int i = 0; i < walls.size(); i++)
                        if (position == walls.get(i).getPosition())
                            wallCheck = true;
                if(entities.size()>0)
                    for(int i = 0; i < entities.size(); i++)
                        if(position == entities.get(i).getPosition())
                            entityCheck = true;
                if(mirrors.size()>0)
                    for(int i = 0; i < mirrors.size(); i++)
                        if(position == mirrors.get(i).getPosition())
                            mirrorCheck = true;

                //touched cell containing mirror
                if(mirrorCheck){
                    if(placeButton.isChecked()){
                        //replace current mirror

                        Bitmap newimage = getBitmapFromAssets(color + "/45mirror.png", 40);
                        ImageView cell = cells.get(position);
                        cell.setImageBitmap(newimage);
                        cells.set(position, cell);

                        Mirror mirror = new Mirror(color, position);
                        //remove current mirror
                        entityHandler.removeMirror(level, position);
                        //add new mirror
                        entityHandler.addMirror(level, mirror, position);
                    }
                    else if(deleteButton.isChecked()){
                        //delete current mirror
                        Bitmap emptycell = getBitmapFromAssets("emptycell.png", 40);
                        clickableimage.setImageBitmap(emptycell);
                        entityHandler.removeMirror(level, position);
                    }
                    else{
                        //flip current mirror
                        Bitmap current = ((BitmapDrawable) clickableimage.getDrawable()).getBitmap();
                        manipulator.newimage(current, getApplicationContext());
                        current = manipulator.rotateImage(90);
                        clickableimage.setImageBitmap(current);

                        for(int i = 0; i < mirrors.size(); i++)
                        {
                            if(mirrors.get(i).getPosition() == position){
                                if(mirrors.get(i).getAngle() == 45){
                                    Mirror temp = mirrors.get(i);
                                    entityHandler.flipMirror(level, temp, position);
                                }
                                else if(mirrors.get(i).getAngle() == 135){
                                    Mirror temp = mirrors.get(i);
                                    entityHandler.flipMirror(level, temp, position);
                                }
                            }
                        }
                    }
                }

                //touched empty cell
                else if(!wallCheck && !entityCheck && !mirrorCheck){
                    if(placeButton.isChecked()){
                        //place a new mirror
                        Bitmap newimage = getBitmapFromAssets(color + "/45mirror.png", 40);
                        ImageView cell = cells.get(position);
                        cell.setImageBitmap(newimage);
                        cells.set(position, cell);

                        Mirror mirror = new Mirror(color, position);
                        entityHandler.addMirror(level, mirror, position);
                    }
                }
            }
        });
    }

    public ArrayList<ImageView> displayIfCollector(char type, int position, String color, ArrayList<ImageView> cells){
        ImageView cell = new ImageView(getApplicationContext());
        Bitmap offboardcollector = getBitmapFromAssets(color+"/offboardcollector.png", 40);
        Bitmap onboardcollectoroff = getBitmapFromAssets(color+"/onboardcollectoroff.png",  40);
        if(type == 'c'){
            //top
            if(position < (length-1)){
                cell = addImage(offboardcollector,90);
                cells.set(position, cell);
                cell = addImage(onboardcollectoroff, 90);
                cells.set((position+length), cell);

                Collector collector = new Collector(color, position+length);
                entityHandler.addEntity(level, collector, position+length);
            }
            //left
            else if(position % length == 0){
                cell = addImage(offboardcollector,0);
                cells.set(position, cell);
                cell = addImage(onboardcollectoroff, 0);
                cells.set((position+1), cell);

                Collector collector = new Collector(color, position+1);
                entityHandler.addEntity(level, collector, position+1);
            }
            //right
            else if(position %  length == length-1){
                cell = addImage(offboardcollector,180);
                cells.set(position, cell);
                cell = addImage(onboardcollectoroff, 180);
                cells.set((position-1), cell);

                Collector collector = new Collector(color, position-1);
                entityHandler.addEntity(level, collector, position-1);
            }
            //bottom
            else if(position > (length * (length-1))){
                cell = addImage(offboardcollector,270);
                cells.set(position, cell);
                cell = addImage(onboardcollectoroff, 270);
                cells.set((position-length), cell);

                Collector collector = new Collector(color, position-length);
                entityHandler.addEntity(level, collector, position-length);
            }
        }
        return cells;
    }

    public ArrayList<ImageView> displayIfEmitter(char type, int position, String color, ArrayList<ImageView> cells){
        ImageView cell = new ImageView(getApplicationContext());
        Bitmap offboardemitter = getBitmapFromAssets(color+"/offboardemitter.png", 40);
        Bitmap onboardemitteroff = getBitmapFromAssets(color+"/onboardemitteroff.png", 40);
        if(type == 'e'){
            //top
            if(position < (length-1)){
                cell = addImage(offboardemitter,90);
                cells.set(position, cell);
                cell = addImage(onboardemitteroff, 90);
                cells.set((position+length), cell);

                Emitter emitter = new Emitter(color, position+length);
                entityHandler.addEntity(level, emitter, position+length);
            }
            //left
            else if(position % length == 0){
                cell = addImage(offboardemitter,0);
                cells.set(position, cell);
                cell = addImage(onboardemitteroff, 0);
                cells.set((position+1), cell);

                Emitter emitter = new Emitter(color, position+1);
                entityHandler.addEntity(level, emitter, position+1);
            }
            //right
            else if(position %  length == length-1){
                cell = addImage(offboardemitter,180);
                cells.set(position, cell);
                cell = addImage(onboardemitteroff, 180);
                cells.set((position-1), cell);

                Emitter emitter = new Emitter(color, position-1);
                entityHandler.addEntity(level, emitter, position-1);
            }
            //bottom
            else if(position > (length * (length-1))){
                cell = addImage(offboardemitter,270);
                cells.set(position, cell);
                cell = addImage(onboardemitteroff, 270);
                cells.set((position-length), cell);

                Emitter emitter = new Emitter(color, position-length);
                entityHandler.addEntity(level, emitter, position-length);
            }
        }
        return cells;
    }

    public ImageView addImage(Bitmap image, int angle){
        ImageView cell = new ImageView(getApplicationContext());
        manipulator.newimage(image, getApplicationContext());
        image = manipulator.rotateImage(angle);
        cell.setImageBitmap(image);
        return cell;
    }

    public void displayFinish(){
        startButton.setEnabled(false);
        RelativeLayout endscreen = new RelativeLayout(this);
        LinearLayout buttonslayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        endscreen.setBackgroundColor(Color.argb(255, 209, 226, 255));
        endscreen.setLayoutParams(params);

        TextView endmessage = new TextView(this);
        endmessage.setText("Winner");
        endmessage.setTextSize(25);
        RelativeLayout.LayoutParams messageparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        messageparams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        Button levelselect = new Button(this);
        levelselect.setText("Return");
        levelselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelActivity.super.onBackPressed();
            }
        });

        Button replay = new Button(this);
        replay.setText("Replay");
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < entities.size(); i++){
                    entities.get(i).setReceived(false);
                }
                level.getBeams().clear();
                level.getMirrors().clear();
                for (ColorableEntity i : level.getEntities()) {
                    if (i.getIdentifier() == 'c') {
                        i.setReceived(false);
                    }
                }
                startButton.setEnabled(true);
                endwindow.dismiss();
                level.isRunning = false;
                startButton.setText("Start");
                startButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        start(v);
                    }
                });
                drawLevel();
            }
        });

        Button nextlevel = new Button(this);
        nextlevel.setText("Next");
        nextlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LevelActivity.class);
                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("levelNum", levelNum + 1);
                i.putExtra("isTutorial", isTutorial);
                startActivity(i);
            }
        });
        nextlevel.setEnabled(level.nextLevelExists);

        buttonslayout.addView(levelselect);
        buttonslayout.addView(replay);
        buttonslayout.addView(nextlevel);

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        endscreen.addView(endmessage, messageparams);


        endscreen.addView(buttonslayout, buttonParams);

        endwindow = new PopupWindow(endscreen, 900, 300);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
                endwindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            }

        }, 1000);
    }

    public void displayBeamMirror(Bitmap beamimage, Bitmap currentimage, ArrayList<ImageView> cells, int currentposition, int angle){
        ImageView cell;
        ImageManipulator manipulator = new ImageManipulator(beamimage, getApplicationContext());
        beamimage = manipulator.rotateImage(angle);
        currentimage = manipulator.overlayImages(currentimage, beamimage);
        cell = cells.get(currentposition);
        cell.setImageBitmap(currentimage);
        cells.set(currentposition, cell);
    }
}
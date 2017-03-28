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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {

    private Level level;
    private int levelNum;
    private boolean isTutorial;
    private String color;
    private ArrayList<ImageView> cells;
    private ImageManipulator manipulator;
    private EntityHandler entityHandler = new EntityHandler();
    private LevelFactory levelFactory;
    private ArrayList<LevelEntity> entities;
    private ImageView colorWheel;
    private ImageView indicator;
    private ImageView mirrorPreview;
    private Button startButton;
    private PopupWindow endwindow;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manipulator = new ImageManipulator(this.getApplicationContext());

        Intent intent = getIntent();
        levelNum = intent.getIntExtra("levelNum", 1);
        isTutorial = intent.getBooleanExtra("isTutorial", false);
        levelFactory = new LevelFactory(levelNum, isTutorial, getApplicationContext());

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level);

        startButton = (Button)findViewById(R.id.startButton);
        indicator = (ImageView)findViewById(R.id.indicator);

        level = levelFactory.generateLevel();
        length = level.getSideLength();
        entities = level.getEntities();

        TextView levelnumber = (TextView) findViewById(R.id.levelnumber);
        levelnumber.setText(Integer.toString(level.levelNum));

        colorWheel = (ImageView) findViewById(R.id.colorWheel);
        colorWheel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (level.isRunning)
                    return false;
                double xp, yp;
                //center of the circle
                double xc = v.getHeight()/2;
                double yc = v.getWidth()/2;

                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    xp = event.getX();
                    yp = event.getY();

                    //30 Degrees = 0.523599 Radians, which is the angle that the wheel dividers are on
                    double angle = Math.atan2(Math.abs(yc-yp),Math.abs(xc-xp));

                    if(Math.sqrt(((xc-xp)*(xc-xp)) + ((yp-yc)*(yp-yc))) < v.getHeight()/4)
                        color = "white";
                    else if(xp > xc) {
                        if (yp < yc) {
                            if (angle > 0.523599)
                                color = "red";
                            else
                                color = "yellow";
                        } else {
                            if (angle > 0.523599)
                                color = "green";
                            else
                                color = "yellow";
                        }
                    }
                    else if(xp < xc) {
                        if (yp < yc) {
                            if (angle > 0.523599)
                                color = "magenta";
                            else
                                color = "blue";
                        } else {
                            if (angle > 0.523599)
                                color = "cyan";
                            else
                                color = "blue";
                        }
                    }
                }
                Bitmap indicatorRing = getBitmapFromAssets(color+"/indicator.png", manipulator.dpToPx(150));
                indicator.setImageBitmap(indicatorRing);
                updateMirrorPreview();
                return true;
            }
        });

        drawLevel();
        color = "white";
        Bitmap indicatorRing = getBitmapFromAssets(color+"/indicator.png", manipulator.dpToPx(150));
        indicator.setImageBitmap(indicatorRing);

        mirrorPreview = (ImageView)findViewById(R.id.mirrorPreview);
        mirrorPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.isRunning)
                    return;
                level.mirrorIsTLBR = !level.mirrorIsTLBR;
                updateMirrorPreview();
            }
        });

        updateMirrorPreview();

        if(level instanceof TutorialLevel)
            displayTextBox();
    }

    private void updateMirrorPreview() {
        Bitmap mirrorToDraw = getBitmapFromAssets(color+"/45mirror.png", 150);
        if (!level.mirrorIsTLBR) {
            mirrorToDraw = manipulator.rotateImage(mirrorToDraw, 90);
        }
        mirrorPreview.setImageBitmap(mirrorToDraw);
    }

    /*
    * Gets a bitmap image from the assets folder and scales it
    * */
    public Bitmap getBitmapFromAssets(String filename, int dptopx){
        AssetManager assetManager = getAssets();

        try {
            InputStream istr = assetManager.open(filename);
            Bitmap image = BitmapFactory.decodeStream(istr);
            int newdimensions = manipulator.dpToPx(dptopx);
            image = manipulator.scale(newdimensions, image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * Stops the simulation, sets collectors received values back to false, and redraws the board without the beams
    * */
    public void stop(View v) {
        level.isRunning = false;
        startButton.setText("Start");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start(v);
            }
        });
        for (LevelEntity i : level.getEntities()) {
            if (i instanceof Collector) {
                ((Collector)i).setReceived(false);
            }
        }
        level.getBeams().clear();
        drawLevel();

    }

    /*
    * Draws the beams on the screen, and checks if the game has been won
    * Calls the displayWin method if the player has successfully completed the level
    * */
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
            if (entities.get(i) instanceof Emitter) {
                position = entities.get(i).getPosition();
                color = ((ColorableEntity)entities.get(i)).getColor();
                int size = length * length;
                image = getBitmapFromAssets(color + "/onboardemitteron.png", 40);

                if (position < length - 1) {
                    cell = cells.get(position + length);
                    image = manipulator.rotateImage(image, 90);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('d', color, position + length);
                    entityHandler.moveBeam(level, beam, position + length);

                } else if (position > (size - length)) {
                    cell = cells.get(position - length);
                    image = manipulator.rotateImage(image, 270);
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
                    image = manipulator.rotateImage(image, 180);
                    cell.setImageBitmap(image);
                    cells.set(position, cell);
                    beam = new Beam('l', color, position - 1);
                    entityHandler.moveBeam(level, beam, position - 1);
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            if(entities.get(i) instanceof Collector) {
                if (((Collector)entities.get(i)).getReceived() == true) {
                    Bitmap onboardcollectoron = getBitmapFromAssets(((ColorableEntity)entities.get(i)).getColor() + "/onboardcollectoron.png", 40);
                    int location = entities.get(i).getPosition();
                    if (location < 2 * length - 1) {
                        onboardcollectoron = manipulator.rotateImage(onboardcollectoron, 90);
                    } else if (location > (length * length - 2 * length)) {
                        onboardcollectoron = manipulator.rotateImage(onboardcollectoron, 270);

                    } else if (location % length == 1) {
                        onboardcollectoron = manipulator.rotateImage(onboardcollectoron, 0);

                    } else if (location % length == length - 2) {
                        onboardcollectoron = manipulator.rotateImage(onboardcollectoron, 180);
                    }
                    cell = cells.get(entities.get(i).getPosition());
                    cell.setImageBitmap(onboardcollectoron);
                    cells.set(location, cell);
                }
            }
        }

        displayBeams();

        if (level.checkWin()) {
            displayFinish();
        }
    }

    /*
    * Uses the Entity array to draw the board and all of its entities
    * Draws an empty board first and then replaces cells with images of their entities
    * */
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

            type = 'x';
            if(entities.get(i) instanceof Wall){
                ImageView cell = new ImageView(getApplicationContext());
                position = entities.get(i).getPosition();
                cell.setImageBitmap(wallcell);
                cells.set(position, cell);
            }
            else {
                if (entities.get(i) instanceof Collector)
                    type = 'c';
                else if (entities.get(i) instanceof Emitter)
                    type = 'e';
                position = entities.get(i).getPosition();
                color = ((ColorableEntity) entities.get(i)).getColor();
                cells = displayIfCollector(type, position, color, cells);
                cells = displayIfEmitter(type, position, color, cells);
            }
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

        for (LevelEntity i : level.getEntities()) {
            if(i instanceof Mirror) {
                Bitmap newimage = getBitmapFromAssets(((Mirror)i).getColor() + "/45mirror.png", 40);
                if (((Mirror)i).getAngle() == 135) {
                    newimage = manipulator.rotateImage(newimage, 90);
                }
                ImageView cell = cells.get(i.getPosition());
                cell.setImageBitmap(newimage);
                cells.set(i.getPosition(), cell);
            }
        }
    }

    /*
    * Makes the cells on the board clickable
    *
    * When a cell is clicked, this method checks the contents of the clicked cell and:
    *     1. places a mirror of the selected color(from color wheel)
    *     2. deletes the mirror inside the touched cell
    *     3. flips the mirror contained in the touched cell
    * Based on the toggle that is selected
    * */
    private void setOnClick(final ImageView clickableimage, final int position) {
        clickableimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.isRunning)
                    return;
                entities = level.getEntities();
                boolean wallCheck = false;
                boolean entityCheck = false;
                boolean mirrorCheck = false;

                //check contents of player selected cell
                if(entities.size()>0)
                    for(int i = 0; i < entities.size(); i++)
                        if(position == entities.get(i).getPosition()) {
                            if(entities.get(i) instanceof Mirror)
                                mirrorCheck = true;
                            else if(entities.get(i) instanceof Wall)
                                wallCheck = true;
                            else
                                entityCheck = true;
                        }

                //touched cell containing mirror
                if(mirrorCheck){
                    Bitmap emptycell = getBitmapFromAssets("emptycell.png", 40);
                    clickableimage.setImageBitmap(emptycell);
                    entityHandler.removeMirror(level, position);
                }

                //touched empty cell
                else if(!wallCheck && !entityCheck && !mirrorCheck){
                    //place a new mirror
                    Bitmap newimage = getBitmapFromAssets(color+"/45mirror.png", 40);
                    if (!level.mirrorIsTLBR) {
                        newimage = manipulator.rotateImage(newimage, 90);
                    }
                    ImageView cell = cells.get(position);
                    cell.setImageBitmap(newimage);
                    cells.set(position, cell);

                    Mirror mirror = new Mirror(color, position);
                    entityHandler.addMirror(level, mirror);
                    if (!level.mirrorIsTLBR) {
                        entityHandler.flipMirror(level, position);
                    }
                }
            }
        });
    }

    /*
    * If the entity inside the given cell is a collector, it will display the corresponding image on the game board
    * */
    public ArrayList<ImageView> displayIfCollector(char type, int position, String color, ArrayList<ImageView> cells){
        ImageView cell;
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

    /*
    * If the entity inside the given cell is an emitter, it will display the corresponding image on the game board
    * */
    public ArrayList<ImageView> displayIfEmitter(char type, int position, String color, ArrayList<ImageView> cells){
        ImageView cell;
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

    /*
    * Rotates an image and places it inside a cell to be displayed on the board
    * */
    public ImageView addImage(Bitmap image, int angle){
        ImageView cell = new ImageView(getApplicationContext());
        image = manipulator.rotateImage(image, angle);
        cell.setImageBitmap(image);
        return cell;
    }

    /*
    * Displays the end game dialog containing the player score as well as
    * buttons to advance to the next level, replay the current level, and return to the previous screen
    * */
    public void displayFinish(){
        startButton.setEnabled(false);
        RelativeLayout endscreen = new RelativeLayout(this);
        LinearLayout buttonslayout = new LinearLayout(this);
        LinearLayout starlayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        endscreen.setBackgroundColor(Color.argb(255, 209, 226, 255));
        endscreen.setLayoutParams(params);

        TextView endmessage = new TextView(this);
        endmessage.setText("Winner");
        endmessage.setTextSize(25);
        RelativeLayout.LayoutParams messageparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        messageparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        messageparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);


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
            level.getBeams().clear();
            for (LevelEntity i : level.getEntities())
                if (i instanceof Collector)
                    ((Collector)i).setReceived(false);
            level.removeMirrors();
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
            color = "white";
            Bitmap indicatorRing = getBitmapFromAssets(color+"/indicator.png", 150);
            indicator.setImageBitmap(indicatorRing);
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

        Bitmap emptyStar = getBitmapFromAssets("EmptyStar.png",40);
        Bitmap filledStar = getBitmapFromAssets("FilledStar.png",40);

        ImageView star1 = new ImageView(this);
        ImageView star2 = new ImageView(this);
        ImageView star3 = new ImageView(this);

        star1.setImageBitmap(filledStar);

        if(level.calculateScore() == 3){
            star2.setImageBitmap(filledStar);
            star3.setImageBitmap(filledStar);
        }
        else if(level.calculateScore() == 2){
            star2.setImageBitmap(filledStar);
            star3.setImageBitmap(emptyStar);
        }
        else{
            star2.setImageBitmap(emptyStar);
            star3.setImageBitmap(emptyStar);
        }

        starlayout.addView(star1);
        starlayout.addView(star2);
        starlayout.addView(star3);

        RelativeLayout.LayoutParams starParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        starParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        starlayout.setPadding(0,50,0,100);

        endscreen.addView(endmessage, messageparams);
        endscreen.addView(starlayout, starParams);
        endscreen.addView(buttonslayout, buttonParams);

        endwindow = new PopupWindow(endscreen, RelativeLayout.LayoutParams.WRAP_CONTENT, 430);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
                endwindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            }

        }, 1000);
    }

    /*
    * Displays the images of the beams that are passing through or being reflected off of a mirror
    * Uses incoming direction, outgoing direction and mirror angle to determine the image and the angle it should be rotated
    * and then places that image on top of the cell
    * */
    public void displayBeamMirror(Bitmap beamimage, Bitmap currentimage, ArrayList<ImageView> cells, int currentposition, int angle){
        ImageView cell;
        beamimage = manipulator.rotateImage(beamimage, angle);
        currentimage = manipulator.overlayImages(currentimage, beamimage);
        cell = cells.get(currentposition);
        cell.setImageBitmap(currentimage);
        cells.set(currentposition, cell);
    }

    /*
    * Displays all beams on the board
    * */
    public void displayBeams() {
        int beamposition;
        ImageView cell;
        Bitmap background, straightbeam;

        for(int i = 0; i < level.getBeams().size(); i++){
            beamposition = level.getBeams().get(i).getPosition();
            char beamdirection = level.getBeams().get(i).getDirection();
            color = level.getBeams().get(i).getColor();
            ArrayList<Beam> beamlist = level.checkCellForBeam(beamposition);

            for (int k = 0; k < beamlist.size(); k++) {
                if(beamlist.get(k).getDirection() == beamdirection || beamlist.get(k).getOppositeDirection() == beamdirection)
                    color = entityHandler.combineColors(color, beamlist.get(k).getColor());
            }

            straightbeam = getBitmapFromAssets(color + "/straightbeam.png", 40);

            if(level.checkCellForEntity(beamposition) == null){
                if(level.getBeams().get(i).getDirection() == 'l' || level.getBeams().get(i).getDirection() == 'r'){
                    background = ((BitmapDrawable) cells.get(beamposition).getDrawable()).getBitmap();
                    straightbeam = manipulator.rotateImage(straightbeam, 90);
                    straightbeam = manipulator.overlayImages(background, straightbeam);
                    cell = cells.get(beamposition);
                    cell.setImageBitmap(straightbeam);
                    cells.set(beamposition, cell);
                }
                else{
                    background = ((BitmapDrawable) cells.get(beamposition).getDrawable()).getBitmap();
                    straightbeam = manipulator.overlayImages(background, straightbeam);
                    cell = cells.get(beamposition);
                    cell.setImageBitmap(straightbeam);
                    cells.set(beamposition, cell);
                }
            }
            if(level.checkCellForEntity(beamposition) instanceof Mirror){
                Beam beam = level.getBeams().get(i);
                String currentColor = beam.getColor();
                char currentDirection = beam.getDirection();
                Mirror currentMirror = ((Mirror) level.checkCellForEntity(beamposition));
                Bitmap BeamTopRight = getBitmapFromAssets(currentColor + "/mirrorbeamtopright.png", 40);
                Bitmap BeamTopLeft = getBitmapFromAssets(currentColor + "/mirrorbeamtopleft.png", 40);
                Bitmap currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                int mirrorposition = currentMirror.getPosition();

                if(entityHandler.isComponent(level.getBeams().get(i).getColor(), (((Mirror) level.checkCellForEntity(beamposition)).getColor()))){
                    //beam is component mirror, completely reflected
                    if (currentMirror.getAngle() == 45) {
                        if (currentDirection == 'u') {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 90);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 180);
                        } else if (currentDirection == 'd') {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 270);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 0);
                        } else if (currentDirection == 'l') {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 0);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 270);
                        } else {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 180);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 90);
                        }
                    } else{
                        if (currentDirection == 'u') {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 90);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 0);
                        } else if (currentDirection == 'd') {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 270);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 180);
                        } else if (currentDirection == 'l') {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 0);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 90);
                        } else {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 180);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 270);
                        }
                    }
                }
                else {
                    //beam is not component of mirror, passes straight through
                    if (currentMirror.getAngle() == 45) {
                        if (currentDirection == 'u' || currentDirection == 'd') {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 90);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 270);
                        } else {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 0);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 180);
                        }
                    } else {
                        if (currentDirection == 'u' || currentDirection == 'd') {
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 90);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopLeft, currentimage, cells, mirrorposition, 270);
                        } else {
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 0);
                            currentimage = ((BitmapDrawable) cells.get(currentMirror.getPosition()).getDrawable()).getBitmap();
                            displayBeamMirror(BeamTopRight, currentimage, cells, mirrorposition, 180);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < level.getEntities().size(); i++){
            if(level.getEntities().get(i) instanceof Emitter){
                int nextPos;
                char direction;
                int currentPos = level.getEntities().get(i).getPosition();
                String color = ((Emitter)level.getEntities().get(i)).getColor();

                if(currentPos%length == 1) {
                    if (level.checkCellForEntity(currentPos - 1) instanceof Emitter) {
                        nextPos = currentPos + 1;
                        direction = 'r';
                    } else if (level.checkCellForEntity(currentPos - length) instanceof Emitter) {
                        nextPos = currentPos + length;
                        direction = 'd';
                    } else {
                        nextPos = currentPos - length;
                        direction = 'u';
                    }
                }
                else if(currentPos%length == length-2) {
                    if (level.checkCellForEntity(currentPos + 1) instanceof Emitter) {
                        nextPos = currentPos - 1;
                        direction = 'l';
                    } else if (level.checkCellForEntity(currentPos - length) instanceof Emitter) {
                        nextPos = currentPos + length;
                        direction = 'd';
                    } else {
                        nextPos = currentPos - length;
                        direction = 'u';
                    }
                }
                else if(currentPos < 2*length-1 && currentPos > length) {
                    nextPos = currentPos + length;
                    direction = 'd';
                }
                else {
                    nextPos = currentPos - length;
                    direction = 'u';
                }

                ArrayList<Beam> beamlist = level.checkCellForBeam(currentPos);

                for (int k = 0; k < beamlist.size(); k++) {
                    if(direction == beamlist.get(k).getDirection() || direction == beamlist.get(k).getOppositeDirection())
                        color = entityHandler.combineColors(color, beamlist.get(k).getColor());
                }

                Bitmap topleft = getBitmapFromAssets(color+"/mirrorbeamtopleft.png", 40);
                Bitmap topright = getBitmapFromAssets(color+"/mirrorbeamtopright.png", 40);

                placenearbeam(nextPos, topright, topleft, direction);
            }
        }

        for(int i = 0; i < level.getBeams().size(); i++){
            int nextPos;
            char direction = level.getBeams().get(i).getDirection();
            int currentPos = level.getBeams().get(i).getPosition();
            String color = level.getBeams().get(i).getColor();

            if(direction == 'u')
                nextPos = currentPos - length;
            else if(direction == 'd')
                nextPos = currentPos + length;
            else if(direction == 'l')
                nextPos = currentPos - 1;
            else
                nextPos = currentPos + 1;

            ArrayList<Beam> beamlist = level.checkCellForBeam(currentPos);

            for (int k = 0; k < beamlist.size(); k++) {
                if(direction == beamlist.get(k).getDirection() || direction == beamlist.get(k).getOppositeDirection())
                    color = entityHandler.combineColors(color, beamlist.get(k).getColor());
            }

            Bitmap topleft = getBitmapFromAssets(color+"/mirrorbeamtopleft.png", 40);
            Bitmap topright = getBitmapFromAssets(color+"/mirrorbeamtopright.png", 40);

            placenearbeam(nextPos, topright, topleft, direction);
        }
    }

    /*
    * Displays a tutorial Text box on the screen above the game board
    * */
    public void displayTextBox(){
        RelativeLayout tutorialbox = new RelativeLayout(this);
        LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tutorialbox.setBackgroundColor(Color.argb(255, 209, 226, 255));
        tutorialbox.setLayoutParams(tparams);

        TextView message = new TextView(this);
        String textMessage;
        TextBox textBox = ((TutorialLevel)level).getTextbox();
        textMessage = textBox.getMessage();
        message.setText(textMessage);
        message.setTextSize(18);
        RelativeLayout.LayoutParams tmessageparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tmessageparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        tutorialbox.addView(message, tmessageparams);

        int width = manipulator.dpToPx(400);

        endwindow = new PopupWindow(tutorialbox, width, RelativeLayout.LayoutParams.WRAP_CONTENT);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // close your dialog
                endwindow.showAtLocation(findViewById(android.R.id.content), Gravity.TOP, 0,250);
            }

        }, 0);
    }


    public void placenearbeam(int nextPos, Bitmap topright, Bitmap topleft, char direction){


        Bitmap background;
        Mirror nextMirror;
        ImageView cell;

        if(level.checkCellForEntity(nextPos) instanceof Mirror){
            nextMirror = ((Mirror)level.checkCellForEntity(nextPos));
            if(nextMirror.getAngle() == 45) {
                if (direction == 'u') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topright = manipulator.rotateImage(topright, 270);
                    topright = manipulator.overlayImages(background, topright);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topright);
                    cells.set(nextPos, cell);
                } else if (direction == 'd') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topright = manipulator.rotateImage(topright, 90);
                    topright = manipulator.overlayImages(background, topright);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topright);
                    cells.set(nextPos, cell);
                } else if (direction == 'l') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topleft = manipulator.rotateImage(topleft, 180);
                    topleft = manipulator.overlayImages(background, topleft);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topleft);
                    cells.set(nextPos, cell);
                } else {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topleft = manipulator.rotateImage(topleft, 0);
                    topleft = manipulator.overlayImages(background, topleft);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topleft);
                    cells.set(nextPos, cell);
                }
            }
            else {
                if (direction == 'u') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topleft = manipulator.rotateImage(topleft, 270);
                    topleft = manipulator.overlayImages(background, topleft);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topleft);
                    cells.set(nextPos, cell);
                } else if (direction == 'd') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topleft = manipulator.rotateImage(topleft, 90);
                    topleft = manipulator.overlayImages(background, topleft);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topleft);
                    cells.set(nextPos, cell);
                } else if (direction == 'l') {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topright = manipulator.rotateImage(topright, 180);
                    topright = manipulator.overlayImages(background, topright);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topright);
                    cells.set(nextPos, cell);
                } else {
                    background = ((BitmapDrawable) cells.get(nextPos).getDrawable()).getBitmap();
                    topright = manipulator.rotateImage(topright, 0);
                    topright = manipulator.overlayImages(background, topright);
                    cell = cells.get(nextPos);
                    cell.setImageBitmap(topright);
                    cells.set(nextPos, cell);
                }
            }
        }
    }
}
package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    private ArrayList<ColorableEntity> entities;
    private ArrayList<Wall> walls;
    private int sideLength;

    //get entites array from database

    public void setEntities(){
        entities = new ArrayList<>();
        walls = new ArrayList<>();

        //test code to add elements to the level
        //will be taken from database
        sideLength = 10;
        Wall wall = new Wall(26);
        walls.add(wall);
        Emitter emitter = new Emitter("Red", 2);
        entities.add(emitter);
        Emitter emitter2 = new Emitter("Blue", 4);
        entities.add(emitter2);
        Emitter emitter3 = new Emitter("Green", 3);
        entities.add(emitter3);
        Collector collector1  = new Collector("Blue", 93);
        entities.add(collector1);
        Collector collector2  = new Collector("Green", 59);
        entities.add(collector2);

    }

    //instantiates a Level and passes in the entity array
    public Level generateLevel(){
        Level level = new Level(entities, walls, sideLength);
        return level;
    }


}

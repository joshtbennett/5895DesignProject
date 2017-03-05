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
        Emitter emitter = new Emitter("Red", 2);
        entities.add(emitter);
        Emitter emitter2 = new Emitter("Blue", 20);
        entities.add(emitter2);
        Emitter emitter3 = new Emitter("Green", 94);
        entities.add(emitter3);
        Emitter emitter4 = new Emitter("Yellow" , 59);
        entities.add(emitter4);
        Collector collector  = new Collector("Red", 92);
        entities.add(collector);
        Collector collector1  = new Collector("Blue", 29);
        entities.add(collector1);
        Collector collector4  = new Collector("Green", 4);
        entities.add(collector4);
        Collector collector3  = new Collector("Yellow", 50);
        entities.add(collector3);
    }

    //instantiates a Level and passes in the entity array
    public Level generateLevel(){
        Level level = new Level(entities, walls, sideLength);
        return level;
    }


}

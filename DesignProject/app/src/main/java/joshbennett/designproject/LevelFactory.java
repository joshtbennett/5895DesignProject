package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    private ArrayList<ColorableEntity> entities;
    private ArrayList<Wall> walls;

    //get entites array from database

    public void setEntities(){
        entities = new ArrayList<>(100);
        walls = new ArrayList<>(100);

        //test code to add elements to the level
        //will be taken from database

        Emitter emitter = new Emitter("Red");
        Collector collector  = new Collector("White");
        Wall wall = new Wall();
        for(int i = 0; i < 100; i++) {
            if(i == 4) {
                entities.add(collector);
                walls.add(null);
            }
            else if(i ==  8){
                entities.add(emitter);
                walls.add(null);

            }
            else if(i == 32) {
                entities.add(null);
                walls.add(wall);
            }
            else{
                entities.add(null);
                walls.add(null);
            }
        }
    }

    //instantiates a Level and passes in the entity array
    public Level generateLevel(){
        Level level = new Level(entities, walls);
        return level;
    }


}

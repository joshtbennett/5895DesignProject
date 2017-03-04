package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    private ArrayList<LevelEntity> entities;

    //get entites array from database

    public void setEntities(){
        entities = new ArrayList<>(100);

        //test code to add elements to the level
        //will be taken from database

        Emitter emitter = new Emitter("Red");
        Collector collector  = new Collector("Yellow");
        Wall wall = new Wall();
        for(int i = 0; i < 100; i++) {
            if(i == 4) {
                entities.add(collector);
            }
            else if(i ==  97){
                entities.add(emitter);
            }
            else if(i == 32) {
                entities.add(null);
            }
            else{
                entities.add(null);
            }
        }
    }

    //instantiates a Level and passes in the entity array
    public Level generateLevel(){
        Level level = new Level(entities);
        return level;
    }


}

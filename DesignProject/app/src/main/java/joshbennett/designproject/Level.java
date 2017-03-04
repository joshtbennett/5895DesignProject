package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    ArrayList<LevelEntity> entities;

    public Level(ArrayList<LevelEntity> entities){
        this.entities = entities;
    }

    boolean checkWin(){return false;}

    int calculateScore(){return 1;}

    void start(){}

    public ArrayList<LevelEntity> getEntities(){
        return entities;
    }
}

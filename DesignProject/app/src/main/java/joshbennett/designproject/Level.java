package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    ArrayList<ColorableEntity> entities;
    private ArrayList<Wall> walls;
    private ArrayList<Mirror> mirrors;

    public Level(ArrayList<ColorableEntity> entities, ArrayList<Wall> walls){
        this.entities = entities;
        this.walls = walls;
        mirrors = new ArrayList<>(100);
        for(int i = 0; i < 100; i++){
            mirrors.add(null);
        }
    }

    boolean checkWin(){return false;}

    int calculateScore(){return 1;}

    void start(){}

    public ArrayList<ColorableEntity> getEntities(){
        return entities;
    }

    public ArrayList<Wall> getWalls(){
        return walls;
    }

    public ArrayList<Mirror> getMirrors (){
        return mirrors;
    }

    public void setEntities(ArrayList<ColorableEntity> entities){ this.entities = entities; }
    public void setWalls(ArrayList<Wall> wallss){ this.walls = walls; }
    public void setMirrors(ArrayList<Mirror> mirrors){ this.mirrors = mirrors; }
}

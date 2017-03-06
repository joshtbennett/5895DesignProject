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
    private ArrayList<Beam> beams;
    private int sideLength;

    public Level(ArrayList<ColorableEntity> entities, ArrayList<Wall> walls,  int sideLength){
        this.sideLength = sideLength;
        this.entities = entities;
        this.walls = walls;
        mirrors = new ArrayList<>();
        beams = new ArrayList<>();
    }

    boolean checkWin(){return false;}

    int calculateScore(){return 1;}

    void start(){}

    public int getSideLength(){ return sideLength; }
    public ArrayList<ColorableEntity> getEntities(){return entities; }
    public ArrayList<Wall> getWalls(){
        return walls;
    }
    public ArrayList<Mirror> getMirrors(){
        return mirrors;
    }
    public ArrayList<Beam> getBeams(){ return beams; }

    public void setEntities(ArrayList<ColorableEntity> entities){ this.entities = entities; }
    public void setWalls(ArrayList<Wall> walls){ this.walls = walls; }
    public void setMirrors(ArrayList<Mirror> mirrors){ this.mirrors = mirrors; }
    public void setBeams(ArrayList<Beam> beams){ this.beams = beams; }
}

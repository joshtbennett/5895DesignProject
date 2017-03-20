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
    public boolean isRunning = false;
    public int levelNum;
    public boolean nextLevelExists;

    public Level(ArrayList<ColorableEntity> entities, ArrayList<Wall> walls,  int sideLength){
        this.sideLength = sideLength;
        this.entities = entities;
        this.walls = walls;
        mirrors = new ArrayList<>();
        beams = new ArrayList<>();
    }

    int calculateScore(){
        int idealScore = 1;
        int playerScore = mirrors.size();

        if(playerScore <= idealScore)
            return 3;
        if(idealScore < playerScore && playerScore < 1.5*idealScore)
            return 2;
        return 1;
    }

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
    public boolean checkWin(){
        boolean win = true;
        int numberOfCollectors = 0, numberOfReceivedCollectors = 0;
        for(int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getIdentifier() == 'c') {
                numberOfCollectors++;
                if (entities.get(i).getReceived() == true)
                    numberOfReceivedCollectors++;
            }
        }
        if(numberOfCollectors/2 == numberOfReceivedCollectors)
            return true;
        else
            return false;
    }

}

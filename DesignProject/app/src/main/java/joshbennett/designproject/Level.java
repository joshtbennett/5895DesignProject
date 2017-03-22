package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    ArrayList<LevelEntity> entities;
    private ArrayList<Beam> beams;
    private int sideLength;
    public boolean isRunning = false;
    public int levelNum;
    public boolean nextLevelExists;

    public Level(ArrayList<LevelEntity> entities,  int sideLength){
        this.sideLength = sideLength;
        this.entities = entities;
        beams = new ArrayList<>();
    }

    int calculateScore(){
        int idealScore = 1;
        int playerScore = 0;

        for(int i = 0; i < entities.size(); i++)
            if(entities.get(i) instanceof Mirror)
                playerScore++;

        if(playerScore <= idealScore)
            return 3;
        if(idealScore < playerScore && playerScore < 1.5*idealScore)
            return 2;
        return 1;
    }

    public int getSideLength(){ return sideLength; }
    public ArrayList<LevelEntity> getEntities(){return entities; }
    public ArrayList<Beam> getBeams(){ return beams; }

    public void setEntities(ArrayList<LevelEntity> entities){ this.entities = entities; }
    public boolean checkWin(){
        int numberOfCollectors = 0, numberOfReceivedCollectors = 0;
        for(int i = 0; i < entities.size(); i++) {
            if (entities.get(i) instanceof Collector) {
                numberOfCollectors++;
                if (((Collector)entities.get(i)).getReceived() == true)
                    numberOfReceivedCollectors++;
            }
        }
        if(numberOfCollectors/2 == numberOfReceivedCollectors)
            return true;
        else
            return false;
    }

}

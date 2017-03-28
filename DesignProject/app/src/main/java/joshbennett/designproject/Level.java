package joshbennett.designproject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    ArrayList<LevelEntity> entities;
    private ArrayList<Beam> beams;
    private int sideLength;
    public int par;
    public boolean isRunning = false;
    public int levelNum;
    public boolean nextLevelExists;
    public boolean mirrorIsTLBR = true;

    public Level(ArrayList<LevelEntity> entities,  int sideLength, int par){
        this.sideLength = sideLength;
        this.entities = entities;
        this.par = par;
        beams = new ArrayList<>();
    }

    /*
    *   Compares the number of Mirrors used by the player to the predetermined par for the level to determine a score from 1 to 3 stars
    * */
    int calculateScore(){
        int idealScore = par;
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

    /*
    * returns the sidelength of the game board
    * */
    public int getSideLength(){ return sideLength; }

    /*
    * returns an array containing all of the emitters, collectors, mirrors, and walls currently on the board
    * */
    public ArrayList<LevelEntity> getEntities(){return entities; }

    /*
    * returns an array containing all the beams currently on the board
    * */
    public ArrayList<Beam> getBeams(){ return beams; }

    /*
    * Used to update the entities currently on the game board
    * */
    public void setEntities(ArrayList<LevelEntity> entities){ this.entities = entities; }

    /*
    * Checks the status of all the collectors on the board and returns true if every one has received its correct color
    * */
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

    /*
    * Checks a given cell on the game board and returns the enetity residing in that cell.
    * returns null if the cell has no entities
    * */
    public LevelEntity checkCellForEntity(int position){
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).getPosition() == position){
                return entities.get(i);
            }
        }
        return null;
    }

    /*
    * Returns an array of all the beams currently residing in a given cell on the game board
    * returns null if the cell has no beam
    * */
    public ArrayList<Beam> checkCellForBeam(int position){
        ArrayList<Beam> beamlist = new ArrayList<>();
        for(int i = 0; i < beams.size(); i++){
            if(beams.get(i).getPosition() == position){
                beamlist.add(beams.get(i));
            }
        }
        return beamlist;
    }

    public void removeMirrors() {
        for(int i = entities.size()-1; i > 0; i--)
            if(entities.get(i) instanceof Mirror)
                entities.remove(i);
    }

}

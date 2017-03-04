package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Beam implements ColorableEntity{

    private boolean red, green, blue;
    private String direction;
    private int xlocation, ylocation;
    private char identifier;

    Beam(int xlocation, int ylocation, boolean red, boolean blue, boolean green, String direction){

        identifier = 'b';

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.direction = direction;
        this.xlocation = xlocation;
        this.ylocation = ylocation;
    }

    public char getIdentifier(){
        return identifier;
    }

    //LevelEntity checkNextSquare(){ }
}

package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Beam implements ColorableEntity{

    private char direction;
    private char identifier;
    private String color;

    Beam(char direction, String color){

        identifier = 'b';
        this.direction = direction;
        this.color = color;
    }

    public char getIdentifier(){
        return identifier;
    }

    @Override
    public String getColor() {
        return color;
    }

    //LevelEntity checkNextSquare(){ }
}

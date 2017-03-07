package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Beam implements ColorableEntity{

    private char direction;
    private char identifier;
    private String color;
    private int position;

    Beam(char direction, String color, int position){
        this.position = position;
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

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setReceived(boolean received) {

    }

    @Override
    public boolean getReceived() {
        return false;
    }

    public char getDirection(){ return direction; }

    public void setColor(String color) {this.color = color;}
    //LevelEntity checkNextSquare(){ }
}

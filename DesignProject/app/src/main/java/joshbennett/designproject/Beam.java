package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Beam implements ColorableEntity{

    private char direction;
    private String color;
    private int position;

    Beam(char direction, String color, int position){
        this.position = position;
        this.direction = direction;
        this.color = color;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public int getPosition() {
        return position;
    }
    public char getDirection(){ return direction; }

    public void setColor(String color) {this.color = color;}

    public char getOppositeDirection(){
        if(direction == 'u')
            return 'd';
        if(direction == 'd')
            return 'u';
        if(direction == 'r')
            return 'l';
        return 'r';
    }
}

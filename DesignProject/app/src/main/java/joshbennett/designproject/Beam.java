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

    /*
    * Returns the color of the beam
    * */
    @Override
    public String getColor() {  return color;   }

    /*
    * Returns the current position of the beam
    * */
    @Override
    public int getPosition() {  return position;    }

    /*
    * Returns the direction that the beam is currently travelling
    * */
    public char getDirection(){ return direction; }

    /*
    * sets the color of the beam to the color passed in to the method
    * */
    public void setColor(String color) {this.color = color;}

    /*
    * returns the opposite direction of the one given to the method
    * */
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

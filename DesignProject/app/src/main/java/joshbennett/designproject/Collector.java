package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Collector implements LevelEntity, ColorableEntity{

    private boolean collected;
    private String color;
    private int position;

    Collector(String color, int position)
    {
        this.position = position;
        this.color = color;
        collected = false;
    }

    /*
    * Returns the color of the collector
    * */
    @Override
    public String getColor() { return color; }

    /*
    * returns the position of the collector
    * */
    @Override
    public int getPosition() {
        return position;
    }

    /*
    * Sets the status of the collector
    * */
    public void setReceived(boolean received) {
        collected = received;
    }

    /*
    * Returns the status of the collector
    * */
    public boolean getReceived(){
        return collected;
    }
}


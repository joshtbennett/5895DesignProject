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

    @Override
    public String getColor() { return color; }

    @Override
    public int getPosition() {
        return position;
    }

    public void setReceived(boolean received) {
        collected = received;
    }
    public boolean getReceived(){
        return collected;
    }
}


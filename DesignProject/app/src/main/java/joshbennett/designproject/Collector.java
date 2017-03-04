package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Collector implements LevelEntity, ColorableEntity{

    private boolean red, green, blue;
    private int xlocation, ylocation;
    private boolean collected;
    private char identifier;
    private String color;

    Collector(String color)
    {
        identifier = 'c';
        this.color = color;

        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.red = red;
        this.green = green;
        this.blue = blue;
        collected = false;
    }

    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getColor() {
        return color;
    }
}


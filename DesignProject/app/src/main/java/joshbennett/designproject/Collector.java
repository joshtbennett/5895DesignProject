package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Collector implements LevelEntity, ColorableEntity{

    private boolean collected;
    private char identifier;
    private String color;

    Collector(String color)
    {
        identifier = 'c';
        this.color = color;
        collected = false;
    }

    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getColor() { return color; }
}


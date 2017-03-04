package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Wall implements LevelEntity
{
    private char identifier;

    public Wall()
    {
        identifier = 'w';
    }


    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getColor() {
        return "none";
    }
}

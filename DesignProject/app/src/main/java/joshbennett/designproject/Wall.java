package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Wall implements LevelEntity
{
    private char identifier;
    private int position;

    public Wall(int position) {
        this.position = position;
        identifier = 'w';
    }

    @Override
    public char getIdentifier() {
        return identifier;
    }

    public int getPosition() {
        return position;
    }
}

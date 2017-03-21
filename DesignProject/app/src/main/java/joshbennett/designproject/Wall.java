package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Wall implements LevelEntity
{
    private int position;

    public Wall(int position) {
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
    }
}

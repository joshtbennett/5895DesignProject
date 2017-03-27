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

    /*
    * Returns the position of the wall
    * */
    @Override
    public int getPosition() {
        return position;
    }
}

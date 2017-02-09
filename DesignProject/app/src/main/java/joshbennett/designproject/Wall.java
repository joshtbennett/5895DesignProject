package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Wall implements LevelEntity
{
    private int xlocation, ylocation;

    Wall(int xlocation, int ylocation)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
    }
}

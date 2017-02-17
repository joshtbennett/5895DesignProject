package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Emitter implements LevelEntity, ColorableEntity
{
    private boolean red, green, blue;
    private int xlocation, ylocation;

    Emitter(int xlocation, int ylocation, boolean red, boolean blue, boolean green)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    void emit(){}
}

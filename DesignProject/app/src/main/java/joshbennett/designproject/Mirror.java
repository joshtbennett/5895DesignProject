package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Mirror implements LevelEntity, ColorableEntity
{
    private boolean red, green, blue;
    private int angle, xlocation, ylocation;

    Mirror(int xlocation, int ylocation, boolean red, boolean blue, boolean green)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.red = red;
        this.green = green;
        this.blue = blue;
        angle = 45;
    }

    void setPosition(){}

    void setAngle(int angle){}

}

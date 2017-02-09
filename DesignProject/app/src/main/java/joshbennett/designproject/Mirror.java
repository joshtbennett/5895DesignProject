package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Mirror implements LevelEntity
{
    private String color;
    private int angle, xlocation, ylocation;

    Mirror(int xlocation, int ylocation, String Color)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.color = Color;
        angle = 45;
    }

    void flipMirror(int angle)
    {
        angle = (angle==45)? 135:45;
    }

}

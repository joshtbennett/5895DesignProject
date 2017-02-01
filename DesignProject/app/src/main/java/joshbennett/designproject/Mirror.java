package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Mirror
{
    private String color;
    private int angle, xlocation, ylocation;

    Mirror(int x_location, int y_location, String Color)
    {
        this.xlocation = x_location;
        this.ylocation = y_location;
        this.color = Color;
        angle = 45;
    }

    void flipMirror(int angle)
    {
        angle = (angle==45)? 135:45;
    }

}

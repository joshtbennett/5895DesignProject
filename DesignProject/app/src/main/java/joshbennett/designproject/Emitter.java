package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Emitter
{
    private String color;
    private int angle, xlocation, ylocation;
    private boolean received;

    Emitter(int x_location, int y_location, String Color)
    {
        this.xlocation = x_location;
        this.ylocation = y_location;
        this.color = Color;

        received = false;

        /* emitters must be on the edges, and they are placed at 90 degree angles */
        if(x_location == 0)
            angle = 0;
        else if(x_location == GRIDWIDTH)
            angle = 180;
        else if(y_location == 0)
            angle = 270;
        else
            angle = 90;
    }
}

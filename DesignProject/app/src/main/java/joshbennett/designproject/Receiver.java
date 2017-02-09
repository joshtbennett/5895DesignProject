package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Receiver implements LevelEntity
{
    private String color;
    private int angle, xlocation, ylocation;

    Receiver(int xlocation, int ylocation, String color)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.color = color;

        /* emitters must be on the edges, and they are placed at 90 degree angles */
        if(xlocation == 0)
            angle = 0;
        else if(xlocation == GRIDWIDTH)
            angle = 180;
        else if(ylocation == 0)
            angle = 270;
        else
            angle = 90;
    }
}

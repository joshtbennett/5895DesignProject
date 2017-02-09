package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Emitter implements LevelEntity
{
    private String color;
    private int angle, xlocation, ylocation;
    private boolean received;

    Emitter(int xlocation, int ylocation, String Color)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.color = Color;

        received = false;

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

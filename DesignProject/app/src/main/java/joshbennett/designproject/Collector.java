package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class Collector implements LevelEntity, ColorableEntity{

    private boolean red, green, blue;
    private int xlocation, ylocation;
    private boolean collected;

    Collector(int xlocation, int ylocation, boolean red, boolean blue, boolean green)
    {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
        this.red = red;
        this.green = green;
        this.blue = blue;
        collected = false;
    }
}


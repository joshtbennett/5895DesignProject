package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Emitter implements LevelEntity, ColorableEntity
{
    private String color;
    private int position;

    Emitter(String color, int position)
    {
        this.position = position;
        this.color = color;
    }

    /*
    * Returns the color of the emitter
    * */
    @Override
    public String getColor() {
        return color;
    }

    /*
    * Returns the position of the emitter
    * */
    @Override
    public int getPosition() {
        return position;
    }
}

package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Mirror implements LevelEntity, ColorableEntity
{
    private int angle;
    private String color;
    private int position;

    Mirror(String color, int position)
    {
        this.position = position;
        this.color = color;
        angle = 45;
    }

    /*
    * Returns the color of the mirror
    * */
    @Override
    public String getColor() { return color; }

    /*
    * Returns the position of the mirror
    * */
    @Override
    public int getPosition() {  return position;    }

    /*
    * Returns the angle of the mirror
    * */
    public int getAngle() { return angle; }

    /*
    * Sets the angle of the mirror
    * */
    public void setAngle(int angle) {
        this.angle = angle;
    }
}

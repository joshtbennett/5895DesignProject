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

    @Override
    public String getColor() { return color; }

    @Override
    public int getPosition() {
        return position;
    }

    public int getAngle() { return angle; }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}

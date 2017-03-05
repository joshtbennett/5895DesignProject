package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Mirror implements LevelEntity, ColorableEntity
{
    private int angle;
    private char identifier;
    private String color;

    Mirror(String color)
    {
        identifier = 'm';
        this.color = color;
        angle = 45;
    }

    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getColor() { return color; }

    public int getAngle() { return angle; }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}

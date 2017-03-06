package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Emitter implements LevelEntity, ColorableEntity
{
    private char identifier;
    private String color;
    private int position;

    Emitter(String color, int position)
    {
        this.position = position;
        identifier = 'e';
        this.color = color;
    }

    void emit(){}

    @Override
    public char getIdentifier() {
        return identifier;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setReceived() {

    }

    @Override
    public boolean getReceived() {
        return false;
    }

}

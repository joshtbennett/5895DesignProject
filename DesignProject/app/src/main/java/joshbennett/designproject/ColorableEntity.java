package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public interface ColorableEntity {
    public char getIdentifier();
    public String getColor();
    public int getPosition();
    public void setReceived();
    public boolean getReceived();
}

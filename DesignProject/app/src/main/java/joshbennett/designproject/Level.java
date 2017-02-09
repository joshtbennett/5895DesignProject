package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    private int numberofemitters, numberofreceivers, numberofwalls, numberofmirrors, width, height;
    private Mirror [][] mirrorgrid;
    private Wall [][] wallgrid;

    Level()
    {
        numberofemitters = 0;
        numberofreceivers = 0;
        numberofwalls = 0;

        //size of grid
        width = ;
        height = ;

        mirrorgrid = new Mirror[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                mirrorgrid[i][j] = null;

        wallgrid = new Wall[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                wallgrid[i][j] = null;
    }

    /**
     *places a wall in the level as an obstacle
     *
     * @param xlocation = x position where a wall will be placed
     * @param ylocation = y position where a wall will be placed
     */
    public void placeWall(int xlocation, int ylocation)
    {
        if(wallgrid[xlocation][ylocation] == null)
        {
            Wall wall = new Wall(xlocation, ylocation);
            wallgrid[xlocation][ylocation] = wall;
            numberofwalls++;
        }
        else
            ERROR MESSAGE: A WALL IS ALREADY HERE
    }

    /**
     * Place a mirror on the board
     *
     * @param xlocation = the x position where the mirror will be placed
     * @param ylocation = the y position where the mirror will be placed
     * @param color = the color of light that the mirror reflects
     */
    public void placeMirror(int xlocation, int ylocation, String color)
    {
        if(mirrorgrid[xlocation][ylocation] == null)
        {
            Mirror mirror = new Mirror(xlocation, ylocation, color);
            mirrorgrid[xlocation][ylocation] = mirror;
            numberofmirrors++;
        }
        else
            ERROR MESSAGE: A MIRROR IS ALREADY HERE
    }

    /**
     * Deletes a mirror from the board
     *
     * @param xlocation = the x position of the mirror to be deleted
     * @param ylocation = the y position of the mirror to be deleted
     */
    public void deleteMirror(int xlocation, int ylocation)
    {
        if(mirrorgrid[xlocation][ylocation] != null)
        {
            mirrorgrid[xlocation][ylocation] = null;
            numberofmirrors--;
        }
        else
            ERROR MESSAGE: THERE ISNT A MIRROR IN THIS LOCATION

    }

    /**
     *Place an emitter on the board
     *
     * @param xlocation = x position where the emitter will be placed
     * @param ylocation = y position where the emitter will be placed
     * @param color = color of light the emitter emits
     */
    public void placeEmitter(int xlocation, int ylocation, String color)
    {
        Emitter emitter = new Emitter(xlocation, ylocation, color);
        numberofemitters++;
    }

    /**
     * Place a receiver on the board
     *
     * @param xlocation = x position where the receiver will be placed
     * @param ylocation = y position where the receiver will be placed
     * @param color = color of light the receiver must receiver
     */
    public void placeReceiver(int xlocation, int ylocation, String color)
    {
        Receiver receiver = new Receiver(xlocation, ylocation, color);
        numberofreceivers++;
    }

}

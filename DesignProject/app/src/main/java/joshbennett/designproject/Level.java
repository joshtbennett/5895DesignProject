package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/1/2017.
 */

public class Level
{
    private LevelEntity[] emitters, collectors, mirrors, walls;
    private int boardHeight, boardWidth;

    boolean checkWin(){return false;}

    int calculateScore(){return 1;}

    void start(){}
}

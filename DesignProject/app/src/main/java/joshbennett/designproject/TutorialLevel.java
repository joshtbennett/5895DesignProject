package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class TutorialLevel extends Level {

    private ArrayList<TextBox> textBoxes;

    public TutorialLevel(ArrayList<LevelEntity> entities, int sideLength, int par, ArrayList<TextBox> textBoxes) {
        super(entities, sideLength, par);
        this.textBoxes = textBoxes;
    }
}

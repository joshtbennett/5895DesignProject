package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class TutorialLevel extends Level {

    private TextBox textBox;

    public TutorialLevel(ArrayList<LevelEntity> entities, int sideLength, int par, TextBox textBox) {
        super(entities, sideLength, par);
        this.textBox = textBox;
    }

    public TextBox getTextbox(){   return textBox;  }
}

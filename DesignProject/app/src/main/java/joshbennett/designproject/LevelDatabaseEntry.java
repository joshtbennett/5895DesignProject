package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public final class LevelDatabaseEntry {

    public String TABLE_NAME;
    public String COLUMN_ENTITY_TYPE = "type";
    public String COLUMN_ENTITY_X = "x";
    public String COLUMN_ENTITY_Y = "y";
    public String COLUMN_ENTITY_COLOR = "color";
    public String COLUMN_ENTITY_PAR = "par";
    public String COLUMN_ENTITY_MESSAGE = "message";

    public LevelDatabaseEntry(int levelnum, boolean isTutorial) {
        TABLE_NAME = (isTutorial ? "tutlevel" : "level") + levelnum;
    }

}

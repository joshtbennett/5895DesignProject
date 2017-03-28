package joshbennett.designproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    private ArrayList<LevelEntity> entities = new ArrayList<>();
    private TextBox textBox;
    private int sideLength;
    private int levelNum;
    private int par;
    private boolean tableHasNextLevel;
    private boolean isTutorial;

    /*
    * Takes info from the database to be used to create the level for the user
    * */
    public LevelFactory(int levelNum, boolean isTutorial, Context context) {

        this.levelNum = levelNum;
        this.isTutorial = isTutorial;
        LevelDatabaseHelper mDbHelper = new LevelDatabaseHelper(context, levelNum, isTutorial);
        SQLiteDatabase db = null;
        LevelDatabaseEntry entry = null;
        Cursor cursor = null;
        try {
            db = mDbHelper.getReadableDatabase();
            entry = new LevelDatabaseEntry(levelNum, isTutorial);

            cursor = db.rawQuery("select * from " + entry.TABLE_NAME + " where " + entry.COLUMN_ENTITY_TYPE + "='data'", null);
            if (cursor.moveToFirst()) {
                sideLength = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
                par = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_PAR));
            }

            cursor = db.rawQuery("select * from " + entry.TABLE_NAME,null);

            if (mDbHelper.isTableExists(db, (isTutorial ? "tutlevel" : "level") + (levelNum+1))) {
                tableHasNextLevel = true;
            }
            else {
                tableHasNextLevel = false;
            }
        }
        catch (Exception e) {
            e.getCause();
        }

        try {
            while (cursor.moveToNext()) {

                String color = cursor.getString(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_COLOR));
                int x = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
                int y = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_Y));

                switch (cursor.getString(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_TYPE)).toLowerCase()) {

                    case "emitter":
                        Emitter emitter = new Emitter(color.toLowerCase(), sideLength * y + x);
                        entities.add(emitter);
                        break;
                    case "collector":
                        Collector collector = new Collector(color.toLowerCase(), sideLength * y + x);
                        entities.add(collector);
                        break;
                    case "wall":
                        Wall wall = new Wall(sideLength * y + x);
                        entities.add(wall);
                        break;
                    case "message":
                        textBox = new TextBox(cursor.getString((cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_MESSAGE))));
                        break;
                    default:
                }

            }
        }
        catch (Exception e) {
            e.getCause();
        }
        cursor.close();
    }

    /*
    * instantiates a new level from the data from the database
    * */
    public Level generateLevel(){
        Level level;
        if (isTutorial) {
            level = new TutorialLevel(entities, sideLength, par, textBox);
        }
        else {
            level = new Level(entities, sideLength, par);
        }
        level.levelNum = levelNum;
        level.nextLevelExists = tableHasNextLevel;
        return level;
    }

}

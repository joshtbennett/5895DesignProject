package joshbennett.designproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    ArrayList<LevelEntity> entities = new ArrayList<>();
    ArrayList<Wall> walls = new ArrayList<>();
    TextBox textBox;
    int sideLength;
    int levelNum;
    int par;
    boolean tableHasNextLevel;
    boolean isTutorial;

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

            String[] projection = {
                    entry.COLUMN_ENTITY_TYPE,
                    entry.COLUMN_ENTITY_X,
                    entry.COLUMN_ENTITY_Y,
                    entry.COLUMN_ENTITY_COLOR,
                    entry.COLUMN_ENTITY_PAR
            };

            String selection = entry.COLUMN_ENTITY_TYPE + " = ?";
            String[] selectionArgs = { "*" };

            String sortOrder =
                    entry.COLUMN_ENTITY_TYPE + " DESC";
            
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
                    /*case "data":
                        sideLength = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
                        par = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_PAR));
                        break; */
                    default:
                }

            }
        }
        catch (Exception e) {
            e.getCause();
        }
        cursor.close();

        // DATABASE WRITING CODE, KEEP FOR LATER
        //if (db.rawQuery("SELECT 1 FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", entry.TABLE_NAME}) == 0)

        /* mDbHelper.createTableIfNotExist(db, entry.TABLE_NAME);



        ContentValues values = new ContentValues();
        values.put(entry.COLUMN_ENTITY_X, 6);
        values.put(entry.COLUMN_ENTITY_Y, 6);
        values.put(entry.COLUMN_ENTITY_COLOR, "blue");
        values.put(entry.COLUMN_ENTITY_ANGLE, 0);
        values.put(entry.COLUMN_ENTITY_TYPE, "mirror"); */


        /* LevelDatabaseEntry entry = new LevelDatabaseEntry(levelNum);
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + entry.TABLE_NAME + " (" +
                        entry.COLUMN_ENTITY_ANGLE + " INTEGER," +
                        entry.COLUMN_ENTITY_COLOR + " TEXT," +
                        entry.COLUMN_ENTITY_TYPE + " TEXT," +
                        entry.COLUMN_ENTITY_X + " INTEGER," +
                        entry.COLUMN_ENTITY_Y + " INTEGER," +
                        entry.COLUMN_LEVEL_SIZE + " INTEGER)";

        db.execSQL(SQL_CREATE_ENTRIES); */
    }

    //instantiates a Level and passes in the entity array
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

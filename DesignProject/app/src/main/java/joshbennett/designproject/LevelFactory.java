package joshbennett.designproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelFactory {

    ArrayList<ColorableEntity> entities = new ArrayList<>();
    ArrayList<Wall> walls = new ArrayList<>();
    int sideLength;

    public LevelFactory(int levelNum, boolean isTutorial, Context context) {

        LevelDatabaseHelper mDbHelper = new LevelDatabaseHelper(context, levelNum);
        SQLiteDatabase db = null;
        LevelDatabaseEntry entry = null;
        Cursor cursor = null;
        try {
            db = mDbHelper.getReadableDatabase();
            entry = new LevelDatabaseEntry(levelNum);


            String[] projection = {
                    entry.COLUMN_ENTITY_TYPE,
                    entry.COLUMN_ENTITY_X,
                    entry.COLUMN_ENTITY_Y,
                    entry.COLUMN_ENTITY_COLOR,
                    entry.COLUMN_ENTITY_ANGLE
            };

            String selection = entry.COLUMN_ENTITY_TYPE + " = ?";
            String[] selectionArgs = { "*" };

            String sortOrder =
                    entry.COLUMN_ENTITY_TYPE + " DESC";

           /* cursor = db.query(
                    entry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            ); */
           cursor = db.rawQuery("select * from " + entry.TABLE_NAME + " where " + entry.COLUMN_ENTITY_TYPE + "=size", null);
            if (cursor.moveToFirst()) {
                sideLength = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
            }

            cursor = db.rawQuery("select * from " + entry.TABLE_NAME,null);
        }
        catch (Exception e)
        {

        }
        Emitter emitter1 = new Emitter("cyan", 24);
        entities.add(emitter1);
        Emitter emitter2 = new Emitter("blue", 40);
        entities.add(emitter2);
        Emitter emitter3 = new Emitter("red", 3);
        entities.add(emitter3);
        Collector collector1 = new Collector("magenta", 59);
        entities.add(collector1);
        Collector collector2 = new Collector("green", 23);
        entities.add(collector2);
        Collector collector3 = new Collector("blue", 39);
        entities.add(collector3);
        Wall wall = new Wall(28);
        walls.add(wall);



        /*
        try {
            while (cursor.moveToNext()) {

                String color = cursor.getString(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_COLOR));
                int x = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
                int y = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_Y));

                switch (cursor.getString(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_TYPE))) {

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
                        walls.add(wall);
                        break;
                    case "size":
                        sideLength = cursor.getInt(cursor.getColumnIndexOrThrow(entry.COLUMN_ENTITY_X));
                        break;
                }

            }
        }
        catch (Exception e) {

        }
        cursor.close();
*/
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
        sideLength = 8;
        Level level = new Level(entities, walls, sideLength);
        return level;
    }

}

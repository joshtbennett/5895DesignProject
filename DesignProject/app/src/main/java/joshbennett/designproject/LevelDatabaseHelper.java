package joshbennett.designproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class LevelDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LevelDatabase.db";

    private String SQL_CREATE_ENTRIES;
    private String SQL_DELETE_ENTRIES;

    public LevelDatabaseHelper(Context context, int levelNum) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LevelDatabaseEntry entry = new LevelDatabaseEntry(levelNum);
        SQL_CREATE_ENTRIES =
                "CREATE TABLE " + entry.TABLE_NAME + " (" +
                        entry.COLUMN_ENTITY_ANGLE + " INTEGER," +
                        entry.COLUMN_ENTITY_COLOR + " TEXT," +
                        entry.COLUMN_ENTITY_TYPE + " TEXT," +
                        entry.COLUMN_ENTITY_X + " INTEGER," +
                        entry.COLUMN_ENTITY_Y + " INTEGER)";
        SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + entry.TABLE_NAME;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void createTableIfNotExist(SQLiteDatabase db, String tableName) {
        if (!isTableExists(db, tableName)) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}

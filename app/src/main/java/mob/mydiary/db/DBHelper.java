package mob.mydiary.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import mob.mydiary.DB.DBStructure.*;


public class DBHelper extends SQLiteOpenHelper {

    /**
     * Version 7 by Daxia
     * Add new table for topic order
     * -------------
     * Version 6 by Daxia
     * Add new table for memo order
     * ---------------------------------
     * Version 5 by Daxia
     * Add color message in topic
     * (Topic bg name is fixed in its dir)
     * --------------
     * Version 4 by Daxia:
     * design db DiaryEntry  -> DiaryEntry_v2
     * --------------
     * Version 3 by Daxia:
     * Add local contacts table
     * Add memo subtitle row.
     * --------------
     * Version 2 by Daxia:
     * Add location row.
     * Add memo table.
     * Add topic order.
     * --------------
     * Version 1 by Daxia：
     * First DB
     */
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "mydiary.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    /**
     * Discarded DIARY DB
     */
    private static final String SQL_CREATE_DIARY_ENTRIES =
            "CREATE TABLE " + DiaryEntry.TABLE_NAME + " (" +
                    DiaryEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    DiaryEntry.COLUMN_TIME + INTEGER_TYPE + COMMA_SEP +
                    DiaryEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    DiaryEntry.COLUMN_MOOD + INTEGER_TYPE + COMMA_SEP +
                    DiaryEntry.COLUMN_WEATHER + INTEGER_TYPE + COMMA_SEP + " )";



    private static final String SQL_CREATE_DIARY_ITEM_ENTRIES =
            "CREATE TABLE " + DiaryItemEntry.TABLE_NAME + " (" +
                    DiaryItemEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    DiaryItemEntry.COLUMN_POSITION + INTEGER_TYPE + COMMA_SEP +
                    DiaryItemEntry.COLUMN_CONTENT + TEXT_TYPE + COMMA_SEP +
                    DiaryItemEntry.COLUMN_REF_DIARY__ID + INTEGER_TYPE + COMMA_SEP + " )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DIARY_ENTRIES);
        db.execSQL(SQL_CREATE_DIARY_ITEM_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}

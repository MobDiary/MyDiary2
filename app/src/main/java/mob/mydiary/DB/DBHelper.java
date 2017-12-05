package mob.mydiary.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mob.mydiary.DB.DBStructure.*;

/**
 * Created by JooYoung on 2017-12-05.
 */


public class DBHelper extends SQLiteOpenHelper {


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
                    DiaryEntry.COLUMN_CONTENT + TEXT_TYPE + COMMA_SEP +
                    DiaryEntry.COLUMN_MOOD + INTEGER_TYPE + COMMA_SEP +
                    DiaryEntry.COLUMN_WEATHER + INTEGER_TYPE + COMMA_SEP + " )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_DIARY_ENTRIES);

        //Add memo order table in version 6
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                db.beginTransaction();
                if (oldVersion < 4) {
                    //Create  diary V2 db
                    db.execSQL(SQL_CREATE_DIARY_ENTRIES);
                    //Delete  diary v1 db
                    String deleteV1DiaryTable = "DROP TABLE IF EXISTS " + DiaryEntry.TABLE_NAME;
                    db.execSQL(deleteV1DiaryTable);
                }
                //Check update success
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } else {
            onCreate(db);
        }
    }

}
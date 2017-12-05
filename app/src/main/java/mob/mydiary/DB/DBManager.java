package mob.mydiary.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import mob.mydiary.DB.DBHelper;
import mob.mydiary.DB.DBStructure.*;
public class DBManager {

    //TODO add SQLiteException
    private Context context;
    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager(SQLiteDatabase db) {
        this.db = db;
    }

    /*
     * DB IO
     */

    public void openDB() throws SQLiteException {
        mDBHelper = new DBHelper(context);
        // Gets the data repository in write mode
        this.db = mDBHelper.getWritableDatabase();
    }


    public void closeDB() {
        mDBHelper.close();
    }


    public void beginTransaction() {
        db.beginTransaction();
    }

    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        db.endTransaction();
    }

    /*
     * Diary
     */
    public long insertDiaryInfo(long time, String title, String content,
                                int mood, int weather) {
        return db.insert(
                DiaryEntry.TABLE_NAME,
                null,
                this.createDiaryInfoCV(time, title, content, mood, weather));
    }


    public long updateDiary(long diaryId, long time, String title, String content,
                            int mood, int weather) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry.COLUMN_TIME, time);
        values.put(DiaryEntry.COLUMN_TITLE, title);
        values.put(DiaryEntry.COLUMN_CONTENT,content);
        values.put(DiaryEntry.COLUMN_MOOD, mood);
        values.put(DiaryEntry.COLUMN_WEATHER, weather);

        return db.update(
                DiaryEntry.TABLE_NAME,
                values,
                DiaryEntry._ID + " = ?",
                new String[]{String.valueOf(diaryId)});
    }

    public long delDiary(long diaryId) {
        return db.delete(
                DiaryEntry.TABLE_NAME,
                DiaryEntry._ID + " = ?"
                , new String[]{String.valueOf(diaryId)});
    }

    private ContentValues createDiaryInfoCV(long time, String title, String content,
                                            int mood, int weather) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry.COLUMN_TIME, time);
        values.put(DiaryEntry.COLUMN_TITLE, title);
        values.put(DiaryEntry.COLUMN_CONTENT,content);
        values.put(DiaryEntry.COLUMN_MOOD, mood);
        values.put(DiaryEntry.COLUMN_WEATHER, weather);
        return values;
    }

}

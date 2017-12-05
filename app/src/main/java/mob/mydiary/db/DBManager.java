package mob.mydiary.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
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

    public void opeDB() throws SQLiteException {
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
    public long insertDiaryInfo(long time, String title,
                                int mood, int weather, boolean attachment,
                                long refTopicId, String locationName) {
        return db.insert(
                DiaryEntry.TABLE_NAME,
                null,
                this.createDiaryInfoCV(time, title,
                        mood, weather, attachment, refTopicId, locationName));
    }

    public long insertDiaryContent(int type, int position, String content, long diaryId) {
        return db.insert(
                DiaryItemEntry.TABLE_NAME,
                null,
                this.createDiaryContentCV(type, position, content, diaryId));
    }

    public long updateDiary(long diaryId, long time, String title,
                            int mood, int weather) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry.COLUMN_TIME, time);
        values.put(DiaryEntry.COLUMN_TITLE, title);
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


    private ContentValues createDiaryInfoCV(long time, String title,
                                            int mood, int weather, boolean attachment, long refTopicId,
                                            String locationName) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry.COLUMN_TIME, time);
        values.put(DiaryEntry.COLUMN_TITLE, title);
        values.put(DiaryEntry.COLUMN_MOOD, mood);
        values.put(DiaryEntry.COLUMN_WEATHER, weather);
        return values;
    }

    private ContentValues createDiaryContentCV(int type, int position, String content, long diaryId) {
        ContentValues values = new ContentValues();
        values.put(DiaryItemEntry.COLUMN_POSITION, position);
        values.put(DiaryItemEntry.COLUMN_CONTENT, content);
        values.put(DiaryItemEntry.COLUMN_REF_DIARY__ID, diaryId);
        return values;
    }

}

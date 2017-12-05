package mob.mydiary.DB;

import android.provider.BaseColumns;

public class DBStructure {


    public static abstract class DiaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary_entry";
        public static final String COLUMN_TIME = "diary_time";
        public static final String COLUMN_TITLE = "diary_title";
        public static final String COLUMN_MOOD = "diary_mood";
        public static final String COLUMN_WEATHER = "diary_weather";
    }
    /**
     * Type see @IDairyRow
     */
    public static abstract class DiaryItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary_item_entry";
        public static final String COLUMN_POSITION = "diary_item_position";
        public static final String COLUMN_CONTENT = "diary_item_content";
        public static final String COLUMN_REF_DIARY__ID = "item_ref_diary_id";
    }


}

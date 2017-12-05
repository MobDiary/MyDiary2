package mob.mydiary.DB;

import android.provider.BaseColumns;

/**
 * Created by juyeong on 2017-12-06.
 */

public class DBStructure {
    public static abstract class DiaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "diary_entry";
        public static final String COLUMN_TIME = "diary_time";
        public static final String COLUMN_TITLE = "diary_title";
        public static final String COLUMN_CONTENT = "diary_content";
        public static final String COLUMN_MOOD = "diary_mood";
        public static final String COLUMN_WEATHER = "diary_weather";
    }
}

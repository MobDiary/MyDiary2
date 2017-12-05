package mob.mydiary.Manager;

import android.content.Context;

import mob.mydiary.R;
// SingleTon으로서 시간을 관리한다.
public class TimeManager {

    private String[] monthsFullName = {"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
    private String[] daysFullName = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};

    private static TimeManager instance = null;

    public static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    private TimeManager() {
    }

    public String[] getMonthsFullName() {
        return monthsFullName;
    }

    public String[] getDaysFullName() {
        return daysFullName;
    }
}

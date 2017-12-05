package mob.mydiary.Diary.picker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import mob.mydiary.R;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {


    private long savedTime;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;


    public static TimePickerFragment newInstance(long savedTime) {
        Bundle args = new Bundle();
        TimePickerFragment fragment = new TimePickerFragment();
        args.putLong("savedTime", savedTime);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        savedTime = getArguments().getLong("savedTime", -1);
        Calendar calendar = Calendar.getInstance();
        if (savedTime != -1) {
            calendar.setTimeInMillis(savedTime);
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //Note:
        //The bug was triggered only on Chinese.
        return new TimePickerDialog(getActivity(), R.style.CustomPickerDialogTheme,
                onTimeSetListener, hour, minute, true);
    }
}
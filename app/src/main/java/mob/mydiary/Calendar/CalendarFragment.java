package mob.mydiary.Calendar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import mob.mydiary.BaseFragment;
import mob.mydiary.R;

public class CalendarFragment extends BaseFragment implements View.OnClickListener, OnDateSelectedListener, DayViewDecorator
{
    private Calendar calendar;

    private Date currentDate;
    private MaterialCalendarView materialCalendarView;
    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        currentDate = new Date();
        calendar.setTime(currentDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);


        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void decorate(DayViewFacade view) {

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return false;
    }
}

package mob.mydiary.Diary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mob.mydiary.DB.DBHelper;
import mob.mydiary.DB.DBManager;
import mob.mydiary.BaseFragment;
import mob.mydiary.Diary.picker.DatePickerFragment;
import mob.mydiary.Diary.picker.TimePickerFragment;
import mob.mydiary.Entries.EntriesFragment;
import mob.mydiary.MainActivity;
import mob.mydiary.Manager.TimeManager;
import mob.mydiary.R;

public class DiaryFragment extends BaseFragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Intent intent;
    private DBHelper mDBHelper;
    private DBManager mDBManager;

    // UI 툴
    private ScrollView ScrollView_diary_content;
    private LinearLayout LL_diary_content, LL_diary_time_information;
    private RelativeLayout RL_diary_info;
    private TextView TV_diary_month, TV_diary_date, TV_diary_day, TV_diary_time;


    private Spinner SP_diary_weather, SP_diary_mood;
    private EditText EDT_diary_title, EDT_diary_content;
    private ImageView IV_diary_photo, IV_diary_delete, IV_diary_save;

    /*
    * 시간 및 날짜
    * */
    private Calendar calendar;
    private TimeManager timeManager;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


    public DiaryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
        mDBManager = new DBManager(getActivity());
        timeManager = TimeManager.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        ScrollView_diary_content = (ScrollView) rootView.findViewById(R.id.ScrollView_diary_content);



        RL_diary_info = (RelativeLayout) rootView.findViewById(R.id.RL_diary_info);

        LinearLayout LL_diary_edit_bar = (LinearLayout) rootView.findViewById(R.id.LL_diary_edit_bar);

        LL_diary_time_information = (LinearLayout) rootView.findViewById(R.id.LL_diary_time_information);
        LL_diary_time_information.setOnClickListener(this);

        TV_diary_month = (TextView) rootView.findViewById(R.id.TV_diary_month);
        TV_diary_date = (TextView) rootView.findViewById(R.id.TV_diary_date);
        TV_diary_day = (TextView) rootView.findViewById(R.id.TV_diary_day);
        TV_diary_time = (TextView) rootView.findViewById(R.id.TV_diary_time);

        SP_diary_weather = (Spinner) rootView.findViewById(R.id.SP_diary_weather);
        SP_diary_weather.setVisibility(View.VISIBLE);
        SP_diary_mood = (Spinner) rootView.findViewById(R.id.SP_diary_mood);
        SP_diary_mood.setVisibility(View.VISIBLE);

        EDT_diary_title = (EditText) rootView.findViewById(R.id.EDT_diary_title);
        EDT_diary_title.setOnClickListener(this);

        EDT_diary_content = (EditText) rootView.findViewById(R.id.EDT_diary_content);
        EDT_diary_content.setOnClickListener(this);

        IV_diary_delete = (ImageView) rootView.findViewById(R.id.IV_diary_delete);
        IV_diary_delete.setVisibility(View.GONE);

        IV_diary_save = (ImageView) rootView.findViewById(R.id.IV_diary_save);
        IV_diary_save.setOnClickListener(this);


        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpinner();
        setCurrentTime(true);
        initDiaryPage();
    }


    private void initDiaryPage() {

        SP_diary_mood.setSelection(0);
        SP_diary_weather.setSelection(0);
        EDT_diary_title.setText("");
    }

    private void initSpinner() {
        ImageArrayAdapter weatherArrayAdapter = new ImageArrayAdapter(getActivity(), DiaryInfoHelper.getWeatherArray());
        SP_diary_weather.setAdapter(weatherArrayAdapter);

        ImageArrayAdapter moodArrayAdapter = new ImageArrayAdapter(getActivity(), DiaryInfoHelper.getMoodArray());
        SP_diary_mood.setAdapter(moodArrayAdapter);
    }
    private void setIcon(int mood, int weather) {
        SP_diary_mood.setSelection(mood);
        SP_diary_weather.setSelection(weather);
    }

    private void setCurrentTime(boolean updateCurrentTime) {
        if (updateCurrentTime) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        TV_diary_month.setText(timeManager.getMonthsFullName()[calendar.get(Calendar.MONTH)]);
        TV_diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        TV_diary_day.setText(timeManager.getDaysFullName()[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        TV_diary_time.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_diary_time_information:
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(calendar.getTimeInMillis());
                datePickerFragment.setOnDateSetListener(this);
                datePickerFragment.show(getFragmentManager(), "datePickerFragment");
                break;
            case R.id.IV_diary_save:
                    saveDiary();
                    Toast.makeText(getActivity(), getString(R.string.toast_diary_insert_successful), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveDiary() {
        mDBManager.openDB();
        mDBManager.beginTransaction();

        mDBManager.insertDiaryInfo(calendar.getTimeInMillis(), EDT_diary_title.getText().toString(),
                EDT_diary_content.getText().toString(),
                SP_diary_mood.getSelectedItemPosition(),
                SP_diary_weather.getSelectedItemPosition());

        mDBManager.setTransactionSuccessful();
        mDBManager.endTransaction();
        mDBManager.closeDB();

        setCurrentTime(true);
        ((MainActivity) getActivity()).callEntriesListRefresh();
        ((MainActivity) getActivity()).gotoPage(0);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //Since JellyBean, the onDateSet() method of the DatePicker class is called twice
        if (view.isShown()) {
            calendar.set(year, monthOfYear, dayOfMonth);
            setDiaryTime();
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(calendar.getTimeInMillis());
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager(), "timePickerFragment");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Since JellyBean, the onTimeSet() method of the TimePicker class is called twice
        if (view.isShown()) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setDiaryTime();
        }
    }
    private void setDiaryTime() {
        TV_diary_month.setText(timeManager.getMonthsFullName()[calendar.get(Calendar.MONTH)]);
        TV_diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        TV_diary_day.setText(timeManager.getDaysFullName()[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        TV_diary_time.setText(sdf.format(calendar.getTime()));
    }
}

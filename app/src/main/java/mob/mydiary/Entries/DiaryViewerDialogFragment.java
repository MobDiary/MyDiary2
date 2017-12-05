package mob.mydiary.Entries;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import mob.mydiary.Manager.PhoneHelper;
import mob.mydiary.R;
import mob.mydiary.DB.DBManager;
import mob.mydiary.MainActivity;
import mob.mydiary.Diary.DiaryInfoHelper;
import mob.mydiary.Diary.ImageArrayAdapter;
import mob.mydiary.Diary.picker.DatePickerFragment;
import mob.mydiary.Diary.picker.TimePickerFragment;
import mob.mydiary.Manager.FileManager;
import mob.mydiary.Manager.ScreenHelper;
import mob.mydiary.Manager.ThemeManager;
import mob.mydiary.Manager.TimeManager;
import mob.mydiary.Manager.ViewTools;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class DiaryViewerDialogFragment extends DialogFragment implements View.OnClickListener,
        DiaryDeleteDialogFragment.DeleteCallback, CopyDiaryToEditCacheTask.EditTaskCallBack,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * Callback
     */
    public interface DiaryViewerCallback {
        void deleteDiary();

        void updateDiary();
    }

    private DiaryViewerCallback callback;

    private static final String TAG = "DiaryViewer";

    /**
     * UI
     */
    private ScrollView ScrollView_diary_content;
    private RelativeLayout RL_diary_info;
    private ProgressBar PB_diary_item_content_hint;
    private LinearLayout LL_diary_time_information;

    private TextView TV_diary_month, TV_diary_date, TV_diary_day, TV_diary_time;

    private ImageView IV_diary_weather;
    private ImageView IV_diary_location_name_icon;
    private TextView TV_diary_weather, TV_diary_location;
    private RelativeLayout RL_diary_weather, RL_diary_mood;
    private Spinner SP_diary_weather, SP_diary_mood;

    private TextView TV_diary_title_content;
    private EditText EDT_diary_title, EDT_diary_content;

    private ImageView IV_diary_close_dialog,
            IV_diary_delete, IV_diary_clear, IV_diary_save;
    private boolean isEditMode;

    /**
     * diary content & info
     */
    private long diaryId;
    private FileManager diaryFileManager;

    /**
     * Edit Mode
     */
    private CopyDiaryToEditCacheTask mTask;
    private Handler loadDiaryHandler;
    private boolean initHandlerOrTaskIsRunning = false;

    private Calendar calendar;
    private TimeManager timeTools;
    private SimpleDateFormat sdf;

    /**
     * Diary Photo viewer
     */
    private ArrayList<Uri> diaryPhotoFileList;

    /**
     * Google Place API
     */
    private int PLACE_PICKER_REQUEST = 1;
    /**
     * Location
     */
    private DiaryViewerHandler diaryViewerHandler;
    private boolean haveLocation;
    private String noLocation;
    private ProgressDialog progressDialog;


    /**
     * Permission
     */
    private boolean firstAllowLocationPermission = false;
    private boolean firstAllowCameraPermission = false;


    public static DiaryViewerDialogFragment newInstance(long diaryId, boolean isEditMode) {
        Bundle args = new Bundle();
        DiaryViewerDialogFragment fragment = new DiaryViewerDialogFragment();
        args.putLong("diaryId", diaryId);
        args.putBoolean("isEditMode", isEditMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEditMode = getArguments().getBoolean("isEditMode", false);
        if (isEditMode) {
            Toast.makeText(getActivity(),
                    getString(R.string.toast_diary_long_click_edit), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {

                    super.onBackPressed();

            }
        };
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //Set background is transparent , for dialog radius
        dialog.getWindow().getDecorView().getBackground().setAlpha(0);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getDialog().setCanceledOnTouchOutside(false);
        View rootView = inflater.inflate(R.layout.fragment_diary, container);
        ScrollView_diary_content = (ScrollView) rootView.findViewById(R.id.ScrollView_diary_content);
        ViewTools.setScrollBarColor(getActivity(), ScrollView_diary_content);

        RL_diary_info = (RelativeLayout) rootView.findViewById(R.id.RL_diary_info);
        RL_diary_info.setBackground(ThemeManager.getInstance().createDiaryViewerInfoBg(getActivity()));

        LinearLayout LL_diary_edit_bar = (LinearLayout) rootView.findViewById(R.id.LL_diary_edit_bar);
        LL_diary_edit_bar.setBackground(ThemeManager.getInstance().createDiaryViewerEditBarBg(getActivity()));

        PB_diary_item_content_hint = (ProgressBar) rootView.findViewById(R.id.PB_diary_item_content_hint);

        EDT_diary_title = (EditText) rootView.findViewById(R.id.EDT_diary_title);
        EDT_diary_content = (EditText) rootView.findViewById(R.id.EDT_diary_content);

        TV_diary_month = (TextView) rootView.findViewById(R.id.TV_diary_month);
        TV_diary_date = (TextView) rootView.findViewById(R.id.TV_diary_date);
        TV_diary_day = (TextView) rootView.findViewById(R.id.TV_diary_day);
        TV_diary_time = (TextView) rootView.findViewById(R.id.TV_diary_time);


        IV_diary_close_dialog = (ImageView) rootView.findViewById(R.id.IV_diary_close_dialog);
        IV_diary_close_dialog.setVisibility(View.VISIBLE);
        IV_diary_close_dialog.setOnClickListener(this);


        IV_diary_delete = (ImageView) rootView.findViewById(R.id.IV_diary_delete);

        IV_diary_save = (ImageView) rootView.findViewById(R.id.IV_diary_save);

        initView(rootView);
        noLocation = getString(R.string.diary_no_location);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback = (DiaryViewerCallback) getTargetFragment();
        diaryId = getArguments().getLong("diaryId", -1L);
        //Init the object
        if (diaryId != -1) {
            if (isEditMode) {
                diaryViewerHandler = new DiaryViewerHandler(this);
                diaryFileManager = new FileManager(getActivity(), FileManager.DIARY_EDIT_CACHE_DIR);
                diaryFileManager.clearDir();
                PB_diary_item_content_hint.setVisibility(View.VISIBLE);
                mTask = new CopyDiaryToEditCacheTask(getActivity(), diaryFileManager, this);
                //Make ths ProgressBar show 0.7s+.
                loadDiaryHandler = new Handler();
                initHandlerOrTaskIsRunning = true;
                loadDiaryHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Copy the file into editCash
                        mTask.execute(diaryId);
                    }
                }, 700);
            } else {
                diaryFileManager = new FileManager(getActivity());
                diaryPhotoFileList = new ArrayList<>();
                initData();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Modify dialog size
        Dialog dialog = getDialog();
        if (dialog != null) {
            int dialogHeight;
            if (PhoneHelper.getDeviceStatusBarType() == PhoneHelper.OTHER) {
                dialogHeight = ScreenHelper.getScreenHeight(getActivity())
                        - ScreenHelper.getStatusBarHeight(getActivity())
                        - ScreenHelper.dpToPixel(getActivity().getResources(), 2 * 10);
            } else {
                dialogHeight = ScreenHelper.getScreenHeight(getActivity())
                        - ScreenHelper.dpToPixel(getActivity().getResources(), 2 * 10);
            }
            int dialogWidth = ScreenHelper.getScreenWidth(getActivity())
                    - ScreenHelper.dpToPixel(getActivity().getResources(), 2 * 5);
            dialog.getWindow().setLayout(dialogWidth, dialogHeight);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (initHandlerOrTaskIsRunning) {
            if (loadDiaryHandler != null) {
                loadDiaryHandler.removeCallbacksAndMessages(null);
            }
            if (mTask != null) {
                mTask.cancel(true);
            }
            dismissAllowingStateLoss();
        }
        if (diaryViewerHandler != null) {
            diaryViewerHandler.removeCallbacksAndMessages(null);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    private void initData() {
        DBManager dbManager = new DBManager(getActivity());
        dbManager.openDB();
        Cursor diaryInfoCursor = dbManager.selectDiaryInfoByDiaryId(diaryId);
        //load Time
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(diaryInfoCursor.getLong(1));
        timeTools = TimeManager.getInstance();
        sdf = new SimpleDateFormat("HH:mm");
        setDiaryTime();

        // 편집하는 모드라면
        if (isEditMode) {
            //Allow to edit diary
            LL_diary_time_information.setOnClickListener(this);
            EDT_diary_title.setText(diaryInfoCursor.getString(2));
        }

        // 일반 보는 모드
        else {
            String diaryTitleStr = diaryInfoCursor.getString(2);
            if (diaryTitleStr == null || diaryTitleStr.equals("")) {
                diaryTitleStr = getString(R.string.diary_no_title);
            }
            TV_diary_title_content.setText(diaryTitleStr);
        }
        setIcon(diaryInfoCursor.getInt(4), diaryInfoCursor.getInt(5));
        diaryInfoCursor.close();
        //Get diary detail
        dbManager.closeDB();
    }

    private void initView(View rootView) {

        // 수정모드
        if (isEditMode) {
            initProgressDialog();

            LL_diary_time_information = (LinearLayout) rootView.findViewById(R.id.LL_diary_time_information);
            SP_diary_mood = (Spinner) rootView.findViewById(R.id.SP_diary_mood);
            SP_diary_mood.setVisibility(View.VISIBLE);
            SP_diary_weather = (Spinner) rootView.findViewById(R.id.SP_diary_weather);
            SP_diary_weather.setVisibility(View.VISIBLE);

            //For hidden hint
            EDT_diary_title.setText(" ");
            EDT_diary_title.getBackground().mutate().setColorFilter(ThemeManager.getInstance().getThemeMainColor(getActivity()),
                    PorterDuff.Mode.SRC_ATOP);

            initMoodSpinner();
            initWeatherSpinner();

            IV_diary_delete.setOnClickListener(this);
            IV_diary_clear.setVisibility(View.GONE);

        }

        // 보기 모드
        else {
            EDT_diary_title.setVisibility(View.GONE);
            RL_diary_weather = (RelativeLayout) rootView.findViewById(R.id.RL_diary_weather);
            RL_diary_weather.setVisibility(View.GONE);
            RL_diary_mood = (RelativeLayout) rootView.findViewById(R.id.RL_diary_mood);
            RL_diary_mood.setVisibility(View.GONE);

            IV_diary_weather = (ImageView) rootView.findViewById(R.id.IV_diary_weather);
            TV_diary_weather = (TextView) rootView.findViewById(R.id.TV_diary_weather);
            IV_diary_weather.setVisibility(View.VISIBLE);
            TV_diary_weather.setVisibility(View.VISIBLE);

            TV_diary_title_content = (TextView) rootView.findViewById(R.id.TV_diary_title_content);
            TV_diary_title_content.setVisibility(View.VISIBLE);
            TV_diary_title_content.setTextColor(ThemeManager.getInstance().getThemeMainColor(getActivity()));

            IV_diary_delete.setOnClickListener(this);
            IV_diary_save.setVisibility(View.GONE);

        }
    }


    private void initMoodSpinner() {
        ImageArrayAdapter moodArrayAdapter = new ImageArrayAdapter(getActivity(), DiaryInfoHelper.getMoodArray());
        SP_diary_mood.setAdapter(moodArrayAdapter);
    }

    private void initWeatherSpinner() {
        ImageArrayAdapter weatherArrayAdapter = new ImageArrayAdapter(getActivity(), DiaryInfoHelper.getWeatherArray());
        SP_diary_weather.setAdapter(weatherArrayAdapter);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.process_dialog_loading));
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
    }


    private void setDiaryTime() {
        TV_diary_month.setText(timeTools.getMonthsFullName()[calendar.get(Calendar.MONTH)]);
        TV_diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        TV_diary_day.setText(timeTools.getDaysFullName()[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        TV_diary_time.setText(sdf.format(calendar.getTime()));
    }

    private void setIcon(int mood, int weather) {
        if (isEditMode) {
            SP_diary_mood.setSelection(mood);
            SP_diary_weather.setSelection(weather);
        } else {
            IV_diary_weather.setImageResource(DiaryInfoHelper.getWeatherResourceId(weather));
            TV_diary_weather.setText(getResources().getStringArray(R.array.weather_list)[weather]);
        }
    }


    private void updateDiary() {

        DBManager dbManager = new DBManager(getActivity());
        dbManager.openDB();
        //Create locationName
        String locationName = TV_diary_location.getText().toString();
        if (noLocation.equals(locationName)) {
            locationName = "";
        }
        dbManager.updateDiary(diaryId, calendar.getTimeInMillis(), EDT_diary_title.getText().toString(),
                EDT_diary_content.getText().toString(),
                SP_diary_mood.getSelectedItemPosition(), SP_diary_weather.getSelectedItemPosition());

    }

    public void onDiaryUpdated() {
        this.dismissAllowingStateLoss();
        callback.updateDiary();
    }


    @Override
    public void onDiaryDelete() {
        callback.deleteDiary();
        dismiss();
    }

    public void onBack() {
        dismiss();
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


    @Override
    public void onCopyToEditCacheCompiled(int result) {
        if (result == CopyDiaryToEditCacheTask.RESULT_COPY_SUCCESSFUL) {
            PB_diary_item_content_hint.setVisibility(View.GONE);
            initData();
            //Open the click listener
            IV_diary_clear.setOnClickListener(this);
            IV_diary_save.setOnClickListener(this);
        } else {
            dismissAllowingStateLoss();
        }
        initHandlerOrTaskIsRunning = false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.LL_diary_time_information:
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(calendar.getTimeInMillis());
                datePickerFragment.setOnDateSetListener(this);
                datePickerFragment.show(getFragmentManager(), "datePickerFragment");
                break;
            case R.id.IV_diary_delete:
                DiaryDeleteDialogFragment diaryDeleteDialogFragment =
                        DiaryDeleteDialogFragment.newInstance(diaryId);
                diaryDeleteDialogFragment.setTargetFragment(this, 0);
                diaryDeleteDialogFragment.show(getFragmentManager(), "diaryDeleteDialogFragment");
                break;
            case R.id.IV_diary_save:
                updateDiary();
                Toast.makeText(getActivity(), getString(R.string.toast_diary_insert_successful), Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private static class DiaryViewerHandler extends Handler {

        private WeakReference<DiaryViewerDialogFragment> mFrag;

        DiaryViewerHandler(DiaryViewerDialogFragment aFragment) {
            mFrag = new WeakReference<>(aFragment);
        }
    }
}

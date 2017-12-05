package mob.mydiary;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mob.mydiary.DB.*;
import mob.mydiary.Calendar.CalendarFragment;
import mob.mydiary.Diary.DiaryFragment;
import mob.mydiary.Entries.EntriesEntity;
import mob.mydiary.Entries.EntriesFragment;
import mob.mydiary.Manager.ThemeManager;
import mob.mydiary.Manager.ScreenHelper;
import mob.mydiary.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private ViewPager ViewPager_diary_content;

    private boolean hasEntries;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private RadioButton btn_entries, btn_calendar, btn_diary;

    private List<EntriesEntity> entriesList = new ArrayList<>();
    private final static int MAX_TEXT_LENGTH = 18;


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                default:
                    btn_entries.setChecked(true);
                    break;
                case 1:
                    btn_calendar.setChecked(true);
                    break;
                case 2:
                    btn_diary.setChecked(true);
                    break;
            }
        }
    };

    private void initViewPager() {
        ViewPager_diary_content = (ViewPager) findViewById(R.id.VP_ViewPage);
        //Make viewpager load one fragment every time.
        ViewPager_diary_content.setOffscreenPageLimit(2);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        ViewPager_diary_content.setAdapter(mPagerAdapter);
        ViewPager_diary_content.addOnPageChangeListener(onPageChangeListener);
        ViewPager_diary_content.setBackground(
                ThemeManager.getInstance().getEntriesBgDrawable(this));
        if (!hasEntries) {
            ViewPager_diary_content.setCurrentItem(2);
            //Set Default Checked Item
            btn_diary.setChecked(true);
        } else {
            //Set Default Checked Item
            btn_entries.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mViewPager = (ViewPager)findViewById(R.id.VP_ViewPage);
        hasEntries = getIntent().getBooleanExtra("has_entries", true);


        btn_entries = (RadioButton)findViewById(R.id.btn_entries);
        btn_calendar = (RadioButton)findViewById(R.id.btn_calendar);
        btn_diary = (RadioButton)findViewById(R.id.btn_diary);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_entries.setOnClickListener(this);
        btn_entries.setTag(0);
        btn_calendar.setOnClickListener(this);
        btn_calendar.setTag(1);
        btn_diary.setOnClickListener(this);
        btn_diary.setTag(2);

        initViewPager();

        mViewPager.setCurrentItem(0);

    }
    @Override
    public void onClick(View v)
    {
        int tag = (int)v.getTag();
        mViewPager.setCurrentItem(tag);
    }

    public void gotoPage(int position) {
        ViewPager_diary_content.setCurrentItem(position);
    }

    public void callEntriesGotoDiaryPosition(int position) {
        EntriesFragment entriesFragment = ((EntriesFragment) mPagerAdapter.getRegisteredFragment(0));
        if (entriesFragment != null) {
            gotoPage(0);
            entriesFragment.gotoDiaryPosition(position);
        }
    }

    public List<EntriesEntity> getEntriesList() {
        return entriesList;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment;
            switch (position) {
                default:
                    fragment = new EntriesFragment();
                    break;
                case 1:
                    fragment = new CalendarFragment();
                    break;
                case 2:
                    fragment = new DiaryFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }

    /**
     초기 화면으로
     */
    public void loadEntries() {
        entriesList.clear();
        DBManager dbManager = new DBManager(this);
        dbManager.openDB();
        //Select diary info
        Cursor diaryCursor = dbManager.selectDiaryList();
        for (int i = 0; i < diaryCursor.getCount(); i++) {
            //get diary info
            String title = diaryCursor.getString(2);
            if ("".equals(title)) {
                title = getString(R.string.diary_no_title);
            }
            EntriesEntity entity = new EntriesEntity(diaryCursor.getLong(0),
                    new Date(diaryCursor.getLong(1)),
                    title.substring(0, Math.min(MAX_TEXT_LENGTH, title.length())),
                    diaryCursor.getInt(4), diaryCursor.getInt(3));

            //Add entity
            entriesList.add(entity);
            diaryCursor.moveToNext();
        }
        diaryCursor.close();
        dbManager.closeDB();
    }
}

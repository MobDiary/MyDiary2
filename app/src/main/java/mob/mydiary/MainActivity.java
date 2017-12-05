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
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mob.mydiary.DB.*;
import mob.mydiary.Calendar.CalendarFragment;
import mob.mydiary.Diary.DiaryFragment;
import mob.mydiary.Entries.EntriesEntity;
import mob.mydiary.Entries.EntriesFragment;
import mob.mydiary.Diary.item.IDairyRow;
import mob.mydiary.Shared.ThemeManager;
import mob.mydiary.Shared.ScreenHelper;
import mob.mydiary.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private ViewPager ViewPager_diary_content;

    private long topicId;
    private boolean hasEntries;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private RadioButton But_diary_topbar_entries, But_diary_topbar_calendar, But_diary_topbar_diary;

    private List<EntriesEntity> entriesList = new ArrayList<>();
    private final static int MAX_TEXT_LENGTH = 18;


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                default:
                    But_diary_topbar_entries.setChecked(true);
                    break;
                case 1:
                    But_diary_topbar_calendar.setChecked(true);
                    break;
                case 2:
                    But_diary_topbar_diary.setChecked(true);
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
                ThemeManager.getInstance().getEntriesBgDrawable(this, getTopicId()));
        if (!hasEntries) {
            ViewPager_diary_content.setCurrentItem(2);
            //Set Default Checked Item
            But_diary_topbar_diary.setChecked(true);
        } else {
            //Set Default Checked Item
            But_diary_topbar_entries.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mViewPager = (ViewPager)findViewById(R.id.VP_ViewPage);
        hasEntries = getIntent().getBooleanExtra("has_entries", true);


        Button btn_entries = (Button)findViewById(R.id.btn_entries);
        Button btn_calendar = (Button)findViewById(R.id.btn_calendar);
        Button btn_diary = (Button)findViewById(R.id.btn_diary);



        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
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

        mViewPager.setCurrentItem(0);

    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter
    {
        Context context;
        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment;
            switch(position)
            {
                case 0:
                    fragment = new EntriesFragment();
                    break;
                case 1:
                    fragment = new CalendarFragment();
                    break;
                case 2:
                    fragment = new DiaryFragment();
                    break;
                default:
                    return null;
            }
            return fragment;
        }
        @Override
        public int getCount()
        {
            return 3;
        }

    }
    @Override
    public void onClick(View v)
    {
        int tag = (int)v.getTag();
        mViewPager.setCurrentItem(tag);
    }

    public long getTopicId() {
        return topicId;
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
        Cursor diaryCursor = dbManager.selectDiaryList(getTopicId());
        for (int i = 0; i < diaryCursor.getCount(); i++) {
            //get diary info
            String title = diaryCursor.getString(2);
            if ("".equals(title)) {
                title = getString(R.string.diary_no_title);
            }
            EntriesEntity entity = new EntriesEntity(diaryCursor.getLong(0),
                    new Date(diaryCursor.getLong(1)),
                    title.substring(0, Math.min(MAX_TEXT_LENGTH, title.length())),
                    diaryCursor.getInt(4), diaryCursor.getInt(3),
                    diaryCursor.getInt(5) > 0 ? true : false);

            //select first diary content
            Cursor diaryContentCursor = dbManager.selectDiaryContentByDiaryId(entity.getId());
            if (diaryContentCursor != null && diaryContentCursor.getCount() > 0) {
                String summary = "";
                //Check content Type
                if (diaryContentCursor.getInt(1) == IDairyRow.TYPE_PHOTO) {
                    summary = getString(R.string.entries_summary_photo);
                } else if (diaryContentCursor.getInt(1) == IDairyRow.TYPE_TEXT) {
                    summary = diaryContentCursor.getString(3)
                            .substring(0, Math.min(MAX_TEXT_LENGTH, diaryContentCursor.getString(3).length()));
                }
                entity.setSummary(summary);
                diaryContentCursor.close();
            }
            //Add entity
            entriesList.add(entity);
            diaryCursor.moveToNext();
        }
        diaryCursor.close();
        dbManager.closeDB();
    }
}

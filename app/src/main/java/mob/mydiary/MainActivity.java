package mob.mydiary;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mob.mydiary.Calendar.CalendarFragment;
import mob.mydiary.Diary.DiaryFragment;
import mob.mydiary.Entries.EntriesFragment;
import mob.mydiary.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        mViewPager = (ViewPager)findViewById(R.id.VP_ViewPage);


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

}

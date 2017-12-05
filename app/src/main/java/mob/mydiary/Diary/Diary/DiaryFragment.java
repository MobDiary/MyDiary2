package mob.mydiary.Diary.Diary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import mob.mydiary.Diary.BaseFragment;
import mob.mydiary.R;

public class DiaryFragment extends BaseFragment implements View.OnClickListener{

    public DiaryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}

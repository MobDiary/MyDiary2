package mob.mydiary.Entries;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import mob.mydiary.BaseFragment;
import mob.mydiary.R;

public class EntriesFragment extends BaseFragment {
    private RecyclerView RecyclerView_entries;

    public EntriesFragment() {
        // Required empty public constructor
    }

    public void gotoDiaryPosition(int position) {
        RecyclerView_entries.scrollToPosition(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);
        return rootView;
    }
}
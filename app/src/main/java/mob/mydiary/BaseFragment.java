package mob.mydiary;

import android.support.v4.app.Fragment;

import java.util.List;
import mob.mydiary.MainActivity;
import mob.mydiary.Entries.EntriesEntity;


public class BaseFragment extends Fragment{
    public long getTopicId() {
        return ((MainActivity) getActivity()).getTopicId();
    }


    public List<EntriesEntity> getEntriesList() {
        return ((MainActivity) getActivity()).getEntriesList();
    }

    public void updateEntriesList() {
        ((MainActivity) getActivity()).loadEntries();
    }
}

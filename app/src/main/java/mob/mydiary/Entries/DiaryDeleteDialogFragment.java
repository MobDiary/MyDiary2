package mob.mydiary.Entries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import mob.mydiary.R;
import mob.mydiary.DB.DBManager;
import mob.mydiary.Manager.FileManager;
import mob.mydiary.Manager.gui.CommonDialogFragment;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.deleteDirectory;

public class DiaryDeleteDialogFragment extends CommonDialogFragment {

    private DeleteCallback callback;

    /**
     * Callback
     */
    public interface DeleteCallback {
        void onDiaryDelete();
    }

    private long diaryId;

    public static DiaryDeleteDialogFragment newInstance(long diaryId) {
        Bundle args = new Bundle();
        DiaryDeleteDialogFragment fragment = new DiaryDeleteDialogFragment();
        args.putLong("diaryId", diaryId);
        fragment.setArguments(args);
        return fragment;
    }

    private void deleteDiary() {
        //Delete the db
        DBManager dbManager = new DBManager(getActivity());
        dbManager.openDB();
        dbManager.delDiary(diaryId);
        dbManager.closeDB();
        //Delete photo data
        try {
            deleteDirectory(new FileManager(getActivity()).getDir());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        callback = (DeleteCallback) getTargetFragment();
        this.getDialog().setCanceledOnTouchOutside(false);
        super.onViewCreated(view, savedInstanceState);
        diaryId = getArguments().getLong("diaryId", -1L);
        this.TV_common_content.setText(getString(R.string.entries_edit_dialog_delete_content));
    }

    @Override
    protected void okButtonEvent() {
        if (diaryId != -1) {
            deleteDiary();
            this.callback.onDiaryDelete();
        }
        dismiss();
    }

    @Override
    protected void cancelButtonEvent() {
        dismiss();
    }
}

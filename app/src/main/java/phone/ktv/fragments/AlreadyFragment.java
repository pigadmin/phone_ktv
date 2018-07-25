package phone.ktv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import phone.ktv.R;

/**
 * 已点歌曲 1级
 */
public class AlreadyFragment extends Fragment {
    View mNewsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        return mNewsView;
    }
}

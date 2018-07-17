package phone.ktv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import phone.ktv.R;

/**
 *  排行榜
 */
public class RankingListFragment extends Fragment {

    View mNewsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.ranking_fragment_layout, null);
        return mNewsView;
    }
}

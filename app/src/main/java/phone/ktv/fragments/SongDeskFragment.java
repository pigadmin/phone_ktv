package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.R;
import phone.ktv.bgabanner.BGABanner;

/**
 * 点歌台
 */
public class SongDeskFragment extends Fragment{

    private static final String TAG = "SongDeskFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.songdesk_fragment_layout, null);

        mContext=getActivity();

        initView();
        initLiter();
        return mNewsView;
    }

    private void initView(){
        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);
    }

    private void initLiter(){
        List<Integer> int1=new ArrayList<>();
        int1.add(R.mipmap.lu_1);
        int1.add(R.mipmap.lu_2);
        int1.add(R.mipmap.lu_3);

        List<String> int2=new ArrayList<>();
        int2.add("图1");
        int2.add("图2");
        int2.add("图3");

        mBanner.setData(int1, int2);
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

            }
        });
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(mContext)
                        .load(model)
                        .placeholder(R.mipmap.lu_1)
                        .error(R.mipmap.lu_1)
                        .fitCenter()
                        .thumbnail(0.7f)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into((ImageView) view);
            }
        });
    }
}

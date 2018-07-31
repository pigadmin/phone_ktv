package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.views.CustomPopuWindw;

/**
 * 已点歌曲 1级
 */
public class AlreadyFragment extends Fragment {

    private static final String TAG = "AlreadyFragment";

    View mNewsView;
    public Context mContext;

    public ImageView mTitle1;//选择 播放形式:顺序播放、随机播放、单曲循环

    private TextView mTitle2;//全部播放

    private TextView mTitle3;//多选

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        mContext = getActivity();
        initView();
        initLiter();
        return mNewsView;
    }

    private void initView() {
        mTitle1 = mNewsView.findViewById(R.id.title_1_ivw);
        mTitle2 = mNewsView.findViewById(R.id.title_2_tvw);
        mTitle3 = mNewsView.findViewById(R.id.title_3_tvw);
    }

    private void initLiter() {
        mTitle1.setOnClickListener(new MyOnClickListenTitle1());
        mTitle2.setOnClickListener(new MyOnClickListenTitle2());
        mTitle3.setOnClickListener(new MyOnClickListenTitle3());
    }

    /**
     * 播放形式
     */
    private class MyOnClickListenTitle1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showPoWindo();
        }
    }

    /**
     * 全部播放
     */
    private class MyOnClickListenTitle2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 多选
     */
    private class MyOnClickListenTitle3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private void showPoWindo() {
        final CustomPopuWindw windw = new CustomPopuWindw(getActivity());
        windw.showPopupWindow(mNewsView.findViewById(R.id.title_1_ivw));
        windw.mPlayStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc1, windw.mText1, windw.mSrc1Go, 1);
                sq(1);
                windw.dismiss();
            }
        });
        windw.mPlayStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc2, windw.mText2, windw.mSrc2Go, 2);
                sq(2);
                windw.dismiss();
            }
        });
        windw.mPlayStyle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc3, windw.mText3, windw.mSrc3Go, 3);
                sq(3);
                windw.dismiss();
            }
        });
    }

    private void sq(int index){
        switch (index){
            case 1:
                mTitle1.setImageResource(R.mipmap.popovers_x_0);
                break;

            case 2:
                mTitle1.setImageResource(R.mipmap.popovers_s_0);
                break;

            case 3:
                mTitle1.setImageResource(R.mipmap.popovers_n_0);
                break;
        }
    }
}

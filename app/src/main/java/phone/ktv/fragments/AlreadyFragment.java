package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.R;
import phone.ktv.adaters.AlreadyListAdater;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.views.CustomPopuWindw;
import phone.ktv.views.MyListView;

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

    private TextView mCancel12;//取消
    private CheckBox mSelectionTotal;//全选
    private TextView mTitle11;//已选多少首

    private AlreadyListAdater mAlreadyListAdater;
    private MyListView mListView;

    private List<MusicPlayBean> mPlayBeanList;


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
        mPlayBeanList = new ArrayList<>();

        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));
        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));
        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));
        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));
        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));
        mPlayBeanList.add(new MusicPlayBean("周杰伦", "告白气球", "HD"));

        mTitle1 = mNewsView.findViewById(R.id.title_1_ivw);
        setLogo(CustomPopuWindw.postion);

        mTitle2 = mNewsView.findViewById(R.id.title_2_tvw);
        mTitle3 = mNewsView.findViewById(R.id.title_3_tvw);

        mCancel12 = mNewsView.findViewById(R.id.cancel_top_12);//取消
        mSelectionTotal = mNewsView.findViewById(R.id.delete_all_cbx119);//全选
        mTitle11 = mNewsView.findViewById(R.id.title_11_ivw);//已选多少首

        mListView = mNewsView.findViewById(R.id.list_view_29);
        mAlreadyListAdater = new AlreadyListAdater(mContext, R.layout.item_songdesk_list_layout, mPlayBeanList);
        mListView.setAdapter(mAlreadyListAdater);
    }

    private void initLiter() {
        mTitle1.setOnClickListener(new MyOnClickListenTitle1());
        mTitle2.setOnClickListener(new MyOnClickListenTitle2());
        mTitle3.setOnClickListener(new MyOnClickListenTitle3());

        mCancel12.setOnClickListener(new MyOnClickListenTitle4());
        mSelectionTotal.setOnClickListener(new MyOnClickListenTitle5());
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
            mNewsView.findViewById(R.id.inclub_ilb_9).setVisibility(View.GONE);
            mNewsView.findViewById(R.id.inclub_ilb_10).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 取消
     */
    private class MyOnClickListenTitle4 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 全选
     */
    private class MyOnClickListenTitle5 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private void showPoWindo() {
        final CustomPopuWindw windw = new CustomPopuWindw(getActivity());
        windw.showPopupWindow(mTitle1);
        windw.mPlayStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc1, windw.mText1, windw.mSrc1Go, 1);
                setLogo(CustomPopuWindw.postion);
                windw.dismiss();
            }
        });
        windw.mPlayStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc2, windw.mText2, windw.mSrc2Go, 2);
                setLogo(CustomPopuWindw.postion);
                windw.dismiss();
            }
        });
        windw.mPlayStyle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windw.setState(windw.mSrc3, windw.mText3, windw.mSrc3Go, 3);
                setLogo(CustomPopuWindw.postion);
                windw.dismiss();
            }
        });
    }

    private void setLogo(int index) {
        switch (index) {
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

package phone.ktv.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import phone.ktv.R;
import phone.ktv.adaters.AlreadyListAdater;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.ToastUtils;
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

    private TextView mCancel12;//取消
    private CheckBox mSelectionTotal;//全选
    private TextView mTitle11;//已选多少首

    private AlreadyListAdater mAlreadyListAdater;
    private ListView mListView;

    private List<MusicPlayBean> mPlayBeanList;

    public Snackbar mSnackbar;

    public TextView mNoData25;

    public static final int Search_Music_Success = 100;
    public static final int Search_Music_Failure = 200;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Search_Music_Success:
                    mNoData25.setVisibility(View.GONE);
                    mAlreadyListAdater.notifyDataSetChanged();
                    break;
                case Search_Music_Failure:
                    mNoData25.setVisibility(View.VISIBLE);
                    mAlreadyListAdater.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        mContext = getActivity();

        initView();
        initLiter();
        isMusicStateList();
        return mNewsView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isMusicStateList();
    }

    /**
     * 查询DB
     */
    private void isMusicStateList() {
        mPlayBeanList.clear();
        try {
            List<MusicPlayBean> playBeans = App.mDb.selector(MusicPlayBean.class).findAll();//数据库查询
            if (playBeans != null && !playBeans.isEmpty()) {
                Logger.d(TAG, "playBeans.." + playBeans.size());
                for (MusicPlayBean playBean : playBeans) {
                    playBean.isState = false;
                    mPlayBeanList.add(playBean);
                    Logger.d(TAG, "playBeans.." + playBean.toString());
                }
                handler.sendEmptyMessage(Search_Music_Success);
            } else {
                handler.sendEmptyMessage(Search_Music_Failure);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i(TAG, "DB查询异常.." + e.getMessage());
        }
    }

    private void updateData(List<MusicPlayBean> playBeans, boolean isState) {
        if (playBeans != null && !playBeans.isEmpty()) {
            for (MusicPlayBean playBean : playBeans) {
                playBean.isState = isState;
            }
            mAlreadyListAdater.notifyDataSetChanged();
        }
    }

    private void initView() {
        mPlayBeanList = new ArrayList<>();
        mTitle1 = mNewsView.findViewById(R.id.title_1_ivw);
        setLogo(CustomPopuWindw.postion);

        mTitle2 = mNewsView.findViewById(R.id.title_2_tvw);
        mTitle3 = mNewsView.findViewById(R.id.title_3_tvw);

        mNoData25 = mNewsView.findViewById(R.id.no_data_tvw25);
        mCancel12 = mNewsView.findViewById(R.id.cancel_top_12);//取消
        mSelectionTotal = mNewsView.findViewById(R.id.delete_all_cbx119);//全选
        mTitle11 = mNewsView.findViewById(R.id.title_11_ivw);//已选多少首

        mListView = mNewsView.findViewById(R.id.list_view_29);

        mAlreadyListAdater = new AlreadyListAdater(mContext, R.layout.item_songdesk_list_layout, mPlayBeanList, mCallBack);
        mListView.setAdapter(mAlreadyListAdater);
    }

    private void initLiter() {
        mTitle1.setOnClickListener(new MyOnClickListenTitle1());
        mTitle2.setOnClickListener(new MyOnClickListenTitle2());
        mTitle3.setOnClickListener(new MyOnClickListenTitle3());

        mCancel12.setOnClickListener(new MyOnClickListenTitle4());
        mSelectionTotal.setOnCheckedChangeListener(new MyOnClickListenTitle5());
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
            if (mPlayBeanList != null) {
                mContext.sendBroadcast(new Intent(App.PLAY));
            }
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
            mAlreadyListAdater.setUpdateType(true);
            showSnackbar();
        }
    }

    /**
     * 取消
     */
    private class MyOnClickListenTitle4 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mNewsView.findViewById(R.id.inclub_ilb_9).setVisibility(View.VISIBLE);
            mNewsView.findViewById(R.id.inclub_ilb_10).setVisibility(View.GONE);
            mAlreadyListAdater.setUpdateType(false);
            updateData(mPlayBeanList, false);
            mTitle11.setText("已选" + getSongNum(mPlayBeanList) + "首");

            mSelectionTotal.setChecked(false);
            if (mSnackbar != null) {
                mSnackbar.dismiss();
            }
        }
    }

    /**
     * 判断当前的Fragment是否可见(种方式只限于再viewpager时使用)
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.d(TAG, "已点歌曲isVisibleToUser.." + isVisibleToUser);
        if (!isVisibleToUser) {
            if (mSnackbar != null) {
                mSnackbar.dismiss();
            }
            if (mAlreadyListAdater != null) {
                mAlreadyListAdater.setUpdateType(false);
                updateData(mPlayBeanList, false);
            }
            if (mNewsView != null) {
                mNewsView.findViewById(R.id.inclub_ilb_9).setVisibility(View.VISIBLE);
                mNewsView.findViewById(R.id.inclub_ilb_10).setVisibility(View.GONE);
            }
            if (mSelectionTotal != null) {
                mSelectionTotal.setChecked(false);
            }
        }
    }

    /**
     * 全选
     */
    private class MyOnClickListenTitle5 implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateData(mPlayBeanList, isChecked ? true : false);
            mTitle11.setText("已选" + getSongNum(mPlayBeanList) + "首");
        }
    }

    /**
     * 获取勾选数目
     *
     * @param playBeans
     * @return
     */
    private int getSongNum(List<MusicPlayBean> playBeans) {
        if (playBeans != null && !playBeans.isEmpty()) {
            int num = 0;
            for (MusicPlayBean playBean : playBeans) {
                if (playBean.isState) {
                    num++;
                }
            }
            return num;
        }
        return 0;
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

    /**
     * 删除事件
     */
    private void operationTotal() {
        if (getSongNum(mPlayBeanList) > 0) {
            try {
                Iterator<MusicPlayBean> iterator = mPlayBeanList.iterator();
                while (iterator.hasNext()) {
                    MusicPlayBean playBean = iterator.next();
                    if (playBean.isState) {
                        App.mDb.delete(playBean);//先删除DB数据
                        iterator.remove();//再删本地列表
                        mAlreadyListAdater.notifyDataSetChanged();
                    }
                }

                mSelectionTotal.setChecked(false);
                mTitle11.setText("已选" + mPlayBeanList.size() + "首");
                ToastUtils.showShortToast(mContext, "删除成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showLongToast(mContext, "请先勾选歌曲");
        }
        showSnackbar();
    }


    public void showSnackbar() {
        mSnackbar = Snackbar.make(getActivity().getWindow().getDecorView(), "确定删除吗?", Snackbar.LENGTH_INDEFINITE);
        final View view = mSnackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(mContext.getResources().getColor(R.color.white));//设置字体的颜色
        view.setBackgroundColor(mContext.getResources().getColor(R.color.bule));//设置背景颜色
        mSnackbar.setActionTextColor(mContext.getResources().getColor(R.color.white));
        mSnackbar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationTotal();
            }
        });
        mSnackbar.show();
    }

    AlreadyListAdater.OnCallBack mCallBack = new AlreadyListAdater.OnCallBack() {
        @Override
        public void onSelectedListener(int index) {
            MusicPlayBean bean = mPlayBeanList.get(index);
            if (bean.isState) {
                bean.isState = false;
            } else {
                bean.isState = true;
            }
            mAlreadyListAdater.notifyDataSetChanged();
            mTitle11.setText("已选" + getSongNum(mPlayBeanList) + "首");
        }
    };
}

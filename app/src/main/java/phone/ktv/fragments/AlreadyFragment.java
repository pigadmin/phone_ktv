package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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
    private List<Boolean> selectedStatus;

    public Snackbar mSnackbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        mContext = getActivity();
        initView();
        initLiter();
        return mNewsView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mPlayBeanList = App.mDb.selector(MusicPlayBean.class).findAll();
        } catch (Exception e) {
            Logger.d(TAG, "e.." + e.getMessage());
        }
    }

    private void initView() {
        mPlayBeanList = new ArrayList<>();
        selectedStatus = new ArrayList<>();

        mTitle1 = mNewsView.findViewById(R.id.title_1_ivw);
        setLogo(CustomPopuWindw.postion);

        mTitle2 = mNewsView.findViewById(R.id.title_2_tvw);
        mTitle3 = mNewsView.findViewById(R.id.title_3_tvw);

        mCancel12 = mNewsView.findViewById(R.id.cancel_top_12);//取消
        mSelectionTotal = mNewsView.findViewById(R.id.delete_all_cbx119);//全选
        mTitle11 = mNewsView.findViewById(R.id.title_11_ivw);//已选多少首

        mListView = mNewsView.findViewById(R.id.list_view_29);

        initBooleanList();
        mAlreadyListAdater = new AlreadyListAdater(mContext, R.layout.item_songdesk_list_layout, mPlayBeanList, selectedStatus, mTitle11);
        mListView.setAdapter(mAlreadyListAdater);
    }

    private void initLiter() {
        mTitle1.setOnClickListener(new MyOnClickListenTitle1());
        mTitle2.setOnClickListener(new MyOnClickListenTitle2());
        mTitle3.setOnClickListener(new MyOnClickListenTitle3());

        mCancel12.setOnClickListener(new MyOnClickListenTitle4());
        mSelectionTotal.setOnCheckedChangeListener(new MyOnClickListenTitle5());
        mListView.setOnItemClickListener(new MyOnItemClickLiter());
    }

    private void initBooleanList() {
        if (mPlayBeanList != null) {
            for (int i = 0; i < mPlayBeanList.size(); i++) {
                selectedStatus.add(false);
            }
        }
    }

    /**
     * item 事件
     */
    private class MyOnItemClickLiter implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
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
            mAlreadyListAdater.setUpdateState(selectedStatus, false);
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
                mAlreadyListAdater.setUpdateState(selectedStatus, false);
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
            mAlreadyListAdater.setUpdateState(selectedStatus, isChecked ? true : false);
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

    public void showSnackbar() {
        mSnackbar = Snackbar.make(getActivity().getWindow().getDecorView(), "确定删除吗?", Snackbar.LENGTH_INDEFINITE);
        final View view = mSnackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(mContext.getResources().getColor(R.color.white));//设置字体的颜色
        view.setBackgroundColor(mContext.getResources().getColor(R.color.bule));//设置背景颜色
        mSnackbar.setActionTextColor(mContext.getResources().getColor(R.color.white));
        mSnackbar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlreadyListAdater.getSelectNum() > 0) {
                    ToastUtils.showLongToast(mContext, "删除成功");
                } else {
                    ToastUtils.showLongToast(mContext, "请先勾选歌曲");
                    showSnackbar();
                }
            }
        });
        mSnackbar.show();
    }
}

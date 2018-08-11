package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.activitys.rank_activitys.RankingListActivity;
import phone.ktv.activitys.rank_activitys.RankingMoreActivity;
import phone.ktv.adaters.RinkingFragmentAdater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridItem;
import phone.ktv.bean.NoticeBean;
import phone.ktv.bgabanner.BGABanner;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 排行榜 1级
 */
public class RankingFragment extends Fragment {

    private static final String TAG = "RankingFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    private RinkingFragmentAdater mDeskAdater;

    private GridView mGridView;

    private SPUtil mSP;

    private List<GridItem> mGridItemList;

    public static final int RankingListSuccess = 100;//排行榜分类获取成功
    public static final int RankingListError = 200;//排行榜分类获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    public static final int NoticeSuccess = 400;
    public static final int NoticeError = 500;

    private TextView mMore;//更多

    private TextView mErrorRetry;

    List<String> int1 = new ArrayList<>();
    List<String> int2 = new ArrayList<>();

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingListSuccess://获取成功
                    updateData();
                    break;

                case RankingListError://获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    updateData();
                    break;

                case RankingExpiredToken://Token过期
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    updateData();
                    break;

                case NoticeSuccess:
                    updaNotice();
                    break;

                case NoticeError:
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.ranking_fragment_layout, null);

        mContext = getActivity();
        mSP = new SPUtil(mContext);
        initView();
        initLiter();
        return mNewsView;
    }

    private void updaNotice() {
        mBanner.setData(int1, int2);
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

    private void initView() {
        mGridItemList = new ArrayList<>();

        mMore = mNewsView.findViewById(R.id.more1);

        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);

        mErrorRetry = mNewsView.findViewById(R.id.error_btn_retry_2);

        mGridView = mNewsView.findViewById(R.id.grid_view_1);
        mDeskAdater = new RinkingFragmentAdater(mContext, R.layout.item_gridicon_image, mGridItemList);
        mGridView.setAdapter(mDeskAdater);
    }

    private void initLiter() {
        mMore.setOnClickListener(new MyOnClickListenerMore());//更多
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        mErrorRetry.setOnClickListener(new MyOnClickListenerErrorRetry());
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

            }
        });
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GridItem item = mGridItemList.get(position);
            if (item != null) {
                IntentUtils.strIntentString(mContext, RankingListActivity.class, "rangId", "rangName", item.id, item.name);
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
        Logger.d(TAG, "排行榜当前isVisibleToUser.." + isVisibleToUser);
        if (isVisibleToUser) {
            getRankingListData();
            getSongNoitce();
        }
    }

    private void updateData() {
        mDeskAdater.notifyDataSetChanged();
        if (mGridItemList != null && !mGridItemList.isEmpty()) {
            mErrorRetry.setVisibility(View.GONE);
        } else {
            mErrorRetry.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 无数据加载
     */
    private class MyOnClickListenerErrorRetry implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getRankingListData();
            getSongNoitce();
        }
    }

    private class MyOnClickListenerMore implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IntentUtils.thisToOther(mContext, RankingMoreActivity.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setState(List<GridItem> itemList) {
        mGridItemList.clear();
        if (itemList != null && !itemList.isEmpty()) {
            if (itemList.size() > 9) {
                for (int i = 0; i < 9; i++) {
                    GridItem item = itemList.get(i);
                    mGridItemList.add(item);
                }
            } else {
                mGridItemList.addAll(itemList);
            }
        }
    }

    /**
     * 获取排行榜分类
     */
    private void getRankingListData() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "song/rangking", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(RankingListError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson<List<GridItem>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<GridItem>>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            Logger.i(TAG, "aJson..." + aJson.toString());
                            setState(aJson.getData());
                            mHandler.sendEmptyMessage(RankingListSuccess);
                        } else if (aJson.getCode() == 500) {
                            mHandler.obtainMessage(RankingExpiredToken, aJson.getMsg()).sendToTarget();
                        } else {
                            mHandler.obtainMessage(RankingListError, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 轮播图(广告)
     */
    private void getSongNoitce() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "rollad", weakHashMap);
        Logger.i(TAG, "url.." + url);
        if (NetUtils.hasNetwork(mContext)) {
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    Logger.i(TAG, "result.." + result + "..msg.." + msg);
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(NoticeError, msg).sendToTarget();
                    } else {
                        AJson aJson = GsonJsonUtils.parseJson2Obj(result, AJson.class);
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                String str = GsonJsonUtils.parseObj2Json(aJson.getData());
                                List<NoticeBean> noticeBean = GsonJsonUtils.parseJson2Obj(str, new TypeToken<List<NoticeBean>>() {
                                });
                                getNoticeData(noticeBean);
                                mHandler.sendEmptyMessage(NoticeSuccess);
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(NoticeError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void getNoticeData(List<NoticeBean> noticeBean) {
        if (noticeBean != null && !noticeBean.isEmpty()) {
            int1.clear();
            int2.clear();
            for (NoticeBean bean : noticeBean) {
                int1.add(bean.path);
                int2.add(bean.name);
            }
        }
    }
}


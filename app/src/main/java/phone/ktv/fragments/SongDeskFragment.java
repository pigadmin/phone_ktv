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

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import phone.ktv.R;
import phone.ktv.activitys.songdesk_activitys.SongDeskActivity2;
import phone.ktv.activitys.songdesk_activitys.SongDeskjMoreActivity;
import phone.ktv.adaters.SongDeskGrid1Adater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridList;
import phone.ktv.bean.ListInfo;
import phone.ktv.bean.NoticeBean;
import phone.ktv.bgabanner.BGABanner;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 点歌台(歌曲大类) 1级
 */
public class SongDeskFragment extends Fragment {

    private static final String TAG = "SongDeskFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    private SongDeskGrid1Adater mDeskAdater;

    private GridView mGridView;

    private SPUtil mSP;

    private List<ListInfo> mGridItemList;

    public static final int RankingListSuccess = 100;//点歌台分类获取成功
    public static final int RankingListError = 200;//点歌台分类获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    public static final int NoticeSuccess = 400;
    public static final int NoticeError = 500;

    private SVProgressHUD mSvProgressHUD;

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
            mSvProgressHUD.dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.songdesk_fragment_layout, null);

        mContext = getActivity();
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        initView();
        initLiter();
        getRankingListData();
        getSongNoitce();
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

        mMore = mNewsView.findViewById(R.id.more15);

        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);

        mErrorRetry = mNewsView.findViewById(R.id.error_btn_retry_1);

        mGridView = mNewsView.findViewById(R.id.grid_view_1231);
        mDeskAdater = new SongDeskGrid1Adater(mContext, R.layout.item_gridicon_image, mGridItemList);
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
            ListInfo item = mGridItemList.get(position);
            if (item != null) {
                IntentUtils.strIntentString(mContext, SongDeskActivity2.class, "id", "name", item.getId(), item.getName());
            }
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
            IntentUtils.thisToOther(mContext, SongDeskjMoreActivity.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setState(List<ListInfo> itemList) {
        mGridItemList.clear();
        if (itemList != null && !itemList.isEmpty()) {
            if (itemList.size() > 9) {
                for (int i = 0; i < 9; i++) {
                    ListInfo item = itemList.get(i);
                    mGridItemList.add(item);
                }
            } else {
                mGridItemList.addAll(itemList);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 获取点歌台分类
     */
    private void getRankingListData() {
        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "song/getSongType", weakHashMap);
        Logger.i(TAG, "url.." + url);
        if (NetUtils.hasNetwork(mContext)) {
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(RankingListError, msg).sendToTarget();
                    } else {
                        AJson aJson = GsonJsonUtils.parseJson2Obj(result, AJson.class);
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                GridList gridList = App.jsonToObject(result, new TypeToken<AJson<GridList>>() {
                                }).getData();
                                Logger.i(TAG, "aJson1..." + aJson.toString());
                                setState(gridList.getList());
                                mHandler.sendEmptyMessage(RankingListSuccess);
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(RankingListError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
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
            mSvProgressHUD.dismiss();
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

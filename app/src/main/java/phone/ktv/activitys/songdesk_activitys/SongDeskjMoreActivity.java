package phone.ktv.activitys.songdesk_activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import phone.ktv.R;
import phone.ktv.adaters.SongDeskGrid1Adater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridList;
import phone.ktv.bean.ListInfo;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.MyGridView;

/**
 * 点歌台分类(歌曲大类-更多) 1级
 */
public class SongDeskjMoreActivity extends phone.ktv.BaseActivity {

    private static final String TAG = "SongDeskjMoreActivity";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private MyGridView mGridView;

    private SongDeskGrid1Adater mGridAdater;

    private List<ListInfo> mGridItemList;

    public static final int SongDeskMoreSuccess = 100;//点歌台分类获取成功
    public static final int SongDeskMoreError = 200;//点歌台分类获取失败
    public static final int SongDeskExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;

    private SPUtil mSP;

    private TextView mNoData;

    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private int mLimit = App.MaxlimitSrc;//页码量
    private int mPage = 1;//第几页

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SongDeskMoreSuccess://获取成功
                    mGridAdater.notifyDataSetChanged();
                    updateData();
                    break;

                case SongDeskMoreError://获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case SongDeskExpiredToken://Token过期
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
            }
            mSvProgressHUD.dismiss();
            mPullToRefresh.onRefreshComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songdesk_more_activity);

        initView();
        initLiter();
        settingPullRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateData() {
        if (mGridItemList != null && !mGridItemList.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mGridItemList = new ArrayList<>();

        mContext = SongDeskjMoreActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);
        mPullToRefresh = findViewById(R.id.sv);
        mNoData = findViewById(R.id.no_data_tvw123);
        mGridView = findViewById(R.id.grid_view_8);
        mGridAdater = new SongDeskGrid1Adater(mContext, R.layout.item_gridicon_image, mGridItemList);
        mGridView.setAdapter(mGridAdater);

        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        getRankingListData();
    }

    private void initLiter() {
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
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

    /**
     * PullToRefreshScrollView 属性
     */
    private void settingPullRefresh() {
        mPullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        mLoadingLayoutProxy = mPullToRefresh.getLoadingLayoutProxy(true, false);
        mLoadingLayoutProxy.setPullLabel("下拉刷新");
        mLoadingLayoutProxy.setRefreshingLabel("正在刷新");
        mLoadingLayoutProxy.setReleaseLabel("松开刷新");

        ILoadingLayout endLoading = mPullToRefresh.getLoadingLayoutProxy(false, true);
        endLoading.setPullLabel("上拉加载更多");
        endLoading.setRefreshingLabel("拼命加载中...");
        endLoading.setReleaseLabel("释放即可加载更多");
    }

    /**
     * 下拉刷新
     */
    private class MyPullToRefresh implements PullToRefreshBase.OnRefreshListener2 {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage = 1;
            mGridItemList.clear();
            getRankingListData();
        }

        /**
         * 上拉加载
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage++;
            getRankingListData();
        }
    }

    /**
     * 获取排行榜分类(更多)
     */
    private void getRankingListData() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("page", mPage + "");//第几页    不填默认1
        weakHashMap.put("limit", mLimit + "");//页码量   不填默认10，最大限度100

        String url = App.getRqstUrl(App.headurl + "song/getSongType", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(SongDeskMoreError, msg).sendToTarget();
                    } else {
                        AJson aJson = GsonJsonUtils.parseJson2Obj(result, AJson.class);
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                GridList gridList = App.jsonToObject(result, new TypeToken<AJson<GridList>>() {
                                }).getData();
                                Logger.i(TAG, "aJson..." + aJson.toString());
                                setState(gridList.getList());
                                mHandler.sendEmptyMessage(SongDeskMoreSuccess);
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(SongDeskExpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(SongDeskMoreError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            mPullToRefresh.onRefreshComplete();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void setState(List<ListInfo> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            mGridItemList.addAll(itemList);
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

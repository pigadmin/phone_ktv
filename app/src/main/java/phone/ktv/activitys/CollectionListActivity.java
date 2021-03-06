package phone.ktv.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import phone.ktv.BaseActivity;
import phone.ktv.R;
import phone.ktv.adaters.CollectionListAdater;
import phone.ktv.app.App;
import phone.ktv.bean.ColleResultBean;
import phone.ktv.bean.CollentBean1;
import phone.ktv.bean.CollentBean2;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.LatelyListAddUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomPopuWindw;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.MyListView;

/**
 * 收藏列表
 */
public class CollectionListActivity extends BaseActivity {

    private static final String TAG = "CollectionListActivity";

    Context mContext;

    private MyListView mListView1;
    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CollectionListAdater mCollectionAdater;

    public ImageView mTitle1;//选择 播放形式:顺序播放、随机播放、单曲循环
    private TextView mTitle2;//全部播放
    private TextView mTitle3;//一键清空

    public static final int RankingSearch2Success = 100;//搜索歌曲获取成功
    public static final int RankingSearch2Error = 200;//搜索歌曲获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;
    private SPUtil mSP;

    private TextView mNoData;

    private List<MusicPlayBean> mCollentBean3s;

    private int mLimit = App.Maxlimit;//页码量
    private int mPage = 1;//第几页


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingSearch2Success:
                    mCollectionAdater.notifyDataSetChanged();
                    updateData();
                    break;

                case RankingSearch2Error:
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case RankingExpiredToken:
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
        setContentView(R.layout.collect_list_activity);

        initView();
        initLiter();
        settingPullRefresh();
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

    private void initView() {
        mCollentBean3s = new ArrayList<>();

        mContext = CollectionListActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw15);
        mTopTitleView1 = findViewById(R.id.customTopTitleView1);

        mPullToRefresh = findViewById(R.id.sv);

        mTitle1 = findViewById(R.id.title_10_ivw);
        setLogo(CustomPopuWindw.postion);

        mTitle2 = findViewById(R.id.title_20_tvw);
        mTitle3 = findViewById(R.id.title_30_tvw);
        mTitle3.setVisibility(View.INVISIBLE);

        mListView1 = findViewById(R.id.list_view_21);
        mCollectionAdater = new CollectionListAdater(CollectionListActivity.this, R.layout.item_collection_list_layout, mCollentBean3s, true, mSP);
        mListView1.setAdapter(mCollectionAdater);

        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        getSongNameData();
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
        mTitle1.setOnClickListener(new MyOnClickListenTitle1());
        mTitle2.setOnClickListener(new MyOnClickListenTitle2());
        mTitle3.setOnClickListener(new MyOnClickListenTitle3());
        mListView1.setOnItemClickListener(new MyOnItemClickLister());
    }

    /**
     * item事件
     */
    private class MyOnItemClickLister implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MusicPlayBean bean = mCollentBean3s.get(position);
            LatelyListAddUtils utils = new LatelyListAddUtils(mSP, CollectionListActivity.this, bean);
            utils.getLatelyList();
            toplay(bean);
        }
    }

    private void toplay(MusicPlayBean bean) {
        App.saveData(bean, mContext, TAG, false);
        sendBroadcast(new Intent(App.PLAY));
    }

    /**
     * 下拉刷新
     */
    private class MyPullToRefresh implements PullToRefreshBase.OnRefreshListener2 {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage = 1;
            mCollentBean3s.clear();
            getSongNameData();
        }

        /**
         * 上拉加载
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage++;
            getSongNameData();
        }
    }


    private void updateData() {
        if (mCollentBean3s != null && !mCollentBean3s.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
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

    /**
     * 获取收藏列表
     */
    private void getSongNameData() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("page", mPage + "");//第几页    不填默认1
        weakHashMap.put("limit", mLimit + "");//页码量   不填默认10，最大限度100

        String url = App.getRqstUrl(App.headurl + "song/collect", weakHashMap);

        Logger.i(TAG, "url.." + url);
        if (NetUtils.hasNetwork(mContext)) {
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(RankingSearch2Error, msg).sendToTarget();
                    } else {
                        analysisJson(result);
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            mPullToRefresh.onRefreshComplete();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void analysisJson(String result) {
        ColleResultBean aJson = GsonJsonUtils.parseJson2Obj(result, ColleResultBean.class);
        if (aJson != null) {
            if (aJson.code == 0) {
                Logger.i(TAG, "aJson..." + aJson.toString());
                String str = GsonJsonUtils.parseObj2Json(aJson.data);
                CollentBean1 collentBean1 = GsonJsonUtils.parseJson2Obj(str, CollentBean1.class);
                mSP.putInt("collectListSize", Integer.parseInt(collentBean1.totalCount));
                String string = GsonJsonUtils.parseObj2Json(collentBean1.list);
                List<CollentBean2> collentBean = GsonJsonUtils.parseJson2Obj(string, new TypeToken<List<CollentBean2>>() {
                });
                for (CollentBean2 p : collentBean) {
                    p.song.sid = p.id;
                    mCollentBean3s.add(p.song);
                }

                mHandler.sendEmptyMessage(RankingSearch2Success);
            } else if (aJson.code == 500) {
                mHandler.obtainMessage(RankingExpiredToken, aJson.msg).sendToTarget();
            } else {
                mHandler.obtainMessage(RankingSearch2Error, aJson.msg).sendToTarget();
            }
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
            if (mCollentBean3s != null) {
                try {
                    mSP.putInt("play_index", 0);
                    for (MusicPlayBean musicPlayBean : mCollentBean3s) {
                        App.saveData(musicPlayBean, CollectionListActivity.this, TAG, false);
                        sendBroadcast(new Intent(App.PLAY));
                    }
                } catch (Exception e) {
                    Logger.d(TAG, "...收藏列表保存失败:" + e.getMessage());
                }
            }
        }
    }

    /**
     * 一键清理
     */
    private class MyOnClickListenTitle3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private void showPoWindo() {
        final CustomPopuWindw windw = new CustomPopuWindw(mContext);
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

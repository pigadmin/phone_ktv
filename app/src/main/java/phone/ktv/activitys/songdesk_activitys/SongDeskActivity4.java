package phone.ktv.activitys.songdesk_activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.adaters.RinkingListAdater;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.bean.ResultBean;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.LatelyListAddUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.MyListView;

/**
 * (点歌台)通过歌星搜索歌曲的列表  4级
 */
public class SongDeskActivity4 extends phone.ktv.BaseActivity {

    private static final String TAG = "SongDeskActivity4";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private MyListView mListView;
    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private SVProgressHUD mSvProgressHUD;

    private RinkingListAdater mRinkingAdater;

    private List<MusicPlayBean> musicPlayBeans;

    public static final int SongDesk4Success = 100;//排行榜歌曲获取成功
    public static final int SongDesk4Error = 200;//排行榜歌曲获取失败
    public static final int SongDesk4ExpiredToken = 300;//Token过期

    private SPUtil mSP;

    private String mRangId, mRangName;

    private TextView mSongBang;//情歌榜
    private TextView getmSongBangList;//情歌多少首

    private TextView mQuanbuPlay;//全部播放

    private int mLimit = App.Maxlimit;//页码量
    private int mPage = 1;//第几页

    public int totalCount = 0;//歌曲总数量

    private TextView mNoData;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SongDesk4Success://获取成功
                    mRinkingAdater.notifyDataSetChanged();
                    mSongBang.setText(mRangName);
                    getmSongBangList.setText("/" + totalCount);
                    mTopTitleView1.setTopText(mRangName);
                    updateData();
                    break;

                case SongDesk4Error://获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case SongDesk4ExpiredToken://Token过期
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
        setContentView(R.layout.ranking_list_activity);

        getIntentData();
        initView();
        initLiter();
        settingPullRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Bundle传值
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mRangId = intent.getStringExtra("id");
            mRangName = intent.getStringExtra("name");
            Logger.i(TAG, "id..." + mRangId + "..name..." + mRangName);
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

    private void updateData() {
        if (musicPlayBeans != null && !musicPlayBeans.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        musicPlayBeans = new ArrayList<>();

        mContext = SongDeskActivity4.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw99);

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);
        mSongBang = findViewById(R.id.song_song110_tvw);
        getmSongBangList = findViewById(R.id.song1_song111_tvw);
        mQuanbuPlay = findViewById(R.id.quanbu_llt1);
        mPullToRefresh = findViewById(R.id.sv);

        mListView = findViewById(R.id.list_view_2);
        mRinkingAdater = new RinkingListAdater(mContext, R.layout.item_ringlist_layout, musicPlayBeans, mSP, SongDeskActivity4.this);
        mListView.setAdapter(mRinkingAdater);

        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        getRankingListData();
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mQuanbuPlay.setOnClickListener(new MyQuanbuPlayOnClick());
        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
        mListView.setOnItemClickListener(new MyOnItemClickLis());
    }

    private class MyOnItemClickLis implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MusicPlayBean bean = musicPlayBeans.get(position);
            LatelyListAddUtils utils = new LatelyListAddUtils(mSP, SongDeskActivity4.this, bean);
            utils.getLatelyList();
            System.out.println("############################");
            toplay(bean);
        }
    }

    private void toplay(MusicPlayBean bean) {
        mSP.putInt("play_index", 0);
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
            musicPlayBeans.clear();
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
     * 全部播放
     */
    private class MyQuanbuPlayOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (musicPlayBeans != null) {
                try {
                    mSP.putInt("play_index", 0);
                    for (MusicPlayBean musicPlayBean : musicPlayBeans) {
                        App.saveData(musicPlayBean, SongDeskActivity4.this, TAG, false);
                        sendBroadcast(new Intent(App.PLAY));
                    }
                    sendBroadcast(new Intent(App.SWITCHPLAY));
                } catch (Exception e) {
                    Logger.d(TAG, "...点歌台保存失败:" + e.getMessage());
                }
            }
        }
    }

    /**
     * 排行榜获取歌曲
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
        weakHashMap.put("singerId", mRangId);//歌手id

        String url = App.getRqstUrl(App.headurl + "song", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(SongDesk4Error, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    ResultBean aJson = GsonJsonUtils.parseJson2Obj(s, ResultBean.class);
                    if (aJson != null) {
                        if (aJson.code == 0) {
                            Logger.i(TAG, "aJson1..." + aJson.toString());
                            setState(aJson.data.list);
                            totalCount = aJson.data.totalCount;
                            mHandler.sendEmptyMessage(SongDesk4Success);
                        } else if (aJson.code == 500) {
                            mHandler.obtainMessage(SongDesk4ExpiredToken, aJson.msg).sendToTarget();
                        } else {
                            mHandler.obtainMessage(SongDesk4Error, aJson.msg).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mPullToRefresh.onRefreshComplete();
            mSvProgressHUD.dismiss();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void setState(List<MusicPlayBean> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            musicPlayBeans.addAll(itemList);
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

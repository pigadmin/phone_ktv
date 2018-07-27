package phone.ktv.activitys.already_activitys;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;
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
import phone.ktv.adaters.AlreadySearchAdater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.bean.ResultBean;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ScreenUtils;
import phone.ktv.tootls.SoftKeyboard;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.MyListView;

/**
 * 搜索1级  (搜索歌曲,歌星)
 */
public class AlreadySearchListActivity extends AppCompatActivity {

    private static final String TAG = "AlreadySearchListActivity";

    Context mContext;

    private ImageView mSrcBack11;//返回
    private ImageView mVoice;//语音功能
    private TextView mSongType;//歌名/歌星
    private EditText mSearchContent;//搜索内容
    private TextView mSearch;//搜索按钮

    private MyListView mListView1;
    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private AlreadySearchAdater mAlreadyAdater;

    private List<MusicPlayBean> musicPlayBeans;
    private List<SingerNumBean.SingerBean> mnumBeanList;

    public static final int RankingSearchSuccess = 100;//搜索歌曲获取成功
    public static final int RankingSearchError = 200;//搜索歌曲获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;
    private SPUtil mSP;

    private TextView mNoData;

    private int mLimit = App.Maxlimit;//页码量
    private int mPage = 1;//第几页

    private boolean isState = true;//标识歌名,歌手

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingSearchSuccess://搜索成功
                    if (isState) {
                        updateData1();
                    } else {
                        updateData2();
                    }
                    break;

                case RankingSearchError://搜索失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case RankingExpiredToken://Token过期
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
        setContentView(R.layout.already_search_layout);

        initView();
        initLiter();
        settingPullRefresh();
        getSongNameData();
    }

    private void initView() {
        musicPlayBeans = new ArrayList<>();
        mnumBeanList = new ArrayList<>();

        mContext = AlreadySearchListActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw);
        mSrcBack11 = findViewById(R.id.src_back11_ivw);
        mSongType = findViewById(R.id.songType_tvw11);
        mVoice = findViewById(R.id.voice12_ivw);
        mSearchContent = findViewById(R.id.search_content_edt);
        mSearch = findViewById(R.id.text_search11_tvw);
        mPullToRefresh = findViewById(R.id.sv);

        mListView1 = findViewById(R.id.list112_view);

        mAlreadyAdater = new AlreadySearchAdater(mContext, musicPlayBeans, mnumBeanList, isState);
        mListView1.setAdapter(mAlreadyAdater);
    }

    private void initLiter() {
        mSrcBack11.setOnClickListener(new MyOnClickBackReturn());//返回事件
        mSongType.setOnClickListener(new MyOnClickSongType());
        mVoice.setOnClickListener(new MyOnClickListenerVoice());
        mSearch.setOnClickListener(new MyOnClickListenerSearch());
        mListView1.setOnItemClickListener(new MyOnItemClickListener1());
        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
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
            musicPlayBeans.clear();
            mnumBeanList.clear();
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

    /**
     * 搜索事件
     */
    private class MyOnClickListenerSearch implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SoftKeyboard.closeKeybord(mSearchContent, mContext);
            mPage = 1;
            musicPlayBeans.clear();
            mnumBeanList.clear();
            getSongNameData();
        }
    }

    private void updateData1() {
        mAlreadyAdater.notifyDataSetChanged();
        if (musicPlayBeans != null && !musicPlayBeans.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    private void updateData2() {
        mAlreadyAdater.notifyDataSetChanged();
        if (mnumBeanList != null && !mnumBeanList.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }

    }

    /**
     * item事件
     */
    private class MyOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isState) {

            } else {
                SingerNumBean.SingerBean bean = mnumBeanList.get(position);
                IntentUtils.strIntentString(mContext, AlreadySearchListActivity2.class, "id", "name", bean.id, bean.name);
            }
        }
    }

    /**
     * 语音
     */
    private class MyOnClickListenerVoice implements View.OnClickListener {
        @Override
        public void onClick(View v) {

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

    /**
     * 显示选择框
     */
    public class MyOnClickSongType implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showPoWindo();
        }
    }

    private void showPoWindo() {
        int height = ScreenUtils.getScreenHeight(mContext);
        View strView = getLayoutInflater().inflate(R.layout.pow_already_btn, null, false);
        final PopupWindow window = new PopupWindow(strView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setAnimationStyle(R.style.AnimationFade);
        window.setBackgroundDrawable(new BitmapDrawable());// 需要设置一下此参数，点击外边可消失
        window.setFocusable(true);// 设置此参数获得焦点，否则无法点击
        window.setOutsideTouchable(false);//设置点击窗口外边窗口消失
        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, -height / 10);

        final TextView song1 = strView.findViewById(R.id.song_btn_1);
        final TextView song2 = strView.findViewById(R.id.song_btn_2);
        song1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongType.setText(song1.getText().toString().trim());
                isState = true;
                mAlreadyAdater.updaState(isState);
                window.dismiss();
            }
        });

        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongType.setText(song2.getText().toString().trim());
                isState = false;
                mAlreadyAdater.updaState(isState);
                window.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 歌名搜索
     */
    private void getSongNameData() {
        mSvProgressHUD.showWithStatus("正在搜索中,请稍后...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("name", "");//名称  （语音输入时直接按名称搜)
        weakHashMap.put("page", mPage + "");//第几页    不填默认1
        weakHashMap.put("limit", mLimit + "");//页码量   不填默认10，最大限度100
        weakHashMap.put("keyword", mSearchContent.getText().toString().trim());//搜索关键字

        String url;
        if (isState) {
            weakHashMap.put("singerId", null);//歌手id
            url = App.getRqstUrl(App.headurl + "song", weakHashMap);
        } else {
            weakHashMap.put("singertypeid", null);//歌手类型id
            url = App.getRqstUrl(App.headurl + "song/singer", weakHashMap);
        }

        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(RankingSearchError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    if (isState) {
                        ResultBean aJson = GsonJsonUtils.parseJson2Obj(s, ResultBean.class);
                        if (aJson != null) {
                            if (aJson.code == 0) {
                                mHandler.sendEmptyMessage(RankingSearchSuccess);
                                Logger.i(TAG, "aJson1..." + aJson.toString());
                                setStateSongName(aJson.data.list);
                            } else if (aJson.code == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.msg).sendToTarget();
                            } else {
                                mHandler.obtainMessage(RankingSearchError, aJson.msg).sendToTarget();
                            }
                        }
                    } else {
                        AJson<List<SingerNumBean.SingerBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<SingerNumBean.SingerBean>>>() {
                        });
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                mHandler.sendEmptyMessage(RankingSearchSuccess);
                                Logger.i(TAG, "aJson2..." + aJson.toString());
                                setStateSong(aJson.getData());
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(RankingSearchError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            mPullToRefresh.onRefreshComplete();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void setStateSongName(List<MusicPlayBean> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            musicPlayBeans.addAll(itemList);
        }
    }

    private void setStateSong(List<SingerNumBean.SingerBean> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            mnumBeanList.addAll(itemList);
        }
    }
}

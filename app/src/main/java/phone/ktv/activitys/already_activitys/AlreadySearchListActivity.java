package phone.ktv.activitys.already_activitys;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import phone.ktv.BaseActivity;
import phone.ktv.R;
import phone.ktv.adaters.AlreadySearchSingAdater;
import phone.ktv.adaters.RinkingListAdater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.bean.ResultBean;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ScreenUtils;
import phone.ktv.tootls.SoftKeyboard;
import phone.ktv.tootls.SpeechRecognitionUtils;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.MyListView;

/**
 * 搜索1级  (搜索歌曲,歌星)
 */
public class AlreadySearchListActivity extends BaseActivity {

    private static final String TAG = "AlreadySearchListActivity";

    Context mContext;

    private ImageView mVoice;//语音功能
    private TextView mSongType;//歌名/歌星
    private EditText mSearchContent;//搜索内容
    private TextView mSearch;//搜索按钮
    private ImageView mDelete;//删除

    private MyListView mListView1;
    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private MyListView mListView2;
    private PullToRefreshScrollView mPullToRefresh2;
    private ILoadingLayout mLoadingLayoutProxy2;

    private RinkingListAdater mAlreadyAdater;
    private AlreadySearchSingAdater mAlreadyAdaterTwo;

    private List<MusicPlayBean> musicPlayBeans;
    private List<SingerNumBean.SingerBean> mnumBeanList;

    public static final int SongSearchSuccess = 100;//搜索歌曲获取成功
    public static final int SongSearchError = 200;//搜索歌曲获取失败
    public static final int RankingExpiredToken = 300;//Token过期
    public static final int SingSearchSuccess = 400;//搜索歌星获取成功
    public static final int SingSearchError = 500;//搜索歌星获取失败

    private SVProgressHUD mSvProgressHUD;
    private SPUtil mSP;

    private TextView mNoData;

    private int mLimit = App.Maxlimit;//页码量
    private int mPage = 1;//第几页

    private boolean isState = true;//标识歌名,歌手

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SongSearchSuccess://搜索歌曲获取成功
                    updateDataSong();
                    break;
                case SongSearchError://搜索歌曲获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
                case RankingExpiredToken://Token过期
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
                case SingSearchSuccess://搜索歌星获取成功
                    updateDataSing();
                    break;
                case SingSearchError://搜索歌星获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
            }
            clearSvpHub();
            clearRefreshComp();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_search_layout);

        initView();
        initLiter();
        settingPullRefresh();
        settingPullRefresh2();
        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        getSongerData();
    }

    private void initView() {
        musicPlayBeans = new ArrayList<>();
        mnumBeanList = new ArrayList<>();

        mContext = AlreadySearchListActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw);
        mSongType = findViewById(R.id.songType_tvw11);
        mDelete = findViewById(R.id.delete_ivw_12);
        mVoice = findViewById(R.id.voice12_ivw);
        mSearchContent = findViewById(R.id.search_content_edt);
        mSearch = findViewById(R.id.text_search11_tvw);


        mPullToRefresh = findViewById(R.id.sv);
        mListView1 = findViewById(R.id.list112_view);
        mAlreadyAdater = new RinkingListAdater(mContext, R.layout.item_ringlist_layout, musicPlayBeans, mSP, AlreadySearchListActivity.this);
        mListView1.setAdapter(mAlreadyAdater);

        mPullToRefresh2 = findViewById(R.id.sv_2);
        mListView2 = findViewById(R.id.list112_view_2);
        mAlreadyAdaterTwo = new AlreadySearchSingAdater(mContext, R.layout.singer_play_item, mnumBeanList, mSP, AlreadySearchListActivity.this);
        mListView2.setAdapter(mAlreadyAdaterTwo);
    }

    private void initLiter() {
        mSongType.setOnClickListener(new MyOnClickSongType());
        mVoice.setOnClickListener(new MyOnClickListenerVoice());
        mSearch.setOnClickListener(new MyOnClickListenerSearch());
        mListView1.setOnItemClickListener(new MyOnItemClickListener1());
        mListView2.setOnItemClickListener(new MyOnItemClickListener2());

        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
        mPullToRefresh2.setOnRefreshListener(new MyPullToRefresh2());

        mSearchContent.addTextChangedListener(new MyAddTextChanged());
        mDelete.setOnClickListener(new MyOnClickDelete());
        mDelete.setVisibility(View.INVISIBLE);
    }

    private void clearRefreshComp() {
        if (isState) {
            if (mPullToRefresh.isShown())
                mPullToRefresh.onRefreshComplete();
        } else {
            if (mPullToRefresh2.isShown())
                mPullToRefresh2.onRefreshComplete();
        }
    }

    private void clearSvpHub() {
        if (mSvProgressHUD.isShowing())
            mSvProgressHUD.dismiss();
    }

    private class MyAddTextChanged implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDelete.setVisibility(TextUtils.isEmpty(mSearchContent.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 清空文本
     */
    private class MyOnClickDelete implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mSearchContent.setText(null);
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
     * PullToRefreshScrollView 属性
     */
    private void settingPullRefresh2() {
        mPullToRefresh2.setMode(PullToRefreshBase.Mode.BOTH);
        mLoadingLayoutProxy2 = mPullToRefresh2.getLoadingLayoutProxy(true, false);
        mLoadingLayoutProxy2.setPullLabel("下拉刷新");
        mLoadingLayoutProxy2.setRefreshingLabel("正在刷新");
        mLoadingLayoutProxy2.setReleaseLabel("松开刷新");

        ILoadingLayout endLoading = mPullToRefresh2.getLoadingLayoutProxy(false, true);
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
            getSongerData();
        }

        /**
         * 上拉加载
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage++;
            getSongerData();
        }
    }

    /**
     * 下拉刷新
     */
    private class MyPullToRefresh2 implements PullToRefreshBase.OnRefreshListener2 {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy2.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage = 1;
            mnumBeanList.clear();
            getSingerData();
        }

        /**
         * 上拉加载
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy2.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage++;
            getSingerData();
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
            if (isState) {
                mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
                getSongerData();
            } else {
                mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
                getSingerData();
            }
        }
    }

    private void updateDataSong() {
        mListView1.setVisibility(View.VISIBLE);
        mPullToRefresh.setVisibility(View.VISIBLE);
        mListView2.setVisibility(View.GONE);
        mPullToRefresh2.setVisibility(View.GONE);
        mAlreadyAdater.notifyDataSetChanged();
        if (musicPlayBeans != null && !musicPlayBeans.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    private void updateDataSing() {
        mListView1.setVisibility(View.GONE);
        mPullToRefresh.setVisibility(View.GONE);
        mListView2.setVisibility(View.VISIBLE);
        mPullToRefresh2.setVisibility(View.VISIBLE);
        mAlreadyAdaterTwo.notifyDataSetChanged();
        if (mnumBeanList != null && !mnumBeanList.isEmpty()) {
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * item1事件
     */
    private class MyOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    /**
     * item2事件
     */
    private class MyOnItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SingerNumBean.SingerBean bean = mnumBeanList.get(position);
            IntentUtils.strIntentString(mContext, AlreadySearchListActivity2.class, "id", "name", bean.id, bean.name);
        }
    }


    /**
     * 语音
     */
    private class MyOnClickListenerVoice implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SpeechRecognitionUtils utils = new SpeechRecognitionUtils(mContext, mSearchContent);
            utils.startDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SpeechRecognizer.getRecognizer().destroy();
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
                window.dismiss();
            }
        });

        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongType.setText(song2.getText().toString().trim());
                isState = false;
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
     * 获取歌曲
     */
    private void getSongerData() {
        if (NetUtils.hasNetwork(mContext)) {
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
            weakHashMap.put("singerId", null);//歌手id
            String url = App.getRqstUrl(App.headurl + "song", weakHashMap);

            Logger.i(TAG, "url.." + url);
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(SongSearchError, msg).sendToTarget();
                    } else {
                        ResultBean aJson = GsonJsonUtils.parseJson2Obj(result, ResultBean.class);
                        if (aJson != null) {
                            if (aJson.code == 0) {
                                Logger.i(TAG, "aJson1..." + aJson.toString());
                                if (aJson.data.list != null && !aJson.data.list.isEmpty()) {
                                    musicPlayBeans.addAll(aJson.data.list);
                                }
                                mHandler.sendEmptyMessage(SongSearchSuccess);
                            } else if (aJson.code == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.msg).sendToTarget();
                            } else {
                                mHandler.obtainMessage(SongSearchError, aJson.msg).sendToTarget();
                            }
                        }
                    }
                }
            });
        } else {
            clearSvpHub();
            clearRefreshComp();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 获取歌星
     */
    private void getSingerData() {
        if (NetUtils.hasNetwork(mContext)) {
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
            weakHashMap.put("singertypeid", null);//歌手类型id
            String url = App.getRqstUrl(App.headurl + "song/singer", weakHashMap);

            Logger.i(TAG, "url.." + url);
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        mHandler.obtainMessage(SingSearchError, msg).sendToTarget();
                    } else {
                        AJson<List<SingerNumBean.SingerBean>> aJson = App.jsonToObject(result, new TypeToken<AJson<List<SingerNumBean.SingerBean>>>() {
                        });
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                Logger.i(TAG, "aJson2..." + aJson.toString());
                                if (aJson.getData() != null && !aJson.getData().isEmpty()) {
                                    mnumBeanList.addAll(aJson.getData());
                                }
                                mHandler.sendEmptyMessage(SingSearchSuccess);
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(RankingExpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(SingSearchError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }
                }
            });
        } else {
            clearSvpHub();
            clearRefreshComp();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }
}

package phone.ktv.activitys.already_activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.SoftKeyboard;
import phone.ktv.tootls.SpeechRecognitionUtils;
import phone.ktv.tootls.TimeUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.MyListView;

/**
 * 搜索2级 (搜索歌曲)
 */
public class AlreadySearchListActivity2 extends AppCompatActivity {

    private static final String TAG = "AlreadySearchListActivity2";

    Context mContext;

    private ImageView mSrcBack11;//返回
    private ImageView mVoice;//语音功能
    private TextView mSongType;//歌名/歌星
    private EditText mSearchContent;//搜索内容
    private TextView mSearch;//搜索按钮
    private ImageView mDelete;//删除

    private MyListView mListView1;
    private PullToRefreshScrollView mPullToRefresh;
    private ILoadingLayout mLoadingLayoutProxy;

    private RinkingListAdater mRinkingAdater;

    private List<MusicPlayBean> musicPlayBeans;

    public static final int RankingSearch2Success = 100;//搜索歌曲获取成功
    public static final int RankingSearch2Error = 200;//搜索歌曲获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;
    private SPUtil mSP;

    private int mLimit = App.Maxlimit;//页码量
    private int mPage = 1;//第几页

    private TextView mNoData;

    private String mId, mName;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingSearch2Success://搜索成功
                    mRinkingAdater.notifyDataSetChanged();
                    updateData1();
                    break;

                case RankingSearch2Error://搜索失败
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
        setContentView(R.layout.already_search_layout2);

        getIntentData();
        initView();
        initLiter();
        settingPullRefresh();
    }

    /**
     * Bundle传值
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getStringExtra("id");
            mName = intent.getStringExtra("name");
            Logger.i(TAG, "mId..." + mId + "..mName..." + mName);
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

    private void initView() {
        musicPlayBeans = new ArrayList<>();

        mContext = AlreadySearchListActivity2.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw1);
        mSrcBack11 = findViewById(R.id.src_back11_ivw);
        mSongType = findViewById(R.id.songType_tvw11);
        mSongType.setVisibility(View.GONE);
        mDelete = findViewById(R.id.delete_ivw_12);
        mPullToRefresh = findViewById(R.id.sv);
        mVoice = findViewById(R.id.voice12_ivw);
        mSearchContent = findViewById(R.id.search_content_edt);
        mSearch = findViewById(R.id.text_search11_tvw);

        mListView1 = findViewById(R.id.list1122_view);
        mRinkingAdater = new RinkingListAdater(mContext, R.layout.item_ringlist_layout, musicPlayBeans, mSP, AlreadySearchListActivity2.this);
        mListView1.setAdapter(mRinkingAdater);

        getSongNameData();
    }

    private void initLiter() {
        mSrcBack11.setOnClickListener(new MyOnClickBackReturn());//返回事件
        mVoice.setOnClickListener(new MyOnClickListenerVoice());
        mSearch.setOnClickListener(new MyOnClickListenerSearch());
        mListView1.setOnItemClickListener(new MyOnItemClickListener1());
        mPullToRefresh.setOnRefreshListener(new MyPullToRefresh());
        mSearchContent.addTextChangedListener(new MyAddTextChanged());
        mDelete.setOnClickListener(new MyOnClickDelete());
        mDelete.setVisibility(View.INVISIBLE);
        mListView1.setOnItemClickListener(new MyOnItemClickLis());
    }

    private class MyOnItemClickLis implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
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
     * 下拉刷新
     */
    private class MyPullToRefresh implements PullToRefreshBase.OnRefreshListener2 {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
            mLoadingLayoutProxy.setLastUpdatedLabel(TimeUtils.getLocalDateTime());
            mPage = 1;
            musicPlayBeans.clear();
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
            getSongNameData();
        }
    }

    private void updateData1() {
        if (musicPlayBeans != null && !musicPlayBeans.isEmpty()) {
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
            ToastUtils.showLongToast(mContext, "1");
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
        weakHashMap.put("singerId", mId);//歌手id
        weakHashMap.put("keyword", mSearchContent.getText().toString().trim());//搜索关键字

        String url = App.getRqstUrl(App.headurl + "song", weakHashMap);

        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(RankingSearch2Error, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    ResultBean aJson = GsonJsonUtils.parseJson2Obj(s, ResultBean.class);
                    if (aJson != null) {
                        if (aJson.code == 0) {
                            mHandler.sendEmptyMessage(RankingSearch2Success);
                            Logger.i(TAG, "aJson1..." + aJson.toString());
                            setStateSongName(aJson.data.list);
                        } else if (aJson.code == 500) {
                            mHandler.obtainMessage(RankingExpiredToken, aJson.msg).sendToTarget();
                        } else {
                            mHandler.obtainMessage(RankingSearch2Error, aJson.msg).sendToTarget();
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
}

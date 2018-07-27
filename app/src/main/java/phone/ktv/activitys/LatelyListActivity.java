package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.adaters.CollectionListAdater;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.bean.ResultBean;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.MyListView;

/**
 * 最近播放列表
 */
public class LatelyListActivity extends AppCompatActivity {

    private static final String TAG = "LatelyListActivity";

    Context mContext;

    private MyListView mListView1;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CollectionListAdater mCollectionAdater;

    private List<MusicPlayBean> musicPlayBeans;

    public static final int RankingSearch2Success = 100;//搜索歌曲获取成功
    public static final int RankingSearch2Error = 200;//搜索歌曲获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;
    private SPUtil mSP;

    private TextView mNoData;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingSearch2Success://搜索成功
                    mCollectionAdater.notifyDataSetChanged();
                    updateData();
                    break;

                case RankingSearch2Error://搜索失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case RankingExpiredToken://Token过期
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
            }
            mSvProgressHUD.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lately_list_activity);

        initView();
        initLiter();
    }

    private void initView() {
        musicPlayBeans = new ArrayList<>();

        mContext = LatelyListActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mNoData = findViewById(R.id.no_data_tvw16);
        mTopTitleView1 = findViewById(R.id.customTopTitleView1);


        mListView1 = findViewById(R.id.list_view_22);
        mCollectionAdater = new CollectionListAdater(mContext, R.layout.item_collection_list_layout, musicPlayBeans);
        mListView1.setAdapter(mCollectionAdater);

        getSongNameData();
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mListView1.setOnItemClickListener(new MyOnItemClickListener1());
    }

    private void updateData() {
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
     * 获取播放记录列表
     */
    private void getSongNameData() {
        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel+"..token.."+token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "song/record", weakHashMap);

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
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void setStateSongName(List<MusicPlayBean> itemList) {
        if (itemList != null && !itemList.isEmpty()) {
            musicPlayBeans.addAll(itemList);
        }
    }
}
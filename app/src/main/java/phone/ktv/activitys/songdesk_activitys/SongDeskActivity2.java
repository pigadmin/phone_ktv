package phone.ktv.activitys.songdesk_activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.adaters.SongDeskGrid2Adater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;

/**
 * 歌星分类(点歌台) (大陆男歌星,内地女歌星,港台女歌星,国语合唱)  2级
 */
public class SongDeskActivity2 extends AppCompatActivity{

    private static final String TAG = "SongDeskActivity2";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private ListView mListView;

    private SongDeskGrid2Adater mRinkingAdater;

    private List<SingerNumBean.SingerBean> mSingerNumBeans;

    public static final int RankingListSuccess=100;//排行榜歌曲获取成功
    public static final int RankingListError=200;//排行榜歌曲获取失败
    public static final int RankingExpiredToken=300;//Token过期

    private SVProgressHUD mSvProgressHUD;

    private SPUtil mSP;

    private String mRangId,mRangName;

    private TextView mSongBang;//情歌榜
    private TextView getmSongBangList;//情歌多少首

    private LinearLayout mQuanbuPlay;//全部播放

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingListSuccess://获取成功
                    mSvProgressHUD.dismiss();
                    mRinkingAdater.notifyDataSetChanged();
                    mSongBang.setText(mRangName);
                    getmSongBangList.setText("/"+mSingerNumBeans.size());
                    break;

                case RankingListError://获取失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;

                case RankingExpiredToken://Token过期
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_list_activity);

        initView();
        initLiter();
        getIntentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRankingListData();
    }

    /**
     * Bundle传值
     */
    private void getIntentData(){
        Intent intent=getIntent();
        if (intent!=null){
          mRangId= intent.getStringExtra("rangId");
          mRangName= intent.getStringExtra("rangName");
          Logger.i(TAG,"mRangId..."+mRangId+"..mRangName..."+mRangName);
        }
    }

    private void initView(){
        mSingerNumBeans=new ArrayList<>();

        mContext= SongDeskActivity2.this;
        mSvProgressHUD=new SVProgressHUD(mContext);
        mSP=new SPUtil(mContext);

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        mSongBang=findViewById(R.id.song_song110_tvw);
        getmSongBangList=findViewById(R.id.song1_song111_tvw);
        mQuanbuPlay=findViewById(R.id.quanbu_llt1);

        mListView=findViewById(R.id.list_view_2);
        mRinkingAdater=new SongDeskGrid2Adater(mContext,R.layout.item_gridicon_image,mSingerNumBeans);
        mListView.setAdapter(mRinkingAdater);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mQuanbuPlay.setOnClickListener(new MyQuanbuPlayOnClick());
    }

    /**
     * 全部播放
     */
    private class MyQuanbuPlayOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 排行榜获取歌曲
     */
    private void getRankingListData(){
        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel= mSP.getString("telPhone",null);//tel
        String token= mSP.getString("token",null);//token
        Logger.i(TAG,"tel.."+tel+"..token.."+token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("page",1+"");//第几页    不填默认1
        weakHashMap.put("limit",10+"");//页码量   不填默认10，最大限度100
        weakHashMap.put("rangId",mRangId);//歌手id

        String url = App.getRqstUrl(App.headurl + "song/getsongSingerType", weakHashMap);
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
                    Logger.i(TAG,"s.."+s);

                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson!=null){
                        if (aJson.getCode()==0){
                            SingerNumBean numBean = App.jsonToObject(s, new TypeToken<AJson<SingerNumBean>>() {}).getData();
                            mHandler.sendEmptyMessage(RankingListSuccess);
                            Logger.i(TAG,"aJson1..."+aJson.toString());
                            setState(numBean.list);
                        } else if (aJson.getCode()==500){
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
            mSvProgressHUD.dismiss();
            ToastUtils.showLongToast(mContext,"网络连接异常,请检查网络配置");
        }
    }

    private void setState(List<SingerNumBean.SingerBean> itemList){
        mSingerNumBeans.clear();
        if (itemList!=null&&!itemList.isEmpty()){
            mSingerNumBeans.addAll(itemList);
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener{
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

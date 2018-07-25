package phone.ktv.activitys.songdesk_activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;

/**
 * 歌星列表(点歌台) (薛之谦,许嵩,刘德华)  3级
 */
public class SongDeskActivity3 extends AppCompatActivity{

    private static final String TAG = "SongDeskActivity3";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private GridView mGridView;

    private SongDeskGrid2Adater mRinkingAdater;

    private List<SingerNumBean.SingerBean> mSingerNumBeans;

    public static final int SongDesk3Success=100;//获取成功
    public static final int SongDesk3Error=200;//获取失败
    public static final int SongDeskExpiredToken=300;//Token过期

    private SVProgressHUD mSvProgressHUD;

    private SPUtil mSP;

    private String mRangId,mRangName;

    private TextView mNoData;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SongDesk3Success://获取成功
                    mSvProgressHUD.dismiss();
                    mRinkingAdater.notifyDataSetChanged();
                    mTopTitleView1.setTopText(mRangName);
                    updateData();
                    break;

                case SongDesk3Error://获取失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;

                case SongDeskExpiredToken://Token过期
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songdesk_more_activity);

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
            mRangId= intent.getStringExtra("id");
            mRangName= intent.getStringExtra("name");
            Logger.i(TAG,"id..."+mRangId+"..name..."+mRangName);
        }
    }

    private void updateData(){
        if (mSingerNumBeans!=null&&!mSingerNumBeans.isEmpty()){
            mNoData.setVisibility(View.GONE);
        } else {
            mNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initView(){
        mSingerNumBeans=new ArrayList<>();

        mContext= SongDeskActivity3.this;
        mSvProgressHUD=new SVProgressHUD(mContext);
        mSP=new SPUtil(mContext);

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        mNoData=findViewById(R.id.no_data_tvw123);

        mGridView=findViewById(R.id.grid_view_8);
        mRinkingAdater=new SongDeskGrid2Adater(mContext,R.layout.item_gridicon_image,mSingerNumBeans);
        mGridView.setAdapter(mRinkingAdater);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SingerNumBean.SingerBean item= mSingerNumBeans.get(position);
            if (item!=null){
                IntentUtils.strIntentString(mContext, SongDeskActivity4.class,"id","name",item.id,item.name);
            }
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
        weakHashMap.put("singertypeid",mRangId);//歌手id

        String url = App.getRqstUrl(App.headurl + "song/singer", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(SongDesk3Error, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG,"s.."+s);

                    AJson<List<SingerNumBean.SingerBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<SingerNumBean.SingerBean>>>() {});
                    if (aJson!=null){
                        if (aJson.getCode()==0){
                            mHandler.sendEmptyMessage(SongDesk3Success);
                            Logger.i(TAG,"aJson1..."+aJson.toString());
                            setState(aJson.getData());
                        } else if (aJson.getCode()==500){
                            mHandler.obtainMessage(SongDeskExpiredToken, aJson.getMsg()).sendToTarget();
                        } else {
                            mHandler.obtainMessage(SongDesk3Error, aJson.getMsg()).sendToTarget();
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

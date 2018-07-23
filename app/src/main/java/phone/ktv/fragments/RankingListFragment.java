package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.adaters.Rinking1FragmentAdater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridItem;
import phone.ktv.bgabanner.BGABanner;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 *  排行榜
 */
public class RankingListFragment extends Fragment {

    private static final String TAG = "RankingListFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    private Rinking1FragmentAdater mDeskAdater;

    private GridView mGridView;

    private SPUtil mSP;

    private List<GridItem> mGridItemList;

    private TextView mNoData;

    public static final int RankingListSuccess=100;//排行榜分类获取成功
    public static final int RankingListError=200;//排行榜分类获取失败
    public static final int RankingExpiredToken=300;//Token过期

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingListSuccess://提交成功
//                    mSvProgressHUD.dismiss();
                    break;

                case RankingListError://提交失败
//                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;

                case RankingExpiredToken://Token过期
//                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.ranking_fragment_layout, null);

        mContext=getActivity();
        mSP=new SPUtil(mContext);

        initView();
        initLiter();
        return mNewsView;
    }

    private void initView(){
        mGridItemList=new ArrayList<>();

        mNoData = mNewsView.findViewById(R.id.noData_tvw);
        mNoData.setVisibility(View.GONE);

        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);

        mGridView=mNewsView.findViewById(R.id.grid_view_1);
        mDeskAdater=new Rinking1FragmentAdater(mContext,R.layout.grids,mGridItemList);
        mGridView.setAdapter(mDeskAdater);
    }

    private void initLiter(){
        List<Integer> int1=new ArrayList<>();
        int1.add(R.mipmap.lu_1);
        int1.add(R.mipmap.lu_2);
        int1.add(R.mipmap.lu_3);

        List<String> int2=new ArrayList<>();
        int2.add("图1");
        int2.add("图2");
        int2.add("图3");

        mBanner.setData(int1, int2);
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        getRankingListData();
    }

    private void setState(List<GridItem> itemList){
        mGridItemList.clear();
        if (itemList!=null&&!itemList.isEmpty()){
            mGridItemList.addAll(itemList);
        }
        if (mGridItemList!=null&&!mGridItemList.isEmpty()){

        } else {

        }

    }

    /**
     * 获取排行榜分类
     */
    private void getRankingListData(){
//        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel= mSP.getString("telPhone",null);//tel
        String token= mSP.getString("token",null);//token
        Logger.i(TAG,"tel.."+tel+"..token.."+token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "song/rangking", weakHashMap);
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
                    AJson<List<GridItem>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<GridItem>>>() {});
                    if (aJson!=null){
                        if (aJson.getCode()==0){
                            mHandler.sendEmptyMessage(RankingListSuccess);
                            Logger.i(TAG,"aJson..."+aJson.toString());
                            setState(aJson.getData());
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
//            mSvProgressHUD.dismiss();
            mGridView.setVisibility(View.GONE);
            mNoData.setText("网络连接异常,请检查网络配置");
        }
    }
}


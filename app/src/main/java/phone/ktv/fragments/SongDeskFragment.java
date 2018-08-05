package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
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
import phone.ktv.activitys.songdesk_activitys.SongDeskActivity2;
import phone.ktv.activitys.songdesk_activitys.SongDeskjMoreActivity;
import phone.ktv.adaters.SongDeskGrid1Adater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridList;
import phone.ktv.bean.ListInfo;
import phone.ktv.bgabanner.BGABanner;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.LoginRequestUtils;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 点歌台(歌曲大类) 1级
 */
public class SongDeskFragment extends Fragment {

    private static final String TAG = "SongDeskFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    private SongDeskGrid1Adater mDeskAdater;

    private GridView mGridView;

    private SPUtil mSP;

    private List<ListInfo> mGridItemList;

    public static final int RankingListSuccess = 100;//点歌台分类获取成功
    public static final int RankingListError = 200;//点歌台分类获取失败
    public static final int RankingExpiredToken = 300;//Token过期

    private SVProgressHUD mSvProgressHUD;

    private TextView mMore;//更多

    private TextView mErrorRetry;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RankingListSuccess://获取成功
                    updateData();
                    break;

                case RankingListError://获取失败
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    updateData();
                    break;

                case RankingExpiredToken://Token过期
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
//                    LoginRequestUtils requestUtils=new LoginRequestUtils(mSP,mContext);
//                    requestUtils.requestLoginData();
//                    if (requestUtils.getRequestIndex()==10){
//                    Logger.d(TAG,"10..........");
//                        getRankingListData();
//                    } else if (requestUtils.getRequestIndex()==20){
//                        Logger.d(TAG,"20..........");
//                    } else {
//                        Logger.d(TAG,"0..........");
//                    }
                    updateData();
                    break;
            }
            mSvProgressHUD.dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.songdesk_fragment_layout, null);

        mContext = getActivity();
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        initView();
        initLiter();
        getRankingListData();
        return mNewsView;
    }

    private void initView() {
        mGridItemList = new ArrayList<>();

        mMore = mNewsView.findViewById(R.id.more15);

        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);

        mErrorRetry = mNewsView.findViewById(R.id.error_btn_retry_1);

        mGridView = mNewsView.findViewById(R.id.grid_view_1231);
        mDeskAdater = new SongDeskGrid1Adater(mContext, R.layout.item_gridicon_image, mGridItemList);
        mGridView.setAdapter(mDeskAdater);
    }

    private void initLiter() {
        List<Integer> int1 = new ArrayList<>();
        int1.add(R.mipmap.lu_1);
        int1.add(R.mipmap.lu_2);
        int1.add(R.mipmap.lu_3);

        List<String> int2 = new ArrayList<>();
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

        mMore.setOnClickListener(new MyOnClickListenerMore());//更多
        mGridView.setOnItemClickListener(new MyOnItemClickListener());
        mErrorRetry.setOnClickListener(new MyOnClickListenerErrorRetry());
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

    private void updateData() {
        mDeskAdater.notifyDataSetChanged();
        if (mGridItemList != null && !mGridItemList.isEmpty()) {
            mErrorRetry.setVisibility(View.GONE);
        } else {
            mErrorRetry.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 无数据加载
     */
    private class MyOnClickListenerErrorRetry implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getRankingListData();
        }
    }

    private class MyOnClickListenerMore implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IntentUtils.thisToOther(mContext, SongDeskjMoreActivity.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setState(List<ListInfo> itemList) {
        mGridItemList.clear();
        if (itemList != null && !itemList.isEmpty()) {
            if (itemList.size() > 9) {
                for (int i = 0; i < 9; i++) {
                    ListInfo item = itemList.get(i);
                    mGridItemList.add(item);
                }
            } else {
                mGridItemList.addAll(itemList);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 获取点歌台分类
     */
    private void getRankingListData() {
        mSvProgressHUD.showWithStatus("请稍等,数据加载中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url = App.getRqstUrl(App.headurl + "song/getSongType", weakHashMap);
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
                    Logger.i(TAG, "s.." + s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            GridList gridList = App.jsonToObject(s, new TypeToken<AJson<GridList>>() {
                            }).getData();
                            Logger.i(TAG, "aJson1..." + aJson.toString());
                            setState(gridList.getList());
                            mHandler.sendEmptyMessage(RankingListSuccess);
                        } else if (aJson.getCode() == 500) {
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
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }
}

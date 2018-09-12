package phone.ktv.activitys;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.AdverOpenOne;
import phone.ktv.bean.ColleResultBean;
import phone.ktv.bean.CollentBean1;
import phone.ktv.tootls.AlertDialogHelper;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.FULL;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.BtmDialog;

/**
 * 闪屏页面
 */
public class FlashScreenActivity extends Activity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "FlashScreenActivity";

    private TextView mNumtext;
    private String mToken;
    private List<AdverOpenOne.AdverOpenTwo> mOpenTwoList;
    boolean isPlay = true;

    private CountDownTimer timer;
    private SPUtil mSP;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);

        initView();
        submData();
        getCollectList();
        getLatelyList();
    }

    private ImageView ad_image;
    private VideoView ad_video;
    MediaPlayer mediaPlayer;

    private void initView() {
        mOpenTwoList = new ArrayList<>();

        mContext = FlashScreenActivity.this;
        mSP = new SPUtil(mContext);

        mToken = mSP.getString("token", null);//token

        mNumtext = findViewById(R.id.num);
        mNumtext.setOnClickListener(this);

        ad_image = findViewById(R.id.ad_image);
        ad_video = findViewById(R.id.ad_video);
        FULL.star(ad_video);
        ad_video.setOnPreparedListener(this);
        ad_video.setOnErrorListener(this);
        ad_video.setOnCompletionListener(this);

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playmusic();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num:
                toStep();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        exitmusic();
        isPlay = true;
    }

    private void exitmusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void startTime() {
        timer = new CountDownTimer(playTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mNumtext.setText("倒计时(" + millisUntilFinished / 1000 + ")秒");
                if (millisUntilFinished / 1000 <= 1) {
                    mNumtext.setClickable(false);
                }
            }

            @Override
            public void onFinish() {
                mNumtext.setText("倒计时(" + 0 + ")秒");
                toStep();
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toStep();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toStep() {
        if (NetUtils.hasNetwork(mContext)) {
            if (isPlay) {
                IntentUtils.thisToOther(mContext, TextUtils.isEmpty(mToken) ? LoginActivity.class : MainActivity.class);
                finish();
                isPlay = false;
            }
        }
    }

    /**
     * 获取开机动画
     */
    int playTime = 0;
    List<AdverOpenOne> openOne;

    private void submData() {
        String url = App.headurl + "open/ad";
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            mNumtext.setVisibility(View.VISIBLE);
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson<List<AdverOpenOne>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<AdverOpenOne>>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            Logger.i(TAG, "获取开机动画.." + aJson.toString());
                            openOne = aJson.getData();
                            if (openOne != null && !openOne.isEmpty()) {
                                for (AdverOpenOne one : openOne) {
                                    if (one.ad != null) {
                                        mOpenTwoList.add(one.ad);
                                        playTime += one.playTime;
                                    }
                                }
                                handler.sendEmptyMessage(UPDATE_AD);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        playTime *= 1000;
                                        startTime();
                                    }
                                });
                            } else {
                                toFaileData();
                            }
                        } else {
                            toFaileData();
                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            networkException();
            mNumtext.setVisibility(View.INVISIBLE);
        }
    }

    private void toFaileData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                playTime = 5000;
//                startTime();
                toStep();
            }
        });
    }

    private final int UPDATE_AD = 0;
    private int currentad = 0;
    private AdverOpenOne.AdverOpenTwo ad;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case UPDATE_AD:
                        if (currentad < openOne.size()) {
                            ad = mOpenTwoList.get(currentad);
                            switch (ad.type) {
                                case 1:
                                    if (ad_image.isShown()) {
                                        ad_image.setVisibility(View.GONE);
                                    }
                                    if (!ad_video.isShown()) {
                                        ad_video.setVisibility(View.VISIBLE);
                                    }
                                    videourl = ad.path;
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                    }
                                    playvideo();
                                    break;
                                case 2:
                                    if (!ad_image.isShown()) {
                                        ad_image.setVisibility(View.VISIBLE);
                                    }
                                    if (ad_video.isShown()) {
                                        ad_video.setVisibility(View.GONE);
                                    }
                                    videourl = ad.backFile;
                                    Picasso.with(FlashScreenActivity.this).load(ad.path).into(ad_image);
                                    playmusic();
                                    break;
                            }
                            handler.sendEmptyMessageDelayed(UPDATE_AD, openOne.get(currentad).playTime * 1000);
                            currentad++;
                        }
                        break;
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    };
    private String videourl;

    private void playvideo() {
        if (!videourl.equals("")) {
            System.out.println(videourl);
            ad_video.setVideoURI(Uri.parse(videourl));
        }
    }

    private void playmusic() {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(FlashScreenActivity.this,
                    Uri.parse(videourl));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    /**
     * 默认访问收藏列表
     */
    private void getCollectList() {
        if (NetUtils.hasNetwork(mContext)) {
            String token = mSP.getString("token", null);//token
            String tel = mSP.getString("telPhone", null);//tel
            Logger.i(TAG, "tel.." + tel + "..token.." + token);
            if (!TextUtils.isEmpty(token)) {
                WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
                weakHashMap.put("telPhone", tel);//手机号
                weakHashMap.put("token", token);//token

                String url = App.getRqstUrl(App.headurl + "song/collect", weakHashMap);
                Logger.i(TAG, "url.." + url);
                CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                    @Override
                    public void onFinish(String result, String msg) {
                        if (TextUtils.isEmpty(result)) {
                            getResult(msg);
                        } else {
                            analysisJsonCollect(result);
                        }
                    }
                });
            }
        } else {
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void analysisJsonCollect(String result) {
        ColleResultBean aJson = GsonJsonUtils.parseJson2Obj(result, ColleResultBean.class);
        if (aJson != null) {
            if (aJson.code == 0) {
                Logger.i(TAG, "默认访问收藏列表..." + aJson.toString());
                String str = GsonJsonUtils.parseObj2Json(aJson.data);
                CollentBean1 collentBean1 = GsonJsonUtils.parseJson2Obj(str, CollentBean1.class);
                mSP.putInt("collectListSize", Integer.parseInt(collentBean1.totalCount));
            } else if (aJson.code == 500) {
                getResult(aJson.msg);
            } else {
                getResult(aJson.msg);
            }
        }
    }

    private void getResult(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongToast(mContext, msg);
            }
        });
    }

    /**
     * 默认访问最近播放
     */
    private void getLatelyList() {
        if (NetUtils.hasNetwork(mContext)) {
            String token = mSP.getString("token", null);//token
            String tel = mSP.getString("telPhone", null);//tel
            Logger.i(TAG, "tel.." + tel + "..token.." + token);
            if (!TextUtils.isEmpty(token)) {
                WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
                weakHashMap.put("telPhone", tel);//手机号
                weakHashMap.put("token", token);//token

                String url = App.getRqstUrl(App.headurl + "song/record", weakHashMap);
                Logger.i(TAG, "url.." + url);
                CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                    @Override
                    public void onFinish(String result, String msg) {
                        if (TextUtils.isEmpty(result)) {
                            getResult(msg);
                        } else {
                            analysisJsonLately(result);
                        }
                    }
                });
            }
        } else {
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void analysisJsonLately(String result) {
        ColleResultBean aJson = GsonJsonUtils.parseJson2Obj(result, ColleResultBean.class);
        if (aJson != null) {
            if (aJson.code == 0) {
                Logger.i(TAG, "默认访问最近播放..." + aJson.toString());
                String str = GsonJsonUtils.parseObj2Json(aJson.data);
                CollentBean1 collentBean1 = GsonJsonUtils.parseJson2Obj(str, CollentBean1.class);
                mSP.putInt("latelyListSize", Integer.parseInt(collentBean1.totalCount));
            } else if (aJson.code == 500) {
                getResult(aJson.msg);
            } else {
                getResult(aJson.msg);
            }
        }
    }

    private void networkException() {
        final BtmDialog dialog = new BtmDialog(this, "温馨提示", "网络连接异常,请检查网络状态");
        dialog.confirm.setText("请重试");
        AlertDialogHelper.BtmDialogDerive1(dialog, true, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submData();
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                dialog.dismiss();
            }
        });
    }
}

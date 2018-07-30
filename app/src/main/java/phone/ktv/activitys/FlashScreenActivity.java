package phone.ktv.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.AdverOpenOne;
import phone.ktv.tootls.FULL;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 闪屏页面
 */
public class FlashScreenActivity extends Activity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "FlashScreenActivity";

    private TextView mNumtext;
    private Context mContext;

    private CountDownTimer timer;

    private SPUtil mSP;

    private String mToken;

    private List<AdverOpenOne.AdverOpenTwo> mOpenTwoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);

        initView();
        submData();
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
//        startTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num:
                IntentUtils.thisToOther(mContext, TextUtils.isEmpty(mToken) ? LoginActivity.class : MainActivity.class);
                finish();
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
                IntentUtils.thisToOther(mContext, MainActivity.class);
                finish();
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            IntentUtils.thisToOther(mContext, TextUtils.isEmpty(mToken) ? LoginActivity.class : MainActivity.class);
            finish();
        }
        return super.onKeyDown(keyCode, event);
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
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
//                    mHandler.obtainMessage(UpdateRequestError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson<List<AdverOpenOne>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<AdverOpenOne>>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            Logger.i(TAG, "aJson1.." + aJson.toString());
                            openOne = aJson.getData();
                            if (openOne != null) {
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

                            }

//                            mHandler.sendEmptyMessage(UpdateRequestSuccess);
                        } else {
//                            mHandler.obtainMessage(UpdateRequestError, aJson.getMsg()).sendToTarget();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    playTime = 5000;
                                    startTime();
                                }
                            });

                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
//            mSvProgressHUD.dismiss();
            ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
        }


    }

    private final int UPDATE_AD = 0;
    private int currentad = 0;
    private AdverOpenOne.AdverOpenTwo ad;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        }
    };
    private String videourl;

    private void playvideo() {

        if (!videourl.equals("")) {
            System.out.println("___________________________");
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
        System.out.println("=================");
        mediaPlayer.start();
    }
}

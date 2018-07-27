package phone.ktv.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.AdverOpenOne;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 闪屏页面
 */
public class FlashScreenActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "FlashScreenActivity";

    private TextView mNumtext;
    private Context mContext;

    private CountDownTimer timer;

    private SPUtil mSP;

    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);

        initView();
        submData();
    }


    private void initView() {
        mContext = FlashScreenActivity.this;
        mSP = new SPUtil(mContext);

        mToken = mSP.getString("token", null);//token

        mNumtext = findViewById(R.id.num);
        mNumtext.setOnClickListener(this);

        startTime();
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
        timer.cancel();
    }

    private void startTime() {
        timer = new CountDownTimer(5000, 1000) {
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
                            List<AdverOpenOne> openOne=aJson.getData();
                            if (openOne!=null){
                                Logger.i(TAG, "openOne.." + openOne.toString());
                                AdverOpenOne.AdverOpenTwo openTwo= openOne.get(0).ad;
                                if (openTwo!=null){
                                    Logger.i(TAG, "openTwo.." + openTwo.toString());
                                } else {

                                }
                            } else {

                            }

//                            mHandler.sendEmptyMessage(UpdateRequestSuccess);
                        } else {
//                            mHandler.obtainMessage(UpdateRequestError, aJson.getMsg()).sendToTarget();
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
}

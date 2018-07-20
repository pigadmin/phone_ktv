package phone.ktv.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.WelcomAd;
import phone.ktv.req.OkReq;
import phone.ktv.req.VolleyReq;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;

/**
 * 闪屏页面
 */
public class FlashScreenActivity extends Activity implements View.OnClickListener, VolleyReq.Api {
    private static final String TAG = "FlashScreenActivity";

    private TextView mNumtext;
    private Context mContext;

    private CountDownTimer timer;
    private VolleyReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);
        req = new VolleyReq(this, this);

        initView();
        getad();
    }


    private void initView() {
        mContext = FlashScreenActivity.this;
        mNumtext = findViewById(R.id.num);
        mNumtext.setOnClickListener(this);

        startTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num:
                IntentUtils.thisToOther(mContext, MainActivity.class);
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
            IntentUtils.thisToOther(mContext, MainActivity.class);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private String ad;

    private void getad() {
        ad = App.headurl + "open/ad";
        req.get(ad);
    }

    @Override
    public void finish(String tag, String json) {
        if (tag.equals(ad)) {
            Logger.d(TAG, tag + "\n" + json);
            AJson aJson = App.jsonToObject(json, new TypeToken<AJson<List<WelcomAd>>>() {
            });
        }

    }

    @Override
    public void error(String tag, String json) {
    }
}

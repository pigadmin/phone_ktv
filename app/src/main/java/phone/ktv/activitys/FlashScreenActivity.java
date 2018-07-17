package phone.ktv.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.tootls.IntentUtils;

/**
 * 闪屏页面
 */
public class FlashScreenActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "FlashScreenActivity";

    private TextView mNumtext;
    private Context mContext;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);
        initView();
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
}

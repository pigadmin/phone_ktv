package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import phone.ktv.R;
import phone.ktv.tootls.SPUtil;
import phone.ktv.views.CustomSetupView;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.SwitchButton;

/**
 * 设置
 */
public class SetUpActivity extends phone.ktv.BaseActivity {

    private static final String TAG = "SetUpActivity";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CustomSetupView mCustomSetupView1;//仅WiFi下联网
    private CustomSetupView mCustomSetupView2;//允许流量播放

    private SPUtil mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity_layout);

        initView();
        initLiter();
    }

    private void initView() {
        mContext = SetUpActivity.this;
        mSP = new SPUtil(mContext);

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);
        mCustomSetupView1 = findViewById(R.id.customSetupView1);
        mCustomSetupView2 = findViewById(R.id.customSetupView2);

        mCustomSetupView1.setUpChecked(mSP.getBoolean("isWifiPlay", true));
        mCustomSetupView2.setUpChecked(mSP.getBoolean("isFlowPlay", false));
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mCustomSetupView1.OnCheckSwitchLiter(new MyOnCheckSetupView1());
        mCustomSetupView2.OnCheckSwitchLiter(new MyOnCheckSetupView2());
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
     * 切换wifi联网
     */
    public class MyOnCheckSetupView1 implements SwitchButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            mSP.putBoolean("isWifiPlay", mCustomSetupView1.isUpChecked());
        }
    }

    /**
     * 切换流量播放状态
     */
    public class MyOnCheckSetupView2 implements SwitchButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            mSP.putBoolean("isFlowPlay", mCustomSetupView2.isUpChecked());
        }
    }
}

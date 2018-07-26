package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.tootls.AlertDialogHelper;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.BtmDialog;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 注册页
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    Context mContext;

    private TextView mRegistProtocol;

    private CustomTopTitleView mTopTitleView1;//返回事件
    private CustomEditView customEditView1;//用户名
    private CustomEditView customEditView2;//手机号
    private CustomEditView customEditView3;//验证码
    private CustomEditView customEditView4;//密码

    private TextView mRegister;//注册
    private CheckBox mCheckjiBox;//用户协议

    private Timer timer;
    private int recLen = 1 * 60;
    private boolean isClick = true;

    private WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
    ;

    private SVProgressHUD mSvProgressHUD;

    public static final int RegisRequestSuccess = 100;//注册成功
    public static final int RegisRequestError = 200;//注册失败
    public static final int RegisCodeSuccess = 300;//获取验证码成功
    public static final int RegisCodeError = 400;//获取验证码失败

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RegisRequestSuccess://提交成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, "注册成功");
                    clearInput();
                    finish();
                    break;

                case RegisRequestError://提交失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, "注册失败:" + msg.obj);
                    clearTimerState();
                    break;

                case RegisCodeSuccess://获取验证码成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, "验证码获取成功");
                    customEditView3.setInputTitle((String) msg.obj);
                    break;

                case RegisCodeError://获取验证码失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    clearTimerState();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        initView();
        initLiter();
    }

    private void initView() {
        mContext = RegisterActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);

        mRegister = findViewById(R.id.register_tvw);
        mCheckjiBox = findViewById(R.id.jizhu_ckb);

        mRegistProtocol = findViewById(R.id.regist_protocol_tvw);
        mRegistProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        customEditView1 = findViewById(R.id.customEditView1);
        customEditView2 = findViewById(R.id.customEditView2);
        customEditView3 = findViewById(R.id.customEditView3);
        customEditView4 = findViewById(R.id.customEditView4);
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        customEditView3.sendOnClick(new MyOnClickSendCode());

        mRegistProtocol.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mCheckjiBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mCheckjiBox.isChecked()) {
                mRegister.setBackgroundResource(R.drawable.selector_btn);
                mRegister.setEnabled(true);
            } else {
                mRegister.setBackgroundResource(R.mipmap.denglu_b);
                mRegister.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_protocol_tvw:
                IntentUtils.thisToOther(mContext, UserProtocolActivity.class);
                break;

            case R.id.register_tvw://注册
                registerClick();
                break;
        }
    }

    /**
     * 注册
     */
    private void registerClick() {
        if (TextUtils.isEmpty(customEditView1.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请输入手机号");
            return;
        }
        if (customEditView2.getInputTitle().length() != 11) {
            mSvProgressHUD.showInfoWithStatus("请输入正确的手机号码(11位)");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请点击获取验证码");
            return;
        }
        if (customEditView3.getInputTitle().length() != 6) {
            mSvProgressHUD.showInfoWithStatus("请输入正确的验证码(6位)");
            return;
        }
        if (TextUtils.isEmpty(customEditView4.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请输入密码");
            return;
        }
        if (customEditView4.getInputTitle().length() < 6) {
            mSvProgressHUD.showInfoWithStatus("密码不能小于6位");
            return;
        }
        if (!mCheckjiBox.isChecked()) {
            mSvProgressHUD.showInfoWithStatus("请仔细阅读并勾选,用户注册协议");
            return;
        }

        submLoginData();
    }

    /**
     * 提交注册数据
     */
    private void submLoginData() {
        mSvProgressHUD.showWithStatus("请稍等,数据提交中...");
        weakHashMap.clear();
        weakHashMap.put("telPhone", customEditView2.getInputTitle());//手机号
        weakHashMap.put("veriCode", customEditView3.getInputTitle());//验证码
        weakHashMap.put("pass", customEditView4.getInputTitle());//密码
        weakHashMap.put("username", customEditView1.getInputTitle());//用户名
        String url = App.getRqstUrl(App.headurl + "register", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(RegisRequestError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "注册s.." + s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            mHandler.sendEmptyMessage(RegisRequestSuccess);
                        } else {
                            mHandler.obtainMessage(RegisRequestError, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            isReturn();
        }
    }

    /**
     * 提交验证码
     */
    private void subCodeData() {
        mSvProgressHUD.showWithStatus("请稍等,验证码发送中...");
        weakHashMap.clear();
        weakHashMap.put("telPhone", customEditView2.getInputTitle());//手机号
        String url = App.getRqstUrl(App.headurl + "sendCode", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(RegisCodeError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "验证码s.." + s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            mHandler.obtainMessage(RegisCodeSuccess, aJson.getData()).sendToTarget();
                        } else {
                            mHandler.obtainMessage(RegisCodeError, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 发送验证码
     */
    public class MyOnClickSendCode implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(customEditView2.getInputTitle())) {
                mSvProgressHUD.showInfoWithStatus("请输入手机号");
                return;
            }

            if (NetUtils.hasNetwork(mContext)) {
                if (isClick) {
                    customEditView3.mVerdCode.setBackgroundResource(R.mipmap.zhuce_yzm_1);
                    isClick = false;
                    subCodeData();//提交验证码
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recLen--;
                                    customEditView3.setVerdCode("已发送:" + recLen);
                                    if (recLen == 0) {
                                        recLen = 1 * 60;
                                        clearTimerState();
                                    }
                                }
                            });
                        }
                    }, 1000, 1000);
                } else {
                    ToastUtils.showShortToast(mContext, "正在发送验证码,请在1分钟后点击发送");
                }
            } else {
                mSvProgressHUD.dismiss();
                ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
            }
        }
    }

    private void isReturn() {
        if (!TextUtils.isEmpty(customEditView1.getInputTitle())
                || !TextUtils.isEmpty(customEditView2.getInputTitle())
                || !TextUtils.isEmpty(customEditView3.getInputTitle())
                || !TextUtils.isEmpty(customEditView4.getInputTitle())) {
            final BtmDialog dialog = new BtmDialog(this, "温馨提示", "确定放弃本次操作吗?");
            AlertDialogHelper.BtmDialogDerive1(dialog, false, true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                }
            }, null);
        } else {
            finish();
        }
    }

    /**
     * 重置验证码状态
     */
    private void clearTimerState() {
        isClick = true;
        customEditView3.setVerdCode("点击发送");
        timer.cancel();
        customEditView3.mVerdCode.setBackgroundResource(R.drawable.selector_btn_yanzm);
    }

    private void clearInput() {
        customEditView1.setInputTitle(null);
        customEditView2.setInputTitle(null);
        customEditView3.setInputTitle(null);
        customEditView4.setInputTitle(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isReturn();
        }
        return super.onKeyDown(keyCode, event);
    }
}

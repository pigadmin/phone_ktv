package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.BtmDialog;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 忘记密码
 */
public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgetPsdActivity";
    private Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CustomEditView customEditView1;//手机号
    private CustomEditView customEditView2;//验证码
    private CustomEditView customEditView3;//新密码
    private CustomEditView customEditView4;//确认新密码

    private TextView mRegister;

    public static final int ForgetRequestSuccess=100;//修改成功
    public static final int ForgetRequestError=200;//修改失败
    public static final int ForgetCodeSuccess=300;//获取验证码成功
    public static final int ForgetCodeError=400;//获取验证码失败

    private Timer timer;
    private int recLen = 1 * 60;
    private boolean isClick = true;

    private SVProgressHUD mSvProgressHUD;

    WeakHashMap<String, String> weakHashMap=new WeakHashMap<>();

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ForgetRequestSuccess://提交成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,"找回密码成功");
                    clearInput();
                    break;

                case ForgetRequestError://提交失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,"找回密码失败:"+msg.obj);
                    break;

                case ForgetCodeSuccess://获取验证码成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,"验证码获取成功");
                    customEditView2.setInputTitle((String) msg.obj);
                    break;

                case ForgetCodeError://获取验证码失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    isClick = true;
                    customEditView2.setVerdCode("点击发送");
                    timer.cancel();
                    customEditView2.mVerdCode.setBackgroundResource(R.drawable.selector_btn_yanzm);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psd_activity_layout);
        initView();
        initLiter();
    }

    private void initView() {
        mContext = ForgetPsdActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        customEditView1=findViewById(R.id.customEditView1);
        customEditView2=findViewById(R.id.customEditView2);
        customEditView3=findViewById(R.id.customEditView3);
        customEditView4=findViewById(R.id.customEditView4);

        mRegister=findViewById(R.id.register_tvw);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        customEditView2.sendOnClick(new MyOnClickSendCode());
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tvw://注册
                registerClick();
                break;
        }
    }

    private void registerClick(){
        if (TextUtils.isEmpty(customEditView1.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请输入手机号码");
            return;
        }
        if (customEditView1.getInputTitle().length()!=11){
            mSvProgressHUD.showInfoWithStatus("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请输入验证码");
            return;
        }
        if (customEditView2.getInputTitle().length()!=6){
            mSvProgressHUD.showInfoWithStatus("请输入正确的验证码");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(customEditView4.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请确认新密码");
            return;
        }
        if (customEditView3.getInputTitle().equals(customEditView4.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("2次输入新密码不一致");
            return;
        }

        submRegisterData();
    }

    /**
     * 提交修改数据
     */
    private void submRegisterData(){
        mSvProgressHUD.showWithStatus("请稍等,数据提交中...");
        weakHashMap.clear();
        weakHashMap.put("telPhone", customEditView1.getInputTitle());//手机号
        weakHashMap.put("veriCode", customEditView2.getInputTitle());//验证码
        weakHashMap.put("newPass", customEditView3.getInputTitle());//新密码

        String url = App.getRqstUrl(App.headurl + "changePass", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(ForgetRequestError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG,"s.."+s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson!=null){
                        if (aJson.getCode()==0){
                            mHandler.sendEmptyMessage(ForgetRequestSuccess);
                        } else {
                            mHandler.obtainMessage(ForgetRequestError, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            isReturn();
        }
    }

    /**
     * 发送验证码
     */
    public class MyOnClickSendCode implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(customEditView1.getInputTitle())){
                ToastUtils.showLongToast(mContext,"请输入手机号码");
                return;
            }

            if (NetUtils.hasNetwork(mContext)) {
                if (isClick) {
                    customEditView2.mVerdCode.setBackgroundResource(R.mipmap.zhuce_yzm_1);
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
                                    customEditView2.setVerdCode("已发送:" + recLen);
                                    if (recLen == 0) {
                                        recLen = 1 * 60;
                                        isClick = true;
                                        customEditView2.setVerdCode("点击发送");
                                        timer.cancel();
                                        customEditView2.mVerdCode.setBackgroundResource(R.drawable.selector_btn_yanzm);
                                    }
                                }
                            });
                        }
                    }, 1000, 1000);
                } else {
                    ToastUtils.showShortToast(mContext, "正在发送验证码,请在1分钟后点击发送");
                }
            } else {
                ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
                return;
            }
        }
    }

    /**
     * 提交验证码
     */
    private void subCodeData(){
        mSvProgressHUD.showWithStatus("请稍等,验证码发送中...");
        weakHashMap.clear();
        weakHashMap.put("telPhone", customEditView2.getInputTitle());//手机号
        String url = App.getRqstUrl(App.headurl + "sendCode", weakHashMap);
        Logger.i(TAG, "url.." + url);

        OkhttpUtils.doStart(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //返回失败
                mHandler.obtainMessage(ForgetCodeError, e.getMessage()).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Logger.i(TAG,"验证码s.."+s);
                AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                if (aJson!=null){
                    if (aJson.getCode()==0){
                        mHandler.obtainMessage(ForgetCodeSuccess, aJson.getMsg()).sendToTarget();
                    } else {
                        mHandler.obtainMessage(ForgetCodeError, aJson.getMsg()).sendToTarget();
                    }
                }
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isReturn();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void isReturn(){
        if (!TextUtils.isEmpty(customEditView1.getInputTitle())
                || !TextUtils.isEmpty(customEditView2.getInputTitle())
                || !TextUtils.isEmpty(customEditView3.getInputTitle())
                ||!TextUtils.isEmpty(customEditView4.getInputTitle())) {
            final BtmDialog dialog = new BtmDialog(this, "温馨提示", "确定放弃本次操作吗?");
            AlertDialogHelper.BtmDialogDerive1(dialog, false, true,new View.OnClickListener() {
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

    private void clearInput(){
        customEditView1.setInputTitle(null);
        customEditView2.setInputTitle(null);
        customEditView3.setInputTitle(null);
        customEditView4.setInputTitle(null);
    }
}

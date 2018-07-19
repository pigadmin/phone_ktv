package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.WelcomAd;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 登录页
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private Context mContext;

    private CheckBox mJizhuPsd;//记住密码
    private TextView mWangjiPsd;//忘记密码
    private TextView mLogin;//登陆
    private TextView mReginster;//注册

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CustomEditView customEditView1;//手机号
    private CustomEditView customEditView2;//密码

    public static final int LoginRequestSuccess=100;//登录成功
    public static final int LoginRequestError=200;//登录失败

    private SVProgressHUD mSvProgressHUD;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LoginRequestSuccess://提交成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,"登录成功");

                    break;

                case LoginRequestError://提交失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext,(String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        initView();
        initLiter();
    }

    private void initView() {
        mContext = LoginActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        customEditView1=findViewById(R.id.customEditView1);
        customEditView2=findViewById(R.id.customEditView2);

        mJizhuPsd=findViewById(R.id.jizhu_ckb);
        mWangjiPsd=findViewById(R.id.wangji_tvw);
        mLogin=findViewById(R.id.login_tvw);
        mReginster=findViewById(R.id.register_tvw);
    }

    private void initLiter(){
        mWangjiPsd.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mReginster.setOnClickListener(this);
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wangji_tvw://忘记密码
                IntentUtils.thisToOther(mContext,ForgetPsdActivity.class);
                break;

            case R.id.login_tvw://登录
                loginClick();
                break;

            case R.id.register_tvw://注册
                IntentUtils.thisToOther(mContext,RegisterActivity.class);
                break;
        }
    }

    /**
     * 登录事件
     */
    private void loginClick(){
        if (TextUtils.isEmpty(customEditView1.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请输入手机号码");
            return;
        }
        if (customEditView1.getInputTitle().length()!=11){
            mSvProgressHUD.showInfoWithStatus("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())){
            mSvProgressHUD.showInfoWithStatus("请输入密码");
            return;
        }
        submLoginData();
    }

    /**
     * 提交登录数据
     */
    private void submLoginData(){
        mSvProgressHUD.showWithStatus("请稍等,数据提交中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("telPhone", customEditView1.getInputTitle());//手机号
        weakHashMap.put("pass", customEditView2.getInputTitle());//密码
        String url = App.getRqstUrl(App.headurl + "login", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(LoginRequestError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG,"s.."+s);
                    AJson aJson = App.jsonToObject(s, new TypeToken<AJson<List<WelcomAd>>>() {
                    });
                    if (aJson.getCode()==0){
                        mHandler.sendEmptyMessage(LoginRequestSuccess);
                    } else {
                        mHandler.obtainMessage(LoginRequestError, aJson.getMsg()).sendToTarget();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
